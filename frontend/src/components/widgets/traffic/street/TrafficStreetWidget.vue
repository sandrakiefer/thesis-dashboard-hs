<template>
    <div class="TrafficStreetWidget container-fluid" v-if="trafficStreet">
        <div class="row align-items-center">
            <div class="col-3 d-flex justify-content-center align-items-center">
                <svg class="iconTop">
                    <use href="/src/assets/svg/headlineicons.svg#trafficStreets" />
                </svg>
            </div>
            <div class="col-9 headline text-start">
                Aktuelle Verkehrsmeldungen
            </div>
        </div>
        <hr />
        <div class="row justify-content-center">
            <div class="overflow-auto scrollTrafficStreetTable">
                <div v-if="trafficStreet.traffic.length < 1" class="row pt-3">
                    <div class="col-12 fw-lighter">Für die ausgewählten Straßen liegen aktuell keine Verkehrsmeldungen
                        vor.
                    </div>
                </div>
                <div v-else class="row align-items-center p-2" v-for="index in trafficStreet.traffic.length"
                    :key="index">
                    <div class="col-3 d-flex">
                        <svg class="streetIcon" v-if="trafficStreet.traffic[index - 1].streetName.startsWith('A')">
                            <use href="/src/assets/svg/trafficstreeticons.svg#A" />
                            <text x="50%" y="52%" dominant-baseline="middle" text-anchor="middle" class="streetA">
                                {{ trafficStreet.traffic[index - 1].streetName.substring(1) }}
                            </text>
                        </svg>
                        <svg class="streetIcon" v-if="trafficStreet.traffic[index - 1].streetName.startsWith('B')">
                            <use href="/src/assets/svg/trafficstreeticons.svg#B" />
                            <text x="50%" y="52%" dominant-baseline="middle" text-anchor="middle" class="streetB">
                                {{ trafficStreet.traffic[index - 1].streetName.substring(1) }}
                            </text>
                        </svg>
                        <svg class="streetIcon ps-2">
                            <use :href="`${trafficstreeticons}#${trafficStreet.traffic[index - 1].symbol}`" />
                        </svg>
                    </div>
                    <div class="col-9 text-start infoStreet">
                        {{ trafficStreet.traffic[index - 1].directionFrom }}
                        <font-awesome-icon v-if="trafficStreet.traffic[index - 1].bothDirections"
                            icon="fa-solid fa-arrow-right-arrow-left" />
                        <font-awesome-icon v-else icon="fa-solid fa-arrow-right-long" />
                        {{ trafficStreet.traffic[index - 1].directionTo }}
                        <br />
                        <span class="smallText">{{ trafficStreet.traffic[index - 1].desription }}</span>
                    </div>
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
    <div class="modal fade" :id="'trafficStreetWidgetSettings' + widgetId" tabindex="-1" v-if="trafficStreet && a && b">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Straßenverkehr - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- a selection -->
                    <div class="row px-4 pb-3">
                        <div class="col-12 text-start pb-1">
                            Für welche Autobahnen sollen die Verkehrsmeldungen angezeigt werden?
                        </div>
                        <div class="row scrollStreets">
                            <div v-for="s in a" :key="s" class="col-3 text-start">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="checkPollenType" :value="s"
                                        v-model="settingsA">
                                    <label class="form-check-label" for="checkPollenType">
                                        {{ s }}
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- b selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Für welche Bundesstraßen sollen die Verkehrsmeldungen angezeigt werden?
                        </div>
                        <div class="row scrollStreets">
                            <div v-for="s in b" :key="s" class="col-3 text-start">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="checkPollenType" :value="s"
                                        v-model="settingsB">
                                    <label class="form-check-label" for="checkPollenType">
                                        {{ s }}
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="trafficStreetString" />
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
import trafficstreeticons from '@/assets/svg/trafficstreeticons.svg';
import { getTrafficStreet } from '@/service/widgets/traffic/street/TrafficStreetService';
import type { TrafficStreet } from '@/service/widgets/traffic/street/TrafficStreet';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "TrafficStreetWidget",
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
        const settingsA = ref<string[]>(widget.value.settings.filter(i => i.startsWith('A')));
        const settingsB = ref<string[]>(widget.value.settings.filter(i => i.startsWith('B')));
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsA.value = widget.value.settings.filter(i => i.startsWith('A'));
            settingsB.value = widget.value.settings.filter(i => i.startsWith('B'));
        });
        /**
         * Load traffic street information from service
         */
        const { loadTrafficStreet, loadTrafficStreetA, loadTrafficStreetB } = getTrafficStreet();
        const trafficStreet = ref<TrafficStreet>();
        const a = ref<string[]>();
        const b = ref<string[]>();
        onMounted(async () => {
            await loadTrafficStreetA().then(response => { a.value = response });
            await loadTrafficStreetB().then(response => { b.value = response });
            await update();
        });
        /** 
         * Update traffic street data on changes
         */
        watch([settingsA, settingsB], async () => {
            await update();
        });
        async function update() {
            await loadTrafficStreet([...settingsA.value, ...settingsB.value]).then(response => { trafficStreet.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [...settingsA.value, ...settingsB.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const trafficStreetString = ref(JSON.stringify(trafficStreet.value, null, 2));
        watch(trafficStreet, () => {
            trafficStreetString.value = JSON.stringify(trafficStreet.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && trafficStreetString.value !== "") trafficStreet.value = JSON.parse(trafficStreetString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            trafficStreet,
            a,
            b,
            settingsA,
            settingsB,
            isEditor,
            trafficStreetString,
            trafficstreeticons,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
@import "../../../../assets/scss/variables";

.scrollTrafficStreetTable {
    max-height: 220px;
}

.streetIcon {
    height: 2rem;
}

.streetA {
    fill: white;
    font-weight: bold;
    font-size: 0.8rem;
}

.streetB {
    fill: black;
    font-weight: bold;
    font-size: 0.8rem;
}

.infoStreet {
    line-height: 1rem;
}

.scrollStreets {
    max-height: 100px;
    overflow-y: scroll;
}
</style>
