<template>
    <div class="BioweatherWidget container-fluid" v-if="bioweather">
        <div class="row align-items-center">
            <div class="col-3 d-flex justify-content-center align-items-center">
                <svg class="iconTop">
                    <use href="/src/assets/svg/headlineicons.svg#bioweather" />
                </svg>
            </div>
            <div class="col-9 headline text-start">
                Biowetter für Hessen
            </div>
        </div>
        <hr />
        <div v-if="bioweather.categories.length < 1" class="row pt-3">
            <div class="col-12 fw-lighter">
                Aktuell sind keine Biowetter-Kategorien in den Einstellungen ausgewählt.
            </div>
        </div>
        <div v-else class="overflow-auto categorieList">
            <div class="row text-start m-2 p-2 categorie" v-for="index in bioweather.categories.length" :key="index">
                <div class="col-12 pb-1" style="font-weight: bold">
                    Wettereinfluss auf
                    <span
                        v-if="bioweather.categories[index - 1].name == 'Allgemeine Befinden' || bioweather.categories[index - 1].name == 'Herz- und Kreislaufgeschehen'">das
                    </span>
                    {{ bioweather.categories[index - 1].name }}
                </div>
                <div class="col-12">
                    <ul class="mb-1">
                        <li v-for="list in bioweather.categories[index - 1].effects.length" :key="list">{{
                                bioweather.categories[index - 1].effects[list - 1]
                        }}</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- loading visualisation -->
    <div v-else class="container-fluid">
        <div class="text-center">
            <div class="spinner-border text-secondary" style="width: 3rem; height: 3rem;">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
    <!-- settings of the widget -->
    <div class="modal fade" :id="'bioweatherWidgetSettings' + widgetId" tabindex="-1" v-if="categories">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Biowetter - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- pollen selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Für welche Kategorien soll das Biowetter angezeigt werden?
                        </div>
                        <div v-for="categorie in categories" :key="categorie" class="col-12 text-start">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="checkPollenType" :value="categorie"
                                    v-model="settingsCategories">
                                <label class="form-check-label" for="checkPollenType">
                                    <span v-if="categorie.startsWith('Allgemeine')">
                                        {{ [categorie.slice(0, 10), 's', categorie.slice(10)].join('') }}
                                    </span>
                                    <span v-else>
                                        {{ categorie }}
                                    </span>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="bioweatherString" />
                        </div>
                        <div class="col-12">
                            <button type="button" class="btn btn-secondary w-100" data-bs-dismiss="modal"
                                @click="editWidget()">
                                Daten zeitweise zum Testen manipulieren
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch, toRef } from 'vue';
import type { PropType } from 'vue';
import { getBioweather } from '@/service/widgets/bioweather/BioweatherService';
import type { Bioweather } from '@/service/widgets/bioweather/Bioweather';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "BioweatherWidget",
    emits: ["update-widget", "close-edit"],
    props: {
        widget: { type: Object as PropType<Widget>, required: true },
    },
    setup(props, context) {
        /**
         * Load current widget information from props/dashboard and keep it reactive
         */
        const widget = toRef(props, "widget");
        const widgetId = ref<number>(widget.value.pos);
        const settingsCategories = ref<string[]>(widget.value.settings);
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsCategories.value = widget.value.settings;
        })
        /**
         * Load bio weather information from service
         */
        const { loadBioweather, loadBioweatherCategories } = getBioweather();
        const bioweather = ref<Bioweather>();
        const categories = ref<string[]>();
        onMounted(async () => {
            await loadBioweatherCategories().then(response => { categories.value = response });
            await update();
        });
        /** 
         * Update bio weather data on changes
         */
        watch(settingsCategories, async () => {
            await update();
        });
        async function update() {
            await loadBioweather(settingsCategories.value).then(response => { bioweather.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", settingsCategories.value);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const bioweatherString = ref(JSON.stringify(bioweather.value, null, 2));
        watch(bioweather, () => {
            bioweatherString.value = JSON.stringify(bioweather.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && bioweatherString.value !== "") bioweather.value = JSON.parse(bioweatherString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            bioweather,
            categories,
            settingsCategories,
            isEditor,
            bioweatherString,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
@import "../../../assets/scss/variables";

.categorie {
    background-color: $accentgray;
    border-radius: 0.5rem;
}

.categorieList {
    max-height: 210px;
}

ul {
    font-size: 0.9rem;
}
</style>
