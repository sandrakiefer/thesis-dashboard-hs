<template>
    <div class="container-fluid h-100 fill" style="min-height: 100vh !important">
        <div class="row justify-content-sm-center h-100">
            <div class="col-xxl-4 col-xl-5 col-lg-5 col-md-7 col-sm-9 pt-5">
                <svg viewBox="0 0 234 30" preserveAspectRatio="xMidYMid meet" class="m-5">
                    <g fill="#FFFFFF">
                        <path
                            d="M75.3 17.6c3.9.3 6.2 2.2 6.2 5.9 0 4.4-3.7 6.6-8.7 6.6-3.4 0-6.3-.6-8.8-3l2.8-2.8c1.7 1.7 4 2.2 6 2.2 2.6 0 4.6-.9 4.6-2.9 0-1.4-.8-2.3-2.8-2.5l-3.3-.3c-3.9-.3-6.2-2.1-6.2-5.8 0-4.1 3.5-6.4 7.9-6.4 3.1 0 5.8.7 7.8 2.4l-2.6 2.7c-1.3-1.1-3.2-1.5-5.2-1.5-2.5 0-3.8 1.1-3.8 2.7 0 1.2.7 2.2 2.9 2.4l3.2.3m-20.6 0-3.3-.3c-2.2-.2-2.9-1.2-2.9-2.4 0-1.6 1.3-2.7 3.8-2.7 2 0 3.8.4 5.2 1.5l2.6-2.7c-1.9-1.7-4.6-2.4-7.8-2.4-4.4 0-7.9 2.3-7.9 6.4 0 3.7 2.3 5.5 6.2 5.8l3.3.3c2 .2 2.8 1.1 2.8 2.5 0 2-2 2.9-4.6 2.9-2.1 0-4.3-.5-6-2.2l-2.8 2.8c2.5 2.4 5.4 3 8.8 3 5 0 8.7-2.3 8.7-6.6.1-3.7-2.2-5.5-6.1-5.9m-18.8.2c0-1.3-.2-1.9-.6-2.9-.7-1.6-2.1-2.6-4.1-2.6s-3.5 1.1-4.2 2.6c-.4 1-.5 1.6-.5 2.9h9.4zm-4.7-9.1c5.6 0 8.9 4.1 8.9 10.1v1.9H26.5c0 3.6 1.9 5.9 5.3 5.9 2.4 0 3.6-.7 5.1-2.1l2.7 2.5c-2.1 2.1-4.2 3.3-7.8 3.3-5.2 0-9.4-2.8-9.4-10.7-.1-6.9 3.4-10.9 8.8-10.9m129.4 3.8c1.7 0 2.7.5 4 2l2.9-2.8c-2-2.1-4-2.9-6.9-2.9-4.7 0-9.3 2.9-9.3 10.7s4.6 10.7 9.3 10.7c2.9 0 4.9-.9 6.9-3l-2.9-2.8c-1.3 1.4-2.4 2-4 2s-3-.7-3.9-1.9c-.9-1.2-1.2-2.7-1.2-5.1 0-2.4.3-3.9 1.2-5.1.9-1.1 2.3-1.8 3.9-1.8m-61.7 5.3c0-1.3-.2-1.9-.6-2.9-.7-1.6-2.1-2.6-4.1-2.6-2 0-3.5 1.1-4.2 2.6-.4 1-.5 1.6-.5 2.9h9.4zm-4.7-9.1c5.6 0 8.9 4.1 8.9 10.1v1.9H89.5c0 3.6 1.9 5.9 5.3 5.9 2.4 0 3.6-.7 5.1-2.1l2.7 2.5c-2.1 2.1-4.2 3.3-7.8 3.3-5.2 0-9.4-2.8-9.4-10.7 0-6.9 3.5-10.9 8.8-10.9m112.6 12h-4.9c-2.5 0-3.8 1.1-3.8 3s1.2 3 3.8 3c1.6 0 2.8-.1 3.9-1.2.6-.6.9-1.6.9-3.1v-1.7zm-4.4-12c5.7 0 8.6 2.4 8.6 7.2v14h-4.1V28c-1.5 1.5-3 2.1-5.6 2.1-2.6 0-4.3-.6-5.6-1.9-1.1-1.1-1.7-2.7-1.7-4.5 0-3.5 2.4-6 7.2-6h5.5v-1.5c0-2.6-1.3-3.9-4.6-3.9-2.3 0-3.4.5-4.6 2l-2.8-2.6c2.2-2.2 4.2-3 7.7-3M141 17.6l-3.3-.3c-2.2-.2-2.9-1.2-2.9-2.4 0-1.6 1.3-2.7 3.8-2.7 2 0 3.8.4 5.2 1.5l2.6-2.7c-1.9-1.7-4.6-2.4-7.8-2.4-4.4 0-7.9 2.3-7.9 6.4 0 3.7 2.3 5.5 6.2 5.8l3.3.3c2 .2 2.8 1.1 2.8 2.5 0 2-2 2.9-4.6 2.9-2.1 0-4.3-.5-6-2.2l-2.8 2.8c2.5 2.4 5.4 3 8.8 3 5 0 8.7-2.3 8.7-6.6.1-3.7-2.2-5.5-6.1-5.9M0 .3V30h4.5V13.1s1.8-.5 4.3-.5c3.5 0 3.4 1.9 3.4 3.4v13.9h4.7V14.3c-.2-3-2.4-5.4-6.8-5.4-2.7 0-5.7.6-5.7.6V0L0 .3zm172.2 0V30h4.5V13.1s1.8-.5 4.3-.5c3.5 0 3.4 1.9 3.4 3.4v13.9h4.7V14.3c-.2-3-2.4-5.4-6.8-5.4-2.7 0-5.7.6-5.7.6V0l-4.4.3zM229.8 9v12.7c0 3.2-1.1 4.6-4.3 4.7-2.9 0-4.3-1.5-4.3-4.7V9H217v13.4c0 2.3-.6 7.8 8.5 7.8s8.5-5.5 8.5-7.8V9h-4.2zM112.5 29.9V17.2c0-3.2 1.1-4.6 4.3-4.7 2.9 0 4.3 1.5 4.3 4.7v12.7h4.2V16.6c0-2.3.6-7.8-8.5-7.8s-8.5 5.5-8.5 7.8V30h4.2z">
                        </path>
                    </g>
                </svg>
                <div class="card text-center mb-2">
                    <div class="card-body px-2 py-1">
                        <ul class="nav nav-pills nav-fill">
                            <li class="nav-item p-1">
                                <a class="nav-link" :class="{ active: showSignIn }" @click="switchSignIn()">Anmelden</a>
                            </li>
                            <li class="nav-item p-1">
                                <a class="nav-link" :class="{ active: showSignUp }"
                                    @click="switchSignUp()">Registrieren</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <component :is="name" @successful-registration="switchSignIn()" @unsuccessful-login="switchSignUp()" />
            </div>
        </div>
    </div>
    <footer class="footer mt-auto">
        <div class="row">
            <div class="col-12 text-center pt-3">
                Bachelor-Arbeit von Sandra Kiefer
            </div>
        </div>
        <div class="row">
            <div class="col-12 text-center fw-lighter pt-2">
                &bdquo;Plattform zur interaktiven Erstellung personalisierter Dashboards
                für die integrierte Visualisierung mehrerer Datenquellen&ldquo;
            </div>
        </div>
        <div class="row">
            <div class="col-12 text-center fw-lighter pt-2">
                <img class="cropped"
                    src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Logo-Hochschule-RheinMain.svg/2560px-Logo-Hochschule-RheinMain.svg.png">
            </div>
        </div>
        <div class="row">
            <div class="col-12 text-center fw-lighter pb-3 extraSmallText">
                Fachbereich Design Informatik Medien<br />
                Studiengang Medieninformatik
            </div>
        </div>
    </footer>
