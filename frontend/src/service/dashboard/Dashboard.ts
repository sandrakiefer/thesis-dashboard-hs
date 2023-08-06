export interface Dashboard {
    name: string;
    synchroActive: boolean;
    bgcolor: string;
    widgets: Array<Widget>;
    weekdays: Array<Weekday>;
    defaultDashboard: boolean;
    lastChange: string;
    startDashboard: boolean;
    id: number;
}

export interface Widget {
    type: string;
    pos: number;
    settings: Array<string>;
}

export interface Weekday {
    name: string,
    weekdayIndex: number,
    timeslots: Array<Timeslot>
}

export interface DashboardInfo {
    name: string;
    id: number;
}

export interface Timeslot {
    startTime: string;
    endTime: string,
}
