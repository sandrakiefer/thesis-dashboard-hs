<template>
    <div class="CoronaWidget container-fluid" v-if="corona">
        <div class="row align-items-center">
            <div class="col-3 d-flex justify-content-center align-items-center">
                <svg class="iconTop">
                    <use href="/src/assets/svg/headlineicons.svg#corona" />
                </svg>
            </div>
            <div class="col-9 headline alig-items-left">
                Coronazahlen für<br>{{ corona.location }}
            </div>
        </div>
        <hr />
        <div class="row">
            <div class="col-4 numbers">
                {{ corona.incidence }}
            </div>
            <div class="col-4 numbers">
                {{ corona.cases }}
            </div>
            <div class="col-4 numbers">
                <span v-if="corona.compareLastWeek > 1">+</span>
                <span v-else>-</span>
                {{ corona.compareLastWeekPercent }}%
            </div>
        </div>
        <div class="row">
            <div class="col-4">
                7-Tage<br>Inzidenz
            </div>
            <div class="col-4">
                Fälle<br>insgesamt
            </div>
            <div class="col-4">
                Vergleich<br>Vorwoche
            </div>
        </div>
        <hr />
        <div class="row justify-content-center py-1 px-3">
            <div class="col-6 position-relative">
                <a target="_blank" rel="noopener noreferrer"
                    href="https://www.hessenschau.de/panorama/corona-hessen-ticker-358.html">
                    <img
                        src="https://www.hessenschau.de/panorama/corona-coronavirus-sujet-102~_t-1583235764383_v-16to9__retina.jpg">
                    <p class="text-overlay position-absolute top-50 start-50 translate-middle">Ticker</p>
                </a>
            </div>
            <div class="col-6">
                <a target="_blank" rel="noopener noreferrer"
                    href="https://www.hessenschau.de/panorama/corona-infektionen-hessen-karte-100.html">
                    <img
                        src="https://www.hessenschau.de/gesellschaft/corona-karte-aktuelle-zahlen-100~_t-1617883236505_v-16to9__retina.jpg">
                </a>
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
    <div class="modal fade" :id="'coronaWidgetSettings' + widgetId" tabindex="-1" v-if="locations">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Corona - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- location selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Für welchen Standort sollen die Coronazahlen angezeigt werden?
                        </div>
                        <div class="col-12">
                            <LocationSelectSearch :locations="locations" :selected="settingsLocation" :key="widget.pos"
                                @selected-location="settingsLocation = $event" />
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="coronaString" />
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
import { defineComponent, onMounted, ref, toRef, watch } from 'vue';
import type { PropType } from 'vue';
import LocationSelectSearch from '@/components/widgets/LocationSelectSearch.vue';
import { getCorona } from '@/service/widgets/corona/CoronaService';
import type { Corona } from '@/service/widgets/corona/Corona';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "CoronaWidget",
    components: { LocationSelectSearch },
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
        const settingsLocation = ref<string>(widget.value.settings[0]);
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsLocation.value = widget.value.settings[0];
        });
        /**
         * Load corona information from service
         */
        const { loadCorona, loadCoronaLocations } = getCorona();
        const corona = ref<Corona>();
        const locations = ref<string[]>();
        onMounted(async () => {
            await loadCoronaLocations().then(response => { locations.value = response });
            await update();
        });
        /** 
         * Update corona data on changes
         */
        watch(settingsLocation, async () => {
            await update();
        });
        async function update() {
            await loadCorona(settingsLocation.value).then(response => { corona.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [settingsLocation.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const coronaString = ref(JSON.stringify(corona.value, null, 2));
        watch(corona, () => {
            coronaString.value = JSON.stringify(corona.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && coronaString.value !== "") corona.value = JSON.parse(coronaString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            corona,
            locations,
            settingsLocation,
            isEditor,
            coronaString,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
img {
    width: 100%;
    border-radius: 0.5rem;
    max-width: 200px;
}

.text-overlay {
    color: white;
    font-weight: bold;
    font-size: 1rem;
    z-index: 1;
}

.numbers {
    font-weight: bold;
    font-size: 1.2rem;
}
</style>
