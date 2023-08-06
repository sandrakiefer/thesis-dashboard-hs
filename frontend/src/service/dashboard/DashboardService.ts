import { computed, reactive } from 'vue';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { DashboardInfo, Dashboard, Widget, Timeslot, Weekday } from './Dashboard';
import { widget_mapping } from '@/assets/widgets/widget_mapping';
import { VueCookieNext } from 'vue-cookie-next';
import Swal from 'sweetalert2';


/* -------------------------------------------------------------------------- */
/*                          Dashboard service setup.                          */
/* -------------------------------------------------------------------------- */

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * State management for dashboard.
 */
const state = reactive({
    dashboard: <Dashboard>{},
    dashboardList: Array<DashboardInfo>(),
    userName: "",
    userRole: "",
    dndIndexStart: 0,
    dndIndexFinish: 0,
    occupiedTime: Array<Weekday>(),
    timeslots: Array<Timeslot>(),
    isTouch: false,
    isOneCol: false,
    services: new Map<string, any>(),
});

/**
 * Clears the saved data for dashboards from the state.
 * Used to restore the initial state when the user logs out.
 */
function clearDashboardServiceState() {
    state.dashboard = {} as Dashboard;
    state.dashboardList = Array<DashboardInfo>();
    state.userName = "";
    state.userRole = "";
    state.dndIndexStart = 0;
    state.dndIndexFinish = 0;
    state.occupiedTime = Array<Weekday>();
    state.timeslots = Array<Timeslot>(),
        state.isTouch = false;
    state.isOneCol = false;
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService() {
    return {
        clearDashboardServiceState,
    };
}


/* -------------------------------------------------------------------------- */
/*        Functions to load dashboards and other required information.        */
/* -------------------------------------------------------------------------- */

/**
 * Checks if the id is present or -1 and forwards the request to the appropriate function.
 * If the id is not defined, a check is made to see if an id is stored in 
 * the cookies and the request is passed to the appropriate function.
 * 
 * @param id  id of the dashboard to load, -1 or undefined
 */
async function loadDashboard(id: number) {
    if (state.services.size < 1) await loadWidgetServices();
    if (id == undefined) {
        if (VueCookieNext.isCookieAvailable("currentDashboardId")) {
            const cookieId = Number(VueCookieNext.getCookie("currentDashboardId"));
            if (cookieId < 0) {
                await loadDashboardCurrent();
            } else {
                await loadDashboardFromId(cookieId);
            }
        } else {
            await loadDashboardCurrent();
        }
    } else {
        if (id < 0) {
            await loadDashboardCurrent();
        } else {
            await loadDashboardFromId(id);
        }
    }
}

/**
 * Loads the dashboard with the given id from the endpoint api/dashboard/id in the backend,
 * and stores this dashboard in the state and puts the id in the cookies.
 * 
 * @param id id of the dashboard to load
 */
async function loadDashboardFromId(id: number) {
    wrappedFetch(`api/dashboard/${id}`, "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
            return response.json();
        })
        .then((jsondata) => {
            state.dashboard = jsondata;
            state.dashboard.widgets = state.dashboard.widgets.sort((w1, w2) => w1.pos - w2.pos);
            VueCookieNext.setCookie("currentDashboardId", state.dashboard.id.toString());
        })
        .then(async () => {
            await loadOccupiedTimeslotsOfAllWeekdays();
            await loadTimeslotsUser();
        });
}

/**
 * Load the current dashboard from the endpoint api/dashboard/current in the backend,
 * and stores this dashboard in the state and puts -1 as id in the cookies (means current).
 */
async function loadDashboardCurrent() {
    wrappedFetch("api/dashboard/current", "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
            return response.json();
        })
        .then((jsondata) => {
            state.dashboard = jsondata;
            state.dashboard.widgets = state.dashboard.widgets.sort((w1, w2) => w1.pos - w2.pos);
            VueCookieNext.setCookie("currentDashboardId", "-1");
        })
        .then(async () => {
            await loadOccupiedTimeslotsOfAllWeekdays();
            await loadTimeslotsUser();
        });
}

/**
 * Loads a list of the names of all dashboards and the ids of the current user
 * from the endpoint api/dashboard/all in the backend.
 */
async function loadDashboardList() {
    wrappedFetch("api/dashboard/all", "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
            return response.json();
        })
        .then((jsondata) => {
            state.dashboardList = jsondata;
        });
}

