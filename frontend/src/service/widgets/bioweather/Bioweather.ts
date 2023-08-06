export interface Bioweather {
    categories: Array<Categories>;
}

interface Categories {
    name: string;
    effects: Array<string>;
}
