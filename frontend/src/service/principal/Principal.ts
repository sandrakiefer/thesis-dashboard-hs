export interface Principal {
    token: Token;
    email: string;
    expires: number;
    issuedAt: number;
}

export enum State {
    LOGGED_IN,
    LOGGED_OUT,
}

export interface Token {
    token: string;
}

export interface TokenPayload {
    sub: string;
    exp: number;
    iat: number;
}
