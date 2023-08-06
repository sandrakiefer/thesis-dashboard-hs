<template>
    <div class="container-fluid dashboard" :class="{ edit: edit }" :style="{ backgroundColor: dashboardColor }">
        <div class="row align-items-center pb-2 ps-2">
            <div :class="{ 'col-md-8 col-sm-12': edit, 'col-8': !edit }" class="d-flex align-items-center welcome">
                {{ getGreeting() }} {{ userName }}!
            </div>
            <div v-if="edit" class="col-md-4 col-sm-12 text-md-end text-sm-start pe-3 pb-2">
                <button type="button" class="btn btn-success p-1 px-2" @click="edit = false">
                    <font-awesome-icon icon="fa-solid fa-circle-check" class="pe-2" />
                    Bearbeitung beenden
                </button>
            </div>
            <div v-else class="col-4 text-end pe-3">
                <font-awesome-icon icon="fa-solid fa-gear" class="dashSettings pe-3" data-bs-toggle="modal"
                    data-bs-target="#dashboardSettingsModal" />
                <font-awesome-icon icon="fa-solid fa-right-from-bracket" class="dashSettings" data-bs-toggle="modal"
                    data-bs-target="#dashboardLogoutModal" />
            </div>
        </div>
        <div class="row gy-3 row-cols-1 row-cols-md-2 row-cols-xl-3">
            <div class="col" v-for="(widget, index) in widgets" :key="index" :draggable="edit"
                @dragstart="dndDragStart(index)" @dragenter="dndDragEnter(index)" @dragend="dndDragEnd()"
                @dragover.prevent>
                <WidgetContainer :widget="widget" :key="index" :class="{ visible: edit }">
                    <component :is="widgetComponents.get(widget.type)" :widget="widget" :key="index"
                        @update-widget="updateWidgetSettings(index, $event)" @close-edit="edit = false" />
                </WidgetContainer>
            </div>
        </div>
    </div>
    <!-- logout modal -->
    <div tabindex="-1" class="modal fade" id="dashboardLogoutModal">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-body text-center">
                    Möchten Sie sich wirklich ausloggen?
                </div>
                <div class="modal-footer justify-content-center">
                    <button type="button" class="btn btn-primary m-2" data-bs-dismiss="modal"
                        @click="logout()">Ausloggen</button>
                    <button type="button" class="btn btn-outline-secondary m-2" data-bs-dismiss="modal">Abbruch</button>
                </div>
            </div>
        </div>
    </div>
    <Settings @edit-dashboard="edit = true" />
</template>

<script lang="ts">
import { defineComponent, ref, markRaw, defineAsyncComponent, onMounted, computed } from 'vue';
import router from '@/router';
import type { RouteComponent } from 'vue-router';
import Settings from '@/components/dashboard/Settings.vue';
import WidgetContainer from '@/components/widgets/WidgetContainer.vue';
import { widget_mapping } from '@/assets/widgets/widget_mapping';
import { useToken } from '@/service/principal/PrincipalService';
import { State } from '@/service/principal/Principal';
import { useDashboardService_dnd, useDashboardService_editWidgets, useDashboardService_loadInfo, useDashboardService_utils } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "DashboardContainer",
    components: { WidgetContainer, Settings },
    setup() {
        /* ----------------------- Imports required services. ----------------------- */
        const { loadDashboard, loadDashboardList, loadUserInfo, dashboard, widgets, dashboardList, userName } = useDashboardService_loadInfo();
        const { updateWidgetSettings } = useDashboardService_editWidgets();
        const { dndDragEnter, dndDragStart, dndDragEnd, checkIfTouch, checkColumnCount } = useDashboardService_dnd();
        const { getGreeting } = useDashboardService_utils();
        const { clearStore, setState } = useToken();

        /**
         * Load required information from props/dashboard and keep it reactive
         */
        const widgetComponents = ref(new Map<string, RouteComponent>());
        const edit = ref(false);
        onMounted(async () => {
            await loadUserInfo();
            await loadDashboard(dashboard.value.id);
            await loadDashboardList();
            checkIfTouch();
            checkColumnCount();
            window.addEventListener("resize", checkColumnCount);
            widget_mapping.forEach(widget => {
                const splitPath = widget.component.split('/');
                if (splitPath.length == 2) widgetComponents.value.set(widget.name, markRaw(defineAsyncComponent(() => import(`../widgets/${splitPath[0]}/${splitPath[1]}.vue`))));
                if (splitPath.length == 3) widgetComponents.value.set(widget.name, markRaw(defineAsyncComponent(() => import(`../widgets/${splitPath[0]}/${splitPath[1]}/${splitPath[2]}.vue`))));
            });
        });

        /**
         * Log out current user and delete cached information.
         */
        async function logout() {
            setState(State.LOGGED_OUT);
            clearStore();
            await router.push("/");
        }

        /**
         * Return data for visualization in the view
         */
        return {
            edit,
            widgetComponents,
            widgets,
            dashboardColor: computed(() => dashboard.value.bgcolor),
            dashboardList,
            userName,
            logout,
            updateWidgetSettings,
            dndDragEnter,
            dndDragStart,
            dndDragEnd,
            getGreeting,
        };
    },
})
</script>

<style lang="scss">
@import "../../assets/scss/variables";

.dashboard {
    background-color: $hslightblue;
    padding: 0.8rem 0.2rem;
    border-radius: 1rem;
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.edit {
    border: 5px solid green;
    -moz-box-shadow: inset 0 0 25px green;
    -webkit-box-shadow: inset 0 0 25px green;
    box-shadow: inset 0 0 25px green;
}

.welcome {
    font-weight: bold;
    font-size: 1.5rem;
    color: $hsblue;
}

.dashSettings {
    color: $hsblue;
    height: 2rem;
    cursor: pointer;

    &:hover {
        filter: brightness(0.7);
    }
}
</style>
