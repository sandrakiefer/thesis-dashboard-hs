import { reactive, ref, computed } from 'vue';
import { useSignUpService } from '@/service/auth/SignUpService';
import { useDashboardService } from '@/service/dashboard/DashboardService';
import { State } from '@/service/principal/Principal';
import type { Principal, Token, TokenPayload } from '@/service/principal/Principal';
import { VueCookieNext } from 'vue-cookie-next';


/* -------------------------------------------------------------------------- */
/*                          Principal service setup.                          */
/* -------------------------------------------------------------------------- */

/**
 * State management for principal.
 */
const principal: Principal = reactive({
    token: { token: "" },
    email: "",
    expires: 0,
    issuedAt: 0
});

/** 
 * State management for login state.
 */
const state = ref(State.LOGGED_OUT);

/**
 * Clears the saved data for principal from the state.
 * Used to restore the initial state when the user logs out.
 */
function clearStore() {
    principal.token = { token: "" };
    principal.email = "";
    principal.expires = 0;
    principal.issuedAt = 0;
    VueCookieNext.removeCookie("token");
    VueCookieNext.removeCookie("currentDashboardId");
    useSignUpService().clearSignUpServiceState();
    useDashboardService().clearDashboardServiceState();
}


/* -------------------------------------------------------------------------- */
/*                      Functions to set token and state.                     */
/* -------------------------------------------------------------------------- */

/**
 * Processes the information and stores the token in the state and cookies.
 * 
 * @param token valid jwt token
 */
function setToken(token: Token) {
    const tokenPayload: TokenPayload = JSON.parse(atob(token.token.split('.')[1]));
    if (principal !== undefined) {
        principal.token = token;
        principal.email = tokenPayload.sub;
        principal.expires = tokenPayload.exp;
        principal.issuedAt = tokenPayload.iat;
        VueCookieNext.setCookie("token", token.token, {
            expire: principal.expires - principal.issuedAt,
            path: "/",
            domain: "",
            secure: "",
            sameSite: "Lax",
        });
        if (state.value !== undefined) state.value = State.LOGGED_IN;
    }
}

/**
 * Switches the status of authentication to the given value.
 * 
 * @param newState status of authentication (loggedin or loggedout)
 */
function setState(newState: State) {
    state.value = newState;
}

/* ---------- Export function to use in the services and components --------- */

export function useToken() {
    const isAuthenticated = computed(() => {
        return (principal !== undefined && principal.token.token !== "" && Math.floor(Date.now() / 1000) < principal.expires);
    });
    if (!isAuthenticated.value && VueCookieNext.isCookieAvailable("token")) {
        setToken({ token: VueCookieNext.getCookie("token") } as Token);
    }
    return {
        principal: computed(() => principal),
        state: computed(() => state.value),
        setToken,
        setState,
        clearStore,
    };
}
