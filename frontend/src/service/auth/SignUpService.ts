import { computed, reactive } from 'vue';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { SignUp } from '@/service/auth/SignUp';


/* -------------------------------------------------------------------------- */
/*                           Sign up service setup.                           */
/* -------------------------------------------------------------------------- */

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * State management fpr sign up.
 */
const state = reactive({ errorMessage: "" });

/**
 * Clears the saved data for sign up from the state.
 * Used to restore the initial state when the user logs out.
 */
function clearSignUpServiceState() {
    state.errorMessage = "";
}


/* -------------------------------------------------------------------------- */
/*            Validation of the several inputs for the registration           */
/* -------------------------------------------------------------------------- */

/**
 * Checks if the given input is empty.
 * 
 * @param input input as string
 * @returns true if the input is not empty, otherwise false
 */
function checkNotEmpty(input: string): boolean {
    return input !== undefined && input !== null && input !== "" && input !== " ";
}

/**
 * Checks if the two given inputs are equal.
 * 
 * @param firstInput first input as string
 * @param secondInput second input as string
 * @returns true if the two given inputs are equal, otherwise false
 */
function checkEquals(firstInput: string, secondInput: string): boolean {
    return firstInput === secondInput;
}

/**
 * Checks whether the given email corresponds to the general format.
 * 
 * @param email email to check as string
 * @returns true if the email is valid, otherwise false
 */
function checkEmail(email: string): boolean {
    const reg = new RegExp("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    return reg.test(email);
}

/**
 * Checks if the given password contains at least one upper case letter, 
 * one lower case letter, one number, one special character and no space.
 * 
 * @param password password to check as string
 * @returns true if the password is valid, otherwise false
 */
function checkPassword(password: string): boolean {
    const regexPassword = new RegExp("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    return regexPassword.test(password);
}

/* ---------------- Export function to use in the components ---------------- */

export function useSignUpService_validation() {
    return {
        checkNotEmpty,
        checkEquals,
        checkEmail,
        checkPassword,
    };
}


/* -------------------------------------------------------------------------- */
/*                          Sign up request service.                          */
/* -------------------------------------------------------------------------- */

/**
 * Function for registering a new user via the endpoint api/auth/signup in the backend.
 * 
 * @param signUpRequest request object that contains all the necessary information for sign up
 */
async function doSignUp(signUpRequest: SignUp) {
    wrappedFetch("api/auth/signup", "POST", JSON.stringify(signUpRequest), false)
        .then(response => {
            if (!response.ok) {
                if (response.status == 400) state.errorMessage = "400";
                if (response.status == 409) state.errorMessage = "409";
                throw new Error("Registration failed");
            }
            state.errorMessage = "200";
        })
        .catch((reason) => {
            console.log(reason);
        });
}

/* ---------------- Export function to use in the components ---------------- */

export function useSignUpService() {
    return {
        errorMessage: computed(() => state.errorMessage),
        doSignUp,
        clearSignUpServiceState,
    };
}
