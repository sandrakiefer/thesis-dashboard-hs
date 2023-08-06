<template>
    <div class="card text-center mb-5">
        <div class="card-body px-5">
            <form @submit.prevent="register()" novalidate>
                <div class="row justify-content-center my-4">
                    <div class="col-6">
                        <label class="form-label text-muted mb-1">Vorname</label>
                        <input type="text" class="form-control text-center"
                            :class="{ validationInput: validError && !validFirstName }" v-model="signUp.firstName">
                        <label v-if="validError && !validFirstName" class="validationText">
                            Bitte geben Sie einen Vornamen an.
                        </label>
                    </div>
                    <div class="col-6">
                        <label class="form-label text-muted mb-1">Nachname</label>
                        <input type="text" class="form-control text-center"
                            :class="{ validationInput: validError && !validLastName }" v-model="signUp.lastName">
                        <label v-if="validError && !validLastName" class="validationText">
                            Bitte geben Sie einen Nachnamen an.
                        </label>
                    </div>
                </div>
                <div class="mb-1 pt-3">
                    <label class="form-label text-muted mb-1">E-Mail Adresse</label>
                    <input type="email" class="form-control text-center"
                        :class="{ validationInput: validError && !validEmail }" v-model="signUp.email">
                    <label v-if="validError && !validEmail" class="validationText">
                        Bitte geben Sie eine gültige E-Mail Adresse an.
                    </label>
                </div>
                <div class=" mb-5">
                    <label class="form-label text-muted mb-1">Wiederholung E-Mail
                        Adresse</label>
                    <input type="email" class="form-control text-center"
                        :class="{ validationInput: validError && !validEmailRepetition }"
                        v-model="signUp.emailRepetition">
                    <label v-if="validError && !validEmailRepetition" class="validationText">
                        Die Wiederholung der E-Mail Adresse ist nicht korrekt.
                    </label>
                </div>
                <div class="mb-1">
                    <label class="form-label text-muted mb-1">Passwort</label>
                    <input type="password" class="form-control text-center"
                        :class="{ validationInput: validError && !validPassword }" v-model="signUp.password">
                    <label v-if="validError && !validPassword" class="validationText">
                        Das Passwort muss mind. 8 Zeichen lang sein, einen Großbuchstaben, einen Kleinbuchstaben,
                        eine Ziffer und ein Sonderzeichen enthalten.
                    </label>
                </div>
                <div class="mb-5">
                    <label class="form-label text-muted mb-1">Wiederholung Passwort</label>
                    <input type="password" class="form-control text-center"
                        :class="{ validationInput: validError && !validPasswordRepetition }"
                        v-model="signUp.passwordRepetition">
                    <label v-if="validError && !validPasswordRepetition" class="validationText">
                        Die Wiederholung des Passwortes ist nicht korrekt.
                    </label>
                </div>
                <div class="alert alert-danger mb-5" role="alert" v-if="errorMessage == '400' && validError">
                    Bei der Registrierung ist etwas schief gelaufen, bitte probieren Sie es in einigen Minuten erneut!
                </div>
                <div class="alert alert-danger mb-5" role="alert" v-if="errorMessage == '409' && validError">
                    Mit der E-Mail Adresse wurde bereits ein Account erstellt. Wollen Sie sich stattdessen <a href="#"
                        @click="switchToSignIn()" class="alert-link">anmelden</a>?
                </div>
                <button type="submit" class="btn btn-primary mb-4">
                    <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"
                        aria-hidden="true"></span>
                    Registrieren
                </button>
            </form>
        </div>
    </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from 'vue';
import { useSignUpService, useSignUpService_validation } from '@/service/auth/SignUpService';
import type { SignUp } from '@/service/auth/SignUp';

export default defineComponent({
    name: "SignUp",
    emits: ["successful-registration"],
    setup(props, context) {
        const { checkNotEmpty, checkEquals, checkEmail, checkPassword } = useSignUpService_validation();
        const { doSignUp, errorMessage } = useSignUpService();
        const signUp = ref<SignUp>({
            email: "",
            emailRepetition: "",
            firstName: "",
            lastName: "",
            password: "",
            passwordRepetition: "",
        });
        const loading = ref(false);
        const validFirstName = computed(() => checkNotEmpty(signUp.value.firstName));
        const validLastName = computed(() => checkNotEmpty(signUp.value.lastName));
        const validEmail = computed(() => checkEmail(signUp.value.email));
        const validEmailRepetition = computed(() => checkEmail(signUp.value.emailRepetition) && checkEquals(signUp.value.email, signUp.value.emailRepetition));
        const validPassword = computed(() => checkPassword(signUp.value.password));
        const validPasswordRepetition = computed(() => checkPassword(signUp.value.passwordRepetition) && checkEquals(signUp.value.password, signUp.value.passwordRepetition));
        const validError = ref(false);
        async function register() {
            if (validFirstName.value && validLastName.value && validEmail.value && validEmailRepetition.value && validPassword.value && validPasswordRepetition.value) {
                loading.value = true;
                await doSignUp(signUp.value);
                setTimeout(() => {
                    loading.value = false;
                    if (errorMessage.value == "200") switchToSignIn();
                    if (errorMessage.value == "400" || errorMessage.value == "409") validError.value = true;
                }, 1000);
            } else {
                validError.value = true;
            }
        }
        function switchToSignIn(): void {
            context.emit("successful-registration");
        }
        return {
            signUp,
            validFirstName,
            validLastName,
            validEmail,
            validEmailRepetition,
            validPassword,
            validPasswordRepetition,
            validError,
            errorMessage: computed(() => errorMessage.value),
            switchToSignIn,
            register,
            loading
        }
    },
});
</script>

<style lang="scss">
</style>
