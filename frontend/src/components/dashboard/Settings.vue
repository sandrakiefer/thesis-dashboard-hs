<template>
    <div class="modal fade" id="dashboardSettingsModal" data-bs-focus="false" tabindex="-1" v-if="occupiedTime">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-11 settingsHeadline">
                                Einstellungen
                            </div>
                            <div class="col-1 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="container-fluid">
                        <!-- dashboard selection -->
                        <div class="row" v-if="(dashboardList.length > 1)">
                            <div class="col-12 pb-1">
                                Welches Dashboard soll angezeigt werden?
                            </div>
                            <div class="col-12">
                                <div class="btn-group pb-4">
                                    <button type="button" class="btn btn-primary dropdown-toggle btn-sm"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                        <span v-if="autoDash">Automatisch nach Zeit ({{ currentDashboardName }}) </span>
                                        <span v-else>{{ currentDashboardName }} </span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li v-for="d in dashboardList" :key="d.id">
                                            <a class="dropdown-item smallText"
                                                @click="loadDashboard(d.id); autoDash = false;">
                                                {{ d.name }}
                                            </a>
                                        </li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                            <a class="dropdown-item smallText"
                                                @click="loadDashboard(-1); autoDash = true;">
                                                Dashboard automatisch nach der Zeit anzeigen
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- location synchro -->
                        <div class="row align-items-center pb-4">
                            <div class="col-10">
                                Synchronisation des Standortes
                            </div>
                            <div class="col-2">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" role="switch"
                                        id="flexSwitchCheckChecked" v-model="synchroActive"
                                        @change="updateSynchroActive(synchroActive)">
                                </div>
                            </div>
                        </div>
                        <!-- widget selection -->
                        <div class="row">
                            <div class="col-12 pb-1">
                                Widgets hinzufügen
                            </div>
                        </div>
                        <div class="row row-cols-4 pb-4">
                            <div class="col-md-auto pb-2" v-for="widget in widgetsToAdd" :key="widget">
                                <button type="button" class="btn btn-primary widgetButton"
                                    @click="addWidgetToDashboard(widget)">
                                    <svg>
                                        <use :href="`${widgeticons}#${widget}`" />
                                    </svg>
                                </button>
                            </div>
                        </div>
                        <!-- edit widget order and settings -->
                        <div class="row pb-2">
                            <div class="col-12 pb-1">
                                Anordnung und Einstellungen der Widgets ändern
                            </div>
                            <div class="col-12">
                                <button type="button" class="btn btn-primary" data-bs-dismiss="modal"
                                    @click="editDashboard()">
                                    <font-awesome-icon icon="fa-solid fa-pen" class="pe-2" />
                                    Widgets bearbeiten
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer justify-content-start">
                    <div class="container-fluid">
                        <div class="row pb-2">
                            <div class="col-10 text-start">Erweiterte Einstellungen</div>
                            <div class="col-2 text-end pe-2">
                                <font-awesome-icon icon="fa-solid fa-chevron-down"
                                    class="settingsExpend collapsedActive" data-bs-toggle="collapse"
                                    data-bs-target="#advancedSettings" @click="isActive = !isActive"
                                    v-show="isActive" />
                                <font-awesome-icon icon="fa-solid fa-chevron-up" class="settingsExpend collapsedActive"
                                    data-bs-toggle="collapse" data-bs-target="#advancedSettings"
                                    @click="isActive = !isActive" v-show="!isActive" />
                            </div>
                        </div>
                        <div class="collapse" id="advancedSettings">
                            <!-- edit timeslots -->
                            <div class="row pb-4 pt-4">
                                <div class="col-12 pb-2">
                                    Wann soll das Dashboard angezeigt werden?
                                </div>
                                <div class="col-12">
                                    <table class="table table-sm table-borderless">
                                        <thead>
                                            <tr>
                                                <th v-for="text in ['', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So', '']"
                                                    :key="text" scope="col" class="text-center pt-3">{{ text }}</th>
                                            </tr>
                                        </thead>
                                        <tbody class="align-middle" v-if="weekdays">
                                            <tr v-for="(t, indexT) in ts" :key="indexT">
                                                <td class="time smallText ps-3 min text-start">
                                                    <span class="extraSmallText">
                                                        {{ getHour(t.startTime) }}-{{ getHour(t.endTime) }} Uhr
                                                    </span>
                                                </td>
                                                <td v-for="(weekday, indexW) in occupiedTime" :key="indexW"
                                                    class="text-center">
                                                    <font-awesome-icon
                                                        v-if="weekday.timeslots.some(e => e.startTime === t.startTime && e.endTime === t.endTime)"
                                                        class="occupied" icon="fa-solid fa-square-minus" />
                                                    <input v-else class="form-check-input" type="checkbox"
                                                        :checked="weekdays.find(e => e.weekdayIndex === indexW)?.timeslots.some(e => e.startTime === t.startTime && e.endTime === t.endTime)"
                                                        @change="changeWeekdays($event, indexW, t)"
                                                        id="flexCheckDefault">
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-6 text-start">
                                    <button type="button" class="btn btn-primary p-1 px-2 w-100" data-bs-toggle="modal"
                                        data-bs-target="#timeSlotSettingsModal">
                                        Uhrzeiten bearbeiten
                                    </button>
                                </div>
                                <div class="col-6 text-end">
                                    <button type="button" class="btn btn-primary p-1 px-2 w-100"
                                        @click="updateWeekdays()">
                                        Zeitplanung speichern
                                    </button>
                                </div>
                            </div>
                            <!-- default dashboard -->
                            <div class="row align-items-center pb-4">
                                <div class="col-10">
                                    Dashboard anzeigen, wenn zu dem Zeitpunkt kein anderes geplant ist?
                                </div>
                                <div class="col-2">
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" role="switch"
                                            id="flexSwitchCheckChecked" v-model="defaultDashboard"
                                            @change="updateDefaultDashboard(defaultDashboard)">
                                    </div>
                                </div>
                            </div>
                            <!-- color picker -->
                            <div class="row align-items-center pb-4">
                                <div class="col-10 text-start">
                                    Hintergrundfarbe des Dashboards
                                </div>
                                <div class="col-2 pe-4">
                                    <input type="color" class="form-control form-control-color" v-model="color"
                                        @focusout="updateColor(color)" />
                                </div>
                            </div>
                            <!-- dashboard management -->
                            <div class="row pb-2">
                                <div class="col-12 pb-1">
                                    Dashboards verwalten
                                </div>
                                <div class="col-12 col-sm-6 pb-2">
                                    <button type="button" class="btn btn-primary w-100" @click="newDashboard()">
                                        <font-awesome-icon icon="fa-solid fa-circle-plus" class="pe-2" />
                                        neu
                                    </button>
                                </div>
                                <div class="col-12 col-sm-6 pb-2">
                                    <button type="button" class="btn btn-primary w-100" @click="deleteDashboard()">
                                        <font-awesome-icon icon="fa-solid fa-trash-can" class="pe-2" />
                                        löschen
                                    </button>
                                </div>
                                <div class="col-12 col-sm-6 pb-2">
                                    <a class="btn btn-primary w-100" :href="downloadDashboard()"
                                        :download="`hsDashboard_${currentDashboardName}.json`">
                                        <font-awesome-icon icon="fa-solid fa-share-nodes" class="pe-2" />
                                        exportieren
                                    </a>
                                </div>
                                <div class="col-12 col-sm-6 pb-2">
                                    <input type="file" id="file" style="display:none;"
                                        @change="updloadDashboard($event)" />
                                    <button type="button" class="btn btn-primary w-100" @click="triggerUpload()">
                                        <font-awesome-icon icon="fa-solid fa-file-arrow-up" class="pe-2" />
                                        laden
                                    </button>
                                </div>
                                <!-- start dashboard management onyl for editors -->
                                <div class="col-12 pb-2 pt-2" v-if="isEditor">
                                    <button type="button" class="btn btn-secondary btn-sm w-100"
                                        @click="updateStartDashboard()">
                                        Aktuelles Dashboard als Start-Dashboard für neue Nutzer festlegen (Info-Widget
                                        wird automatisch hinzugefügt)
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="timeSlotSettingsModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-11 settingsHeadline">
                                Einstellungen - Zeitspannen bearbeiten
                            </div>
                            <div class="col-1 text-end">
                                <font-awesome-icon data-bs-toggle="modal" data-bs-target="#dashboardSettingsModal"
                                    icon="fa-solid fa-xmark" class="dashClose" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="container-fluid pt-1">
                        <div class="row" v-for="(t, index) in ts" :key="index">
                            <div class="col-12 pb-1">
                                {{ index + 1 }}. Zeitspanne von {{ getHour(t.startTime) }} Uhr
                                bis {{ getHour(t.endTime) }} Uhr
                            </div>
                            <div class="col-6">
                                <div class="btn-group pb-4 w-100">
                                    <button type="button" class="btn btn-primary dropdown-toggle btn-sm"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                        {{ getHour(t.startTime) }}
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li v-for="i in getPossibleTimes(index, true)" :key="i">
                                            <a class="dropdown-item smallText"
                                                @click="changeTimeslots(i, index, true)">{{ i }}</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="btn-group pb-4 w-100">
                                    <button type="button" class="btn btn-primary dropdown-toggle btn-sm"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                        {{ getHour(t.endTime) }}
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li v-for="i in getPossibleTimes(index, false)" :key="i">
                                            <a class="dropdown-item smallText"
                                                @click="changeTimeslots(i, index, false)">{{ i }}</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 text-start">
                            <button type="button" class="btn btn-primary p-1 px-2" data-bs-toggle="modal"
                                data-bs-target="#dashboardSettingsModal" @click="updateTimeslotsUser(ts)">
                                Zeitplanung speichern
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from 'vue';
import { VueCookieNext } from 'vue-cookie-next';
import widgeticons from '@/assets/svg/widgeticons.svg';
import type { Timeslot } from '@/service/dashboard/Dashboard';
import { useDashboardService_dashboardManagement, useDashboardService_editWidgets, useDashboardService_loadInfo, useDashboardService_startDashboard, useDashboardService_synchroLocation, useDashboardService_timeManagement, useDashboardService_utils } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "SettingsDashboard",
    emits: ["add-widget", "edit-dashboard"],
    setup(props, context) {
        /* ----------------------- Imports required services. ----------------------- */
        const { loadDashboard, dashboard, dashboardList, isEditor, widgetsToAdd } = useDashboardService_loadInfo();
        const { updateSynchroActive } = useDashboardService_synchroLocation();
        const { addWidgetToDashboard } = useDashboardService_editWidgets();
        const { updateTimeslotsUser, updateTimeslotsOfDashboard, updateDefaultDashboard, getPossibleTimes, occupiedTime, timeslots } = useDashboardService_timeManagement();
        const { newDashboard, deleteDashboard, downloadDashboard, updloadDashboard, updateColor } = useDashboardService_dashboardManagement();
        const { updateStartDashboard } = useDashboardService_startDashboard();
        const { getHour, toTimeString } = useDashboardService_utils();

        /**
         * Load required information from props/dashboard and keep it reactive
         */
        const isActive = ref(true);
        const color = ref("");
        const synchroActive = ref(false);
        const defaultDashboard = ref(false);
        const weekdays = ref(dashboard.value.weekdays);
        const ts = ref(timeslots.value);
        const displayChange = ref(false);
        const autoDash = ref(false);
        onMounted(async () => {
            autoDash.value = (Number(VueCookieNext.getCookie("currentDashboardId")) < 0) ? true : false;
        });
        watch(dashboard, () => {
            color.value = dashboard.value.bgcolor;
            synchroActive.value = dashboard.value.synchroActive;
            defaultDashboard.value = dashboard.value.defaultDashboard;
            weekdays.value = dashboard.value.weekdays;
        });
        watch(timeslots, () => {
            ts.value = timeslots.value;
        });

        /**
         * Help function to activate the edit mode in the other components.
         */
        function editDashboard() {
            context.emit("edit-dashboard");
        }

        /* -------------------- Time management of the dashboard. ------------------- */

        /**
         * Help function for updating the timeslot of a weekday locally of a dashboard.
         * 
         * @param event true or false (value of the corresponding checkbox)
         * @param index index of the day of the week to be changed
         * @param timeslot timeslot in which the current dashboard should be displayed
         */
        function changeWeekdays(event: any, index: number, timeslot: Timeslot) {
            if (event.target.checked === true) {
                weekdays.value.filter(e => e.weekdayIndex === index)[0].timeslots.push(timeslot);
            } else {
                const pos: number = weekdays.value.filter(e => e.weekdayIndex === index)[0].timeslots.findIndex(e => e.startTime === timeslot.startTime && e.endTime === timeslot.endTime);
                weekdays.value.filter(e => e.weekdayIndex === index)[0].timeslots.splice(pos, 1);
            }
            displayChange.value = true;
        }
        /**
         * Help function for updating the display times of a dashboard in the backend.
         */
        function updateWeekdays() {
            if (displayChange.value) {
                updateTimeslotsOfDashboard(weekdays.value);
                displayChange.value = false;
            }
        }
        /**
         * Change the timelsot of a user with the given index and time.
         * 
         * @param time new time to be save
         * @param index index of th timeslot to change
         * @param isStart true if the time is the start time of the time slot, otherwise false
         */
        function changeTimeslots(time: number, index: number, isStart: boolean) {
            if (isStart) {
                ts.value[index].startTime = toTimeString(time, false);
            } else {
                ts.value[index].endTime = toTimeString(time, true);
            }
        }

        /**
         * Help function Upload a file.
         */
        function triggerUpload() {
            document.getElementById("file")?.click();
        }

        /**
         * Return data for visualization in the view
         */
        return {
            isActive,
            color,
            dashboard,
            currentDashboardName: computed(() => dashboard.value.name),
            dashboardList,
            isEditor,
            widgetsToAdd,
            occupiedTime,
            ts,
            synchroActive,
            defaultDashboard,
            weekdays,
            autoDash,
            widgeticons,
            loadDashboard,
            updateSynchroActive,
            addWidgetToDashboard,
            editDashboard,
            updateColor,
            updateTimeslotsOfDashboard,
            getHour,
            toTimeString,
            updateDefaultDashboard,
            changeWeekdays,
            updateWeekdays,
            getPossibleTimes,
            changeTimeslots,
            updateTimeslotsUser,
            newDashboard,
            updloadDashboard,
            deleteDashboard,
            downloadDashboard,
            triggerUpload,
            updateStartDashboard,
        }
    }
})
</script>

<style lang="scss">
@import "../../assets/scss/variables";

.settingsExpend {
    transform: scale(2, 2);
    cursor: pointer;

    &:hover {
        filter: brightness(2.5);
    }
}

.form-switch .form-check-input {
    height: 1.2rem !important;
    width: 2.5rem !important;
}

.widgetButton {
    height: 4rem;
    width: 4rem;

    svg {
        width: 2.5rem;
        height: 3rem;
    }
}

.timeEdit {
    color: $hsblue;
}

.time {
    line-height: 1;
}

.dashClose {
    transform: scale(2, 2);
    cursor: pointer;

    &:hover {
        filter: brightness(2.5);
    }
}

.form-check-input:checked {
    background-color: $hsblue  !important;
    border: 0;
}

.table {
    background-color: white;
    border-radius: 0.5rem;
}

th {
    font-size: 0.9rem;
    font-weight: normal;
}

td.min {
    width: 1%;
    white-space: nowrap;
}

.occupied {
    color: $accentgray;
}
</style>
