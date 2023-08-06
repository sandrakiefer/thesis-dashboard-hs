<template>
    <div class="card text-center mb-5">
        <div class="card-body px-5">
            <form @submit.prevent="login()" novalidate>
                <div class=" my-4">
                    <label class="form-label text-muted mb-1">E-Mail Adresse</label>
                    <input type="email" class="form-control text-center"
                        :class="{ validationInput: validError && !validEmail }" v-model="signIn.email">
                    <label v-if="validError && !validEmail" class="validationText">Bitte geben Sie Ihre E-Mail Adresse
                        ein.</label>
                </div>
                <div class="mb-4">
                    <label class="form-label text-muted mb-1">Passwort</label>
                    <input type="password" class="form-control text-center"
                        :class="{ validationInput: validError && !validPassword }" v-model="signIn.password">
                    <label v-if="validError && !validPassword" class="validationText">Bitte geben Sie Ihr Passwort
                        ein.</label>
                </div>
                <div class="alert alert-danger mb-5" role="alert"
                    v-if="((errorMessage == '400') || (errorMessage == '401')) && validError">
                    Bei der Anmeldung ist etwas schief gelaufen, bitte überprüfen Sie Ihre Angaben auf Korrektheit.
                    Wenn Sie noch keinen Account erstellt haben, können Sie sich stattdessen hier <a href="#"
                        @click="switchToSignUp()" class="alert-link">registrieren</a>.
                </div>
                <button type="submit" class="btn btn-primary mb-4">
                    <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"
                        aria-hidden="true"></span>
                    Anmelden
                </button>
            </form>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import router from '@/router';
import { useSignUpService_validation } from '@/service/auth/SignUpService';
import { useSignInService } from '@/service/auth/SignInService';
import type { SignIn } from '@/service/auth/SignIn';

export default defineComponent({
    name: "SignIn",
    emits: ["unsuccessful-login"],
    setup(props, context) {
        const { checkNotEmpty } = useSignUpService_validation();
        const { doSignIn, errorMessage } = useSignInService();
        const signIn = ref<SignIn>({ email: "", password: "" });
        const loading = ref(false);
        const validEmail = computed(() => checkNotEmpty(signIn.value.email));
        const validPassword = computed(() => checkNotEmpty(signIn.value.password));
        const validError = ref(false);
        async function login() {
            if (validEmail.value && validPassword.value) {
                loading.value = true;
                await doSignIn(signIn.value);
                setTimeout(async () => {
                    loading.value = false;
                    if (errorMessage.value == "200") await router.push("/dashboard");
                    if (errorMessage.value == "400" || errorMessage.value == "401") validError.value = true;
                }, 100);
            } else {
                validError.value = true;
            }
        }
        function switchToSignUp(): void {
            context.emit("unsuccessful-login");
        }
        return {
            signIn,
            validEmail,
            validPassword,
            validError,
            errorMessage: computed(() => errorMessage.value),
            login,
            switchToSignUp,
            loading,
        }
    },
});
</script>

<style lang="scss">
</style>
