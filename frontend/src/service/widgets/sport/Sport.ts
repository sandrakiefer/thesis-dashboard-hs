export interface Sport {
    table: Array<Table>;
    lastMatch: Match;
    nextMatch: Match;
    firstDay: boolean;
    lastDay: boolean;
}

interface Table {
    name: string;
    iconLink: string;
    diff: number;
    position: number;
    matches: number;
    points: number;
}

interface Match {
    team1Name: string
    team1Score: number;
    team1IconLink: string;
    team2Name: string
    team2Score: number;
    team2IconLink: string;
    date: string;
}
