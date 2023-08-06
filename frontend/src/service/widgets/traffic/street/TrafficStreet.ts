export interface TrafficStreet {
    traffic: Array<Street>;
}

interface Street {
    streetName: string;
    symbol: string;
    directionFrom: string;
    directionTo: string;
    desription: string;
    bothDirections: string;
}
