import { computed, reactive } from 'vue';
import { useToken } from '@/service/principal/PrincipalService';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Principal } from '@/service/principal/Principal';
import type { SignIn } from '@/service/auth/SignIn';

/**
 * Set signin infomation for further requests.
 */
const { setToken, principal } = useToken();

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * State management fpr sign in.
 */
const state = reactive({ errorMessage: "" });

/**
 * Function for logging in an already registered user via the endpoint api/auth/signin in the backend. 
 * 
 * @param signInRequest request object that contains all the necessary information for the sign in request
 * @returns promise of jwt token, needed for further requests
 */
async function doSignIn(signInRequest: SignIn): Promise<Principal> {
    await wrappedFetch("api/auth/signin", "POST", JSON.stringify(signInRequest), false)
        .then(async response => {
            if (!response.ok) {
                if (response.status == 400) state.errorMessage = "400";
                if (response.status == 401) state.errorMessage = "401";
                throw new Error("Login failed");
            }
            state.errorMessage = "200";
            const token = await response.json();
            setToken(token);
        })
        .catch((reason) => {
            console.log(reason);
        });
    return principal.value;
}

/* ---------------- Export function to use in the components ---------------- */

export function useSignInService() {
    return {
        doSignIn,
        errorMessage: computed(() => state.errorMessage),
    };
}