/**
 * Loads the name and role of the current user from the 
 * endpoints api/user/name and api/user/role in the backend.
 */
async function loadUserInfo() {
    await wrappedFetch("api/user/name", "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for user.");
            return response.text();
        })
        .then((jsondata) => {
            state.userName = jsondata;
        });
    await wrappedFetch("api/user/role", "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for user.");
            return response.text();
        })
        .then((jsondata) => {
            state.userRole = jsondata;
        });
}

/**
 * Help function to import the different services of the widgets 
 * and to store them in a map with the name of the widget as key.
 */
async function loadWidgetServices() {
    const imports = await Promise.all(widget_mapping.filter(widget => widget.name !== "info").map(async widget => {
        const splitPath = widget.service.split('/');
        let service;
        if (splitPath.length == 3) {
            service = await import(`../widgets/${splitPath[0]}/${splitPath[1]}/${splitPath[2]}.ts`);
        } else {
            service = await import(`../widgets/${splitPath[0]}/${splitPath[1]}.ts`)
        }
        return { key: widget.name, val: service };
    }));
    state.services = new Map(imports.map(obj => [obj.key, obj.val] as [string, any]));
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_loadInfo() {
    return {
        loadDashboard,
        loadDashboardList,
        loadUserInfo,
        dashboard: computed(() => state.dashboard),
        widgets: computed(() => state.dashboard.widgets),
        dashboardList: computed(() => state.dashboardList),
        userName: computed(() => state.userName),
        isEditor: computed(() => (state.userRole === "EDITOR") ? true : false),
        widgetsToAdd: computed(() => widget_mapping.map(widget => widget.name).filter(name => name !== "info")),
    }
}


/* -------------------------------------------------------------------------- */
/*              Function to update the dashboard in the backend.              */
/* -------------------------------------------------------------------------- */

/**
 * Updates the dashboard in the database with the dashboards from the 
 * current state, using the endpoint api/dashboard/edit in the backend.
 */
async function updateDashboardInDB() {
    wrappedFetch("api/dashboard/edit", "PUT", JSON.stringify(state.dashboard))
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
            return response.json();
        })
        .then((jsondata) => {
            state.dashboard = jsondata;
            state.dashboard.widgets = state.dashboard.widgets.sort((w1, w2) => w1.pos - w2.pos);
        });
}


/* -------------------------------------------------------------------------- */
/*               Functions for synchronization of the location.               */
/* -------------------------------------------------------------------------- */

/**
 * Checks all current widgets in the dashboard for location and returns the first location found.
 * 
 * @returns location, if no widget with location is currently in the dashboard an empty string is returned
 */
function getSynchroValue(): string {
    let location: string;
    for (const widget of state.dashboard.widgets) {
        if (widget.type !== "info") {
            location = state.services.get(widget.type).getLocation(widget);
            if (location) return location;
        }
    }
    return "";
}

/**
 * Updates the given location in all widgets of the current dashboard that are location-based.
 * 
 * @param value value to synchronize
 */
function updateAttributeForAllWidgetsInDashboard(value: string) {
    state.dashboard.widgets.forEach(widget => {
        if (widget.type !== 'info') state.services.get(widget.type).setLocation(widget, value);
    });
}

/**
 * Enabled or disabled synchronization, forwards the request 
 * to the appropriate functions and updates the dashboard in the backend.
 * 
 * @param value true if the dashboard is to be synchronized, otherwise false
 */
