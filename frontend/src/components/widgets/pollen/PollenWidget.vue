<template>
    <div class="PollenWidget container-fluid" v-if="pollen">
        <div class="row align-items-center">
            <div class="col-3 d-flex justify-content-center align-items-center">
                <svg class="iconTop">
                    <use href="/src/assets/svg/headlineicons.svg#pollen" />
                </svg>
            </div>
            <div class="col-9 headline alig-items-left">
                Pollenflugvorhersage<br>{{ pollen.location }}
            </div>
        </div>
        <hr />
        <div v-if="pollen.pollution.length < 1" class="row py-3">
            <div class="col-12 fw-lighter">
                Aktuell sind keine Pollenarten für die Pollenflugvorhersage in den Einstellungen ausgewählt.
            </div>
        </div>
        <div v-else class="row justify-content-center">
            <div class="col-10">
                <div class="overflow-auto pollenTable">
                    <table class="table table-sm table-borderless align-middle smallText">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col">heute</th>
                                <th scope="col">morgen</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="index in pollen.pollution.length" :key="index">
                                <th scope="row" class="text-start">{{ pollen.pollution[index - 1].name }}</th>
                                <td class="centeringPol">
                                    <div :class="'circle ' + getColor(pollen.pollution[index - 1].today)">
                                        {{ pollen.pollution[index - 1].today }}
                                    </div>
                                </td>
                                <td>
                                    <div :class="'circle ' + getColor(pollen.pollution[index - 1].tomorrow)">
                                        {{ pollen.pollution[index - 1].tomorrow }}
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <hr />
        <div class="row align-items-center smallText">
            <div class="col-1">
                <div class="circle green">0</div>
            </div>
            <div class="col-2 text-start infoSmall">
                <span class="smallText">keine</span>
            </div>
            <div class="col-1">
                <div class="circle yellow">1</div>
            </div>
            <div class="col-2 text-start infoSmall">
                <span class="smallText">gering</span>
            </div>
            <div class="col-1">
                <div class="circle orange">2</div>
            </div>
            <div class="col-2 text-start infoSmall">
                <span class="smallText">mittel</span>
            </div>
            <div class="col-1">
                <div class="circle red">3</div>
            </div>
            <div class="col-2 text-start infoSmall">
                <span class="smallText">stark</span>
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
    <div class="modal fade" :id="'pollenWidgetSettings' + widgetId" tabindex="-1" v-if="types && locations">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Pollen - Einstellungen
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
                            Für welchen Standort soll die Pollenflugvorhersage angezeigt werden?
                        </div>
                        <div class="col-12">
                            <LocationSelectSearch :locations="locations" :selected="settingsLocation" :key="widget.pos"
                                @selected-location="settingsLocation = $event" />
                        </div>
                    </div>
                    <!-- pollen selection -->
                    <div class="row px-4 pt-4">
                        <div class="col-12 text-start pb-1">
                            Für welche Pollenarten soll die Pollenflugvorhersage angezeigt werden?
                        </div>
                        <div v-for="t in types" :key="t" class="col-3 text-start">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="checkPollenType" :value="t"
                                    v-model="settingsTypes">
                                <label class="form-check-label" for="checkPollenType">
                                    {{ t }}
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="pollenString" />
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
import { getPollen } from '@/service/widgets/pollen/PollenService';
import type { Pollen } from '@/service/widgets/pollen/Pollen';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "PollenWidget",
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
        const settingsTypes = ref<string[]>(widget.value.settings.slice(1));
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsLocation.value = widget.value.settings[0];
            settingsTypes.value = widget.value.settings.slice(1);
        });
        /**
         * Load pollen information from service
         */
        const { loadPollen, loadPollenTypes, loadPollenLocations, getColor } = getPollen();
        const pollen = ref<Pollen>();
        const locations = ref<string[]>();
        const types = ref<string[]>();
        onMounted(async () => {
            await loadPollenTypes().then(response => { types.value = response });
            await loadPollenLocations().then(response => { locations.value = response });
            await update();
        });
        /** 
         * Update pollen data on changes
         */
        watch([settingsLocation, settingsTypes], async () => {
            await update();
        });
        async function update() {
            await loadPollen(settingsLocation.value, settingsTypes.value).then(response => { pollen.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [settingsLocation.value, ...settingsTypes.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const pollenString = ref(JSON.stringify(pollen.value, null, 2));
        watch(pollen, () => {
            pollenString.value = JSON.stringify(pollen.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && pollenString.value !== "") pollen.value = JSON.parse(pollenString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            pollen,
            types,
            locations,
            settingsTypes,
            settingsLocation,
            isEditor,
            pollenString,
            getColor,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
$green: #8ED191;
$yellow: #EAE191;
$orange: #F2CB59;
$red: #DD8181;

.circle {
    border-radius: 50%;
    width: 1.3rem;
    height: 1.3rem;
    text-align: center;
    font-weight: bold;
    margin: 0 auto;

    &.green {
        background: $green;
        border: 2px solid $green;
    }

    &.yellow {
        background: $yellow;
        border: 2px solid $yellow;
    }

    &.orange {
        background: $orange;
        border: 2px solid $orange;
    }

    &.red {
        background: $red;
        border: 2px solid $red;
    }
}

.infoSmall {
    line-height: 1;
}

.centeringPol {
    display: flex;
    align-items: center;
    align-content: center;
}

.pollenTable {
    max-height: 170px;
}
</style>
