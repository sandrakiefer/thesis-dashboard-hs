import type { Dashboard } from '@/service/dashboard/Dashboard';

export interface User {
    email: string;
    firstname: string;
    lastname: string;
    timeslots: Array<Timeslot>;
    dashboards: Array<Dashboard>;
    password: string;
    role: string;
}

interface Timeslot {
    startTime: string;
    endTime: string;
}
