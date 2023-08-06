export interface News {
    headlines: Array<Headline>;
}

interface Headline {
    title: string;
    link: string;
}