</template>

<script lang="ts">
import { defineComponent, ref, computed, defineAsyncComponent } from 'vue'
import SignIn from '@/components/auth/SignIn.vue';
import SignUp from '@/components/auth/SignIn.vue';

export default defineComponent({
    name: "AuthView",
    components: {
        SignIn,
        SignUp
    },
    setup() {
        const showSignIn = ref(true);
        const showSignUp = ref(false);
        const name = computed(() => {
            if (showSignIn.value) {
                return defineAsyncComponent(() => import("@/components/auth/SignIn.vue"));
            }
            if (showSignUp.value) {
                return defineAsyncComponent(() => import("@/components/auth/SignUp.vue"));
            }
            return '';
        });
        const switchSignIn = () => {
            showSignUp.value = false;
            showSignIn.value = true;
        };
        const switchSignUp = () => {
            showSignIn.value = false;
            showSignUp.value = true;
        };
        return {
            name,
            showSignIn,
            showSignUp,
            switchSignIn,
            switchSignUp,
        }
    },
})
</script>

<style lang="scss">
@import "../assets/scss/variables";

.fill {
    background-color: #005293;
    padding-bottom: 100px;
}

.cropped {
    width: 200px;
    height: 40px;
    object-fit: cover;
}

.nav-link {
    background-color: $accentgray  !important;
    color: black !important;
    font-weight: bold;

    &.active {
        background-color: $hsblue  !important;
        color: white !important;
    }
}

footer {
    background-color: white;
}
</style>