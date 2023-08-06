export interface Pollen {
    location: string;
    pollution: Array<Pollution>;
}

interface Pollution {
    name: string;
    today: number;
    tomorrow: number;
}
