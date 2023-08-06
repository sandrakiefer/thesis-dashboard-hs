<template>
    <div class="SportWidget container-fluid" v-if="sport">
        <!-- next match info -->
        <div class="row align-items-center match mt-3">
            <div class="col-4 text-end">{{ sport.nextMatch.team1Name }}</div>
            <div class="col-1 d-flex justify-content-center align-items-center">
                <img :src="sport.nextMatch.team1IconLink" class="iconMatch" />
            </div>
            <div class="col-2 d-flex justify-content-center align-items-center"
                :title="getShortDate(sport.nextMatch.date)">
                <button v-if="checkIfCurrentPlaying(sport.nextMatch.date)" type="button" class="btnSport" disabled>
                    <font-awesome-icon icon="fa-solid fa-futbol" />
                </button>
                <button v-if="sport.nextMatch.team1Score < 0 && !checkIfCurrentPlaying(sport.nextMatch.date)"
                    type="button" class="btnSport" disabled>
                    {{ getDayName(sport.nextMatch.date) }}
                </button>
                <button v-if="sport.nextMatch.team1Score >= 0 && !checkIfCurrentPlaying(sport.nextMatch.date)"
                    type="button" class="btnSport" disabled>
                    {{ sport.nextMatch.team1Score }}:{{ sport.nextMatch.team2Score }}
                </button>
            </div>
            <div class="col-1 d-flex justify-content-center align-items-center">
                <img :src="sport.nextMatch.team2IconLink" class="iconMatch" />
            </div>
            <div class="col-4 text-start">{{ sport.nextMatch.team2Name }}</div>
        </div>
        <!-- last match info -->
        <div class="row align-items-center match">
            <div class="col-4 text-end">{{ sport.lastMatch.team1Name }}</div>
            <div class="col-1 d-flex justify-content-center align-items-center">
                <img :src="sport.lastMatch.team1IconLink" class="iconMatch" />
            </div>
            <div class="col-2 d-flex justify-content-center align-items-center"
                :title="getShortDate(sport.lastMatch.date)">
                <button v-if="checkIfCurrentPlaying(sport.lastMatch.date)" type="button" class="btnSport" disabled>
                    <font-awesome-icon icon="fa-solid fa-futbol" />
                </button>
                <button v-if="sport.lastMatch.team1Score < 0 && !checkIfCurrentPlaying(sport.lastMatch.date)"
                    type="button" class="btnSport" disabled>
                    {{ getDayName(sport.lastMatch.date) }}
                </button>
                <button v-if="sport.lastMatch.team1Score >= 0 && !checkIfCurrentPlaying(sport.lastMatch.date)"
                    type="button" class="btnSport" disabled>
                    {{ sport.lastMatch.team1Score }}:{{ sport.lastMatch.team2Score }}
                </button>
            </div>
            <div class="col-1 d-flex justify-content-center align-items-center">
                <img :src="sport.lastMatch.team2IconLink" class="iconMatch" />
            </div>
            <div class="col-4 text-start">{{ sport.lastMatch.team2Name }}</div>
        </div>
        <hr />
        <!-- table -->
        <table class="table table-sm table-borderless mt-3">
            <thead>
                <tr class="tableSep">
                    <th scope="col">#</th>
                    <th scope="col"> </th>
                    <th scope="col" class="text-start">Verein</th>
                    <th scope="col">Sp.</th>
                    <th scope="col">Diff.</th>
                    <th scope="col">Pkt.</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="index in 5" :key="index">
                    <td>{{ sport.table[index - 1].position }}</td>
                    <td><img :src="sport.table[index - 1].iconLink" class="iconTable" /></td>
                    <td class="text-start">{{ sport.table[index - 1].name }}</td>
                    <td>{{ sport.table[index - 1].matches }}</td>
                    <td>{{ sport.table[index - 1].diff }}</td>
                    <td>{{ sport.table[index - 1].points }}</td>
                </tr>
            </tbody>
        </table>
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
    <div class="modal fade" :id="'sportWidgetSettings' + widgetId" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Sport - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- liga selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Für welche Liga sollen die Sportinformationen angezeigt werden?
                        </div>
                        <div class="col-12 text-start pt-2">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="sportRadioBl1" id="sportRadioBl11"
                                    value="bl1" v-model="settingsLeague">
                                <label class="form-check-label" for="sportRadioBtn1">
                                    1. Bundesliga
                                </label>
                            </div>
                        </div>
                        <div class="col-12 text-start">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="sportRadioBl2" id="sportRadioBl2"
                                    value="bl2" v-model="settingsLeague">
                                <label class="form-check-label" for="sportRadioBtn2">
                                    2. Bundesliga
                                </label>
                            </div>
                        </div>
                    </div>
                    <!-- team selection -->
                    <div class="row px-4 pt-3">
                        <div class="col-12 text-start pb-1">
                            Für welche Mannschaft sollen die Sportinformationen angezeigt werden?
                        </div>
                        <div class="col-12 pt-2">
                            <select class="form-select w-50" v-model="settingsTeam">
                                <option v-for="t in teams" :key="t" :value="t">{{ t }}</option>
                            </select>
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="sportString" />
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
import { getSport } from '@/service/widgets/sport/SportService';
import type { Sport } from '@/service/widgets/sport/Sport';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "SportWidget",
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
        const settingsLeague = ref<string>(widget.value.settings[0]);
        const settingsTeam = ref<string>(widget.value.settings[1]);
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsLeague.value = widget.value.settings[0];
            settingsTeam.value = widget.value.settings[1];
        });
        /**
         * Load sport information from service
         */
        const { loadSport, loadSportTeams, getDayName, getShortDate, checkIfCurrentPlaying } = getSport();
        const sport = ref<Sport>();
        const teams = ref<string[]>();
        onMounted(async () => {
            await loadSportTeams(settingsLeague.value).then(response => { teams.value = response });
            await update();
        });
        /** 
         * Update sport data on changes
         */
        watch(settingsLeague, async () => {
            await loadSportTeams(settingsLeague.value).then(response => {
                teams.value = response;
                settingsTeam.value = teams.value[0];
            });
        });
        watch(settingsTeam, async () => {
            await update();
        });
        async function update() {
            await loadSport(settingsLeague.value, settingsTeam.value).then(response => { sport.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [settingsLeague.value, settingsTeam.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const sportString = ref(JSON.stringify(sport.value, null, 2));
        watch(sport, () => {
            sportString.value = JSON.stringify(sport.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && sportString.value !== "") sport.value = JSON.parse(sportString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            sport,
            teams,
            settingsLeague,
            settingsTeam,
            isEditor,
            sportString,
            getDayName,
            getShortDate,
            updateWidget,
            checkIfCurrentPlaying,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
@import "../../../assets/scss/variables";

.btnSport {
    display: inline-block;
    box-sizing: border-box;
    background-color: $accentgray;
    border: none;
    padding: 0.2rem 0rem;
    text-align: center;
    border-radius: 0.3rem;
    font-size: 1rem;
    font-weight: bold;
    color: black;
    width: 100%;
}

.tableSep {
    border-bottom: 0.5px solid lightgray;
}

img.iconMatch,
img.iconTable {
    pointer-events: none;
    -webkit-user-drag: none;
    user-select: none;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
}

.iconMatch {
    height: 2rem;
    width: auto;
}

.iconTable {
    height: 1rem;
    width: auto;
}

.match {
    margin: 0.2rem 0rem;
}
</style>