function updateSynchroActive(value: boolean) {
    state.dashboard.synchroActive = value;
    updateAttributeForAllWidgetsInDashboard(getSynchroValue());
    updateDashboardInDB();
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_synchroLocation() {
    return {
        updateSynchroActive,
    }
}


/* -------------------------------------------------------------------------- */
/*                Functions to add, delete and modify widgets.                */
/* -------------------------------------------------------------------------- */

/**
 * Adds the widget of the given type to the dashboard and updates the dashboard in the database.
 * Checks if there is a synchronized location and if not, the default settings of the widget are used.
 * 
 * @param name name of the type of the widget
 */
async function addWidgetToDashboard(name: string) {
    let defaultSettings = widget_mapping.find(widget => widget.name === name)?.defaultSettings;
    if (defaultSettings == undefined) defaultSettings = [];
    const newWidget: Widget = { type: name, pos: state.dashboard.widgets.length, settings: defaultSettings };
    if (getSynchroValue()) {
        state.dashboard.widgets.push(state.services.get(name).setLocation(newWidget, getSynchroValue()));
    } else {
        state.dashboard.widgets.push(newWidget);
    }
    state.dashboard.synchroActive ? updateSynchroActive(true) : updateDashboardInDB();
}

/**
 * Deletes the widget with the given index from the dashboard and updates the dashboard in the database.
 * 
 * @param index position of the widget in the dashboard
 */
async function deleteWidget(index: number) {
    state.dashboard.widgets.splice(index, 1);
    updatePos();
    updateDashboardInDB();
}

/**
 * Updates the settings of the widget with the given id and updates the dashboard in the database.
 * 
 * @param index position of the widget in the dashboard
 * @param settings new settings of the widget
 */
async function updateWidgetSettings(index: number, settings: Array<string>) {
    state.dashboard.widgets[index].settings = settings;
    const w = state.dashboard.widgets[index];
    if (state.dashboard.synchroActive) {
        const location = state.services.get(w.type).getLocation(w);
        if (location) updateAttributeForAllWidgetsInDashboard(location);
    }
    updateDashboardInDB();
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_editWidgets() {
    return {
        addWidgetToDashboard,
        deleteWidget,
        updateWidgetSettings,
    }
}


/* -------------------------------------------------------------------------- */
/*    Functions for drag-and-drop / to change the positions of the widgets.   */
/* -------------------------------------------------------------------------- */

/**
 * HTML Drag and Drop API dragstart event when the widget is strated dragging.
 * Drag index start is set to the current position of the widget for switching the widgets later.
 * 
 * @param index index of the widget where the drag is started
 */
function dndDragStart(index: number) {
    state.dndIndexStart = index;
}

/**
 * HTML Drag and Drop API dragenter event when the widget is moved to the position of another one.
 * Dragged widget is set to the moved position and the rest of the widgets are moved to the back.
 * 
 * @param index index of the widget where the drag is entered
 */
function dndDragEnter(index: number) {
    state.dndIndexFinish = index;
    dndSwitchItems();
    state.dndIndexStart = index;
}

/**
 * Swaps the positions of the widgets from start index to finish index.
 */
function dndSwitchItems(): void {
    const x = state.dashboard.widgets[state.dndIndexStart];
    state.dashboard.widgets.splice(state.dndIndexStart, 1);
    state.dashboard.widgets.splice(state.dndIndexFinish, 0, x);
}

/**
 * HTML Drag and Drop API dragend event when th drag of the widget is finished.
 * Updates the positions of the widgets and updates them in the database.
 */
function dndDragEnd(): void {
    updatePos();
    updateDashboardInDB();
}

/**
 * Updates the position attributes with the current position in the array/dashboard.
 */
function updatePos(): void {
    for (let i = 0; i < state.dashboard.widgets.length; i++) {
        state.dashboard.widgets[i].pos = i;
    }
}

/**
 * Drag and drop alternative for touch devices to move the widget down one position.
 * 
 * @param pos position of the widget which should be moved downwards
 */
function dndMoveDown(pos: number): void {
    state.dndIndexStart = pos;
    if ((pos + 1) >= state.dashboard.widgets.length) {
        state.dndIndexFinish = 0;
    } else {
        state.dndIndexFinish = pos + 1;
    }
    dndSwitchItems();
    dndDragEnd();
}

/**
 * Drag and drop alternative for touch devices to move the widget up one position.
 * 
 * @param pos position of the widget which should be moved upwards
 */
function dndMoveUp(pos: number): void {
    state.dndIndexStart = pos;
    if ((pos - 1) < 0) {
        state.dndIndexFinish = state.dashboard.widgets.length - 1;
    } else {
        state.dndIndexFinish = pos - 1;
    }
    dndSwitchItems();
    dndDragEnd();
}

/**
 * Checks if the calling device is touch and updates the corresponding boolean value in the state.
 */
function checkIfTouch() {
    if ("ontouchstart" in document.documentElement) {
        state.isTouch = true;
    } else {
        state.isTouch = false;
    }
}

/**
 * Checks if the calling device has a single-column dashboard and updates the corresponding boolean value in the state.
 */
function checkColumnCount() {
    if (window.innerWidth < 768) {
        state.isOneCol = true;
    } else {
        state.isOneCol = false;
    }
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_dnd() {
    return {
        dndDragStart,
        dndDragEnter,
        dndDragEnd,
        dndMoveDown,
        dndMoveUp,
        checkIfTouch,
        checkColumnCount,
        isTouch: computed(() => state.isTouch),
        isOneCol: computed(() => state.isOneCol),
    }
}


/* -------------------------------------------------------------------------- */
/*                Functions for time management of a dashboard.               */
/* -------------------------------------------------------------------------- */

/**
 * Load the current timeslots of a user from the endpoint api/user/timeslots 
 * in the backend, an stores the timeslots in the state.
 */
async function loadTimeslotsUser() {
    await wrappedFetch("api/user/timeslots", "GET")
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for user.");
            return response.json();
        })
        .then((jsondata) => {
            state.timeslots = jsondata;
        });
}

/**
 * Updates the given timeslots of the current user from the 
 * endpoint api/user/timeslots in the backend.
 * 
 * @param timeslots new timeslots to be saved
 */
async function updateTimeslotsUser(timeslots: Array<Timeslot>) {
    await wrappedFetch("api/user/timeslots", "PUT", JSON.stringify(timeslots))
        .then(response => {
            if (!response.ok) throw new Error("An error occured during communication with the server for user.");
            return response.json();
        })
        .then((jsondata) => {
            state.timeslots = jsondata;
        });
    loadDashboard(state.dashboard.id);
}

/**
 * Load the occupied timeslots of all weekdays for the dashboard with the given id from the endpoint
 * api/user/displaytime/id in the backend, an stores the occupied time in the state.
 */
async function loadOccupiedTimeslotsOfAllWeekdays() {
    setTimeout(async () => {
        await wrappedFetch(`api/user/displaytime/${state.dashboard.id}`, "GET")
            .then(response => {
                if (!response.ok) throw new Error("An error occured during communication with the server for user.");
                return response.json();
            })
            .then((jsondata) => {
                state.occupiedTime = jsondata;
            })
    }, 100);
}

/**
 * Updates the timeslots of the current dashboard and updates it in the database.
 * 
 * @param weekdays new weekdays containing the occoupied timeslots
 */
async function updateTimeslotsOfDashboard(weekdays: Array<Weekday>) {
    state.dashboard.weekdays = weekdays;
    updateDashboardInDB();
}

/**
 * Enabled or disabled if the dashboard is default 
 * and updates the dashboard in the backend.
 * 
 * @param value true if the dashboard is default, otherwise false
 */
async function updateDefaultDashboard(value: boolean) {
    if (state.dashboard.defaultDashboard != value) {
        state.dashboard.defaultDashboard = value;
        updateDashboardInDB();
    }
}

/**
 * Calculates the possible times for displaytime.
 * 
 * @param pos position of the time to be calculated in the array of displaytimes
 * @param isStart true if it is the start time, false if it is the end time
 * @returns array of possible times to change
 */
function getPossibleTimes(pos: number, isStart: boolean): number[] {
    let timeFrom: number;
    let timeUntil: number;
    if (isStart) {
        if (pos - 1 < 0) {
            timeFrom = Number(getHour(state.timeslots[state.timeslots.length - 1].endTime));
        } else {
            timeFrom = Number(getHour(state.timeslots[pos - 1].endTime));
        }
        timeUntil = Number(getHour(state.timeslots[pos].endTime));
    } else {
        timeFrom = Number(getHour(state.timeslots[pos].startTime));
        if (pos + 1 > state.timeslots.length - 1) {
            timeUntil = Number(getHour(state.timeslots[0].startTime));
        } else {
            timeUntil = Number(getHour(state.timeslots[pos + 1].startTime));
        }
    }
    const range = [...Array(24).keys()];
    const getRange = (start: number, end: number) => {
        const ret = [];
        if (start > end) {
            if (start + 1 == 24) start = 0;
            for (let i = start; i < range.length; i++) {
                ret.push(i);
            }
            for (let i = 0; i <= end; i++) {
                ret.push(i);
            }
        } else {
            for (let i = start; i <= end; i++) {
                ret.push(i);
            }
        }
        return ret;
    };
    if (isStart) {
        return getRange(timeFrom, timeUntil).slice(0, -1);
    } else {
        return getRange(timeFrom, timeUntil).slice(1);
    }
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_timeManagement() {
    return {
        updateTimeslotsUser,
        updateTimeslotsOfDashboard,
        updateDefaultDashboard,
        getPossibleTimes,
        occupiedTime: computed(() => state.occupiedTime),
        timeslots: computed(() => state.timeslots),
    }
}


/* -------------------------------------------------------------------------- */
/*      Functions for managing the dashboards via the advanced settings.      */
/* -------------------------------------------------------------------------- */

/**
 * Creates a new dashboard with the endpoint api/dashboard/new 
 * in the backend and asks before for a name not yet assigned.
 */
async function newDashboard() {
    const { value: newName } = await Swal.fire({
        input: 'text',
        inputLabel: 'Bitte geben Sie einen Namen für das neue Dashboard ein.',
        confirmButtonText: 'Neues Dashboard erstellen',
        background: '#D8E9F6',
        customClass: {
            inputLabel: 'sweetAlert2',
            confirmButton: 'btn btn-primary',
        },
        preConfirm: (value) => {
            if (state.dashboardList.some(d => d.name === value)) {
                Swal.showValidationMessage('Bitte geben Sie einen noch nicht vergebenen Namen an.');
            }
            if (!value) {
                Swal.showValidationMessage('Bitte geben Sie einen Namen für das neue Dashboard an.');
            }
        },
    });
    if (newName !== undefined) {
        wrappedFetch(`api/dashboard/new?name=${newName}`, "POST")
            .then(response => {
                if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
                return response.json();
            })
            .then(async (jsondata) => {
                await loadDashboardList();
                state.dashboard = jsondata;
                state.dashboard.widgets = state.dashboard.widgets.sort((w1, w2) => w1.pos - w2.pos);
                VueCookieNext.setCookie("currentDashboardId", state.dashboard.id.toString());
            });
    }
}

/**
 * Deletes the current dashboard with the endpoint api/dashboard/delete in the backend 
 * and verifies beforehand that it must not be the users last dashboard.
 */
async function deleteDashboard() {
    if (state.dashboardList.length < 2) {
        const Toast = Swal.mixin({
            toast: true,
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            background: '#D8E9F6',
            customClass: {
                title: 'sweetAlert2',
            },
        });
        Toast.fire({
            icon: 'error',
            title: 'Sie können nicht ihr letztes Dashboard löschen.',
        });
    } else {
        Swal.fire({
            title: 'Wollen Sie das Dashboard "' + state.dashboard.name + '" wirklich löschen?',
            inputLabel: 'Bitte geben Sie einen Namen für das neue Dashboard ein.',
            showDenyButton: true,
            confirmButtonText: 'löschen',
            denyButtonText: `abbrechen`,
            background: '#D8E9F6',
            customClass: {
                title: 'swal2-popup',
                confirmButton: 'btn btn-primary',
                denyButton: 'btn btn-danger',
            },
        }).then((result) => {
            if (result.isConfirmed) {
                wrappedFetch(`api/dashboard/delete/${state.dashboard.id}`, "DELETE")
                    .then(async (response) => {
                        if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
                        await loadDashboardList();
                        await loadDashboardCurrent();
                    });
                const Toast = Swal.mixin({
                    toast: true,
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: true,
                    background: '#D8E9F6',
                    customClass: {
                        title: 'sweetAlert2',
                    },
                });
                Toast.fire({
                    icon: 'success',
                    title: 'Das Dashboard "' + state.dashboard.name + '" wurde erfolgreich gelöscht.',
                });
            }
        })

    }

}

/**
 * Provides the current dashboard as a JSON file for download.
 * 
 * @returns string where the created file is located
 */
function downloadDashboard(): string {
    const contentType = "application/json";
    const data = JSON.stringify(state.dashboard);
    const blob = new Blob([data], { type: contentType });
    return window.URL.createObjectURL(blob);
}

/**
 * Checks the given file for validity, then loads it and asks for a name not yet assigned.
 * Then, the dashboard is imported into the database with the endpoint api/dashboard/import 
 * in the backend  and displayed upon success.
 * 
 * @param e file to be uploaded
 */
async function updloadDashboard(e: any) {
    const reader = new FileReader();
    let uploadedDashboard = <Dashboard>{};
    reader.onload = (input: any) => {
        try {
            uploadedDashboard = JSON.parse(input.target.result);
        } catch (ex) {
            const Toast = Swal.mixin({
                toast: true,
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                background: '#D8E9F6',
                customClass: {
                    title: 'sweetAlert2',
                },
            });
            Toast.fire({
                icon: 'error',
                title: 'Bitte laden Sie ein gültige Datei hoch.',
            });
        }
    }
    reader.readAsText(e.target.files[0]);
    if (uploadedDashboard !== undefined) {
        const { value: newName } = await Swal.fire({
            input: 'text',
            inputLabel: 'Bitte geben Sie einen Namen für das importierte Dashboard ein.',
            confirmButtonText: 'Dashboard importieren',
            background: '#D8E9F6',
            customClass: {
                inputLabel: 'sweetAlert2',
                confirmButton: 'btn btn-primary',
            },
            preConfirm: (value) => {
                if (state.dashboardList.some(d => d.name === value)) {
                    Swal.showValidationMessage('Bitte geben Sie einen noch nicht vergebenen Namen an.');
                }
                if (!value) {
                    Swal.showValidationMessage('Bitte geben Sie einen Namen für das importierte Dashboard an.');
                }
            },
        });
        if (newName !== undefined) {
            uploadedDashboard.name = newName;
            wrappedFetch("api/dashboard/import", "POST", JSON.stringify(uploadedDashboard))
                .then(response => {
                    if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
                    return response.json();
                })
                .then(async (jsondata) => {
                    await loadDashboardList();
                    await loadOccupiedTimeslotsOfAllWeekdays();
                    state.dashboard = jsondata;
                    state.dashboard.widgets = state.dashboard.widgets.sort((w1, w2) => w1.pos - w2.pos);
                    VueCookieNext.setCookie("currentDashboardId", state.dashboard.id.toString());
                });
        }
    }
}

/**
 * Updates the background color of the dashboard 
 * and refreshes the dashboard in the database.
 * 
 * @param color hex color string
 */
async function updateColor(color: string) {
    state.dashboard.bgcolor = color;
    updateDashboardInDB();
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_dashboardManagement() {
    return {
        newDashboard,
        deleteDashboard,
        downloadDashboard,
        updloadDashboard,
        updateColor,
    }
}


/* -------------------------------------------------------------------------- */
/*           Functions for updating the start dashboard of editors.           */
/* -------------------------------------------------------------------------- */

/**
 * Sets the editor's current dashboard as the new starting dashboard for upcoming new users.
 */
async function updateStartDashboard() {
    if (state.userRole === "EDITOR") {
        wrappedFetch("api/dashboard/start", "PUT", JSON.stringify(state.dashboard))
            .then(response => {
                if (!response.ok) throw new Error("An error occured during communication with the server for dashboard.");
                const Toast = Swal.mixin({
                    toast: true,
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: true,
                    background: '#D8E9F6',
                    customClass: {
                        title: 'sweetAlert2',
                    },
                });
                Toast.fire({
                    icon: 'success',
                    title: 'Start-Dashboard wurde erfolgreich für neue Nutzer hinterlegt.',
                });
            });
    }
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_startDashboard() {
    return {
        updateStartDashboard,
    }
}


/* -------------------------------------------------------------------------- */
/*                               Help functions                               */
/* -------------------------------------------------------------------------- */

/**
 * Returns a greeting according to the current time of day.
 * 
 * @returns greeting phrase as string
 */
function getGreeting(): string {
    const hour = new Date().getHours();
    if (hour >= 5 && hour <= 11) return "Guten Morgen";
    if (hour >= 12 && hour <= 17) return "Guten Tag";
    if (hour >= 18 && hour <= 22) return "Guten Abend";
    return "Hallo";
}

/**
 * Formats the timeslot as string for visualization.
 * 
 * @param time time to parse
 * @returns parsed time as string
 */
function getHour(time: string): string {
    let str = time.slice(0, 2);
    if (str.startsWith("0")) str = str.charAt(str.length - 1);
    if (time.slice(3, 5) == "59") {
        if (Number(str) + 1 >= 24) {
            str = "0";
        } else {
            str = (Number(str) + 1).toString();
        }
    }
    return str;
}

/**
 * Formats timeslot visualization string to the 
 * required string for saving in the database.
 * 
 * @param time time to format as string
 * @param end true if it is the end time, false if it is the start time
 * @returns formatted time as string
 */
function toTimeString(time: number, end: boolean): string {
    let str = time + ":00:00";
    if (end) str = (Number(time) - 1).toString() + ":59:59"
    if (str.charAt(1) == ":") str = "0" + str;
    return str;
}

/* ---------------- Export function to use in the components ---------------- */

export function useDashboardService_utils() {
    return {
        getGreeting,
        getHour,
        toTimeString,
    }
}
