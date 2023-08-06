<template>
    <div class="col widget-container">
        <div class="widget-overlay">
            <!-- icons for touch -->
            <span v-if="isTouch">
                <!-- vertical icons -->
                <span v-if="isOneCol">
                    <font-awesome-icon icon="fa-solid fa-caret-up"
                        class="shiftTB position-absolute top-0 start-50 translate-middle-x" @click="dndMoveUp(id)" />
                    <font-awesome-icon icon="fa-solid fa-caret-down"
                        class="shiftTB position-absolute bottom-0 start-50 translate-middle-x"
                        @click="dndMoveDown(id)" />
                </span>
                <!-- horizontal icons -->
                <span v-else>
                    <font-awesome-icon icon="fa-solid fa-caret-left"
                        class="shiftLR position-absolute top-50 start-0 translate-middle-y ps-5"
                        @click="dndMoveUp(id)" />
                    <font-awesome-icon icon="fa-solid fa-caret-right"
                        class="shiftLR position-absolute top-50 end-0 translate-middle-y pe-5"
                        @click="dndMoveDown(id)" />
                </span>
            </span>
            <!-- icons for desktop (drag and drop) -->
            <span v-else>
                <font-awesome-icon icon="fa-solid fa-up-down-left-right"
                    class="move position-absolute top-50 start-50 translate-middle" />
            </span>
            <font-awesome-icon v-if="name != 'info'" icon="fa-solid fa-gear"
                class="settings position-absolute top-0 end-0" data-bs-toggle="modal" :data-bs-target="settingslink"
                :key="id" />
            <font-awesome-icon icon="fa-solid fa-trash-can" class="delete position-absolute bottom-0 end-0"
                @click="deleteWidget(id)" />
        </div>
        <slot></slot>
    </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue';
import type { PropType } from 'vue';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_dnd, useDashboardService_editWidgets } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: 'WidgetContainer',
    props: {
        widget: { type: Object as PropType<Widget>, required: true },
    },
    setup(props) {
        const { deleteWidget } = useDashboardService_editWidgets();
        const { dndMoveDown, dndMoveUp, isTouch, isOneCol } = useDashboardService_dnd();
        return {
            settingslink: computed(() => '#' + props.widget.type + 'WidgetSettings' + props.widget.pos),
            name: computed(() => props.widget.type),
            id: computed(() => props.widget.pos),
            isTouch,
            isOneCol,
            deleteWidget,
            dndMoveUp,
            dndMoveDown,
        }
    },
})
</script>

<style lang="scss">
.widget-container {
    background-color: white;
    position: relative;
    text-align: center;
    border-radius: 1rem;
    height: 325px;
    display: flex;
    align-items: center;
}

.visible {
    .widget-overlay {
        visibility: visible !important;
        cursor: move;
        cursor: grab;
        cursor: -moz-grab;
        cursor: -webkit-grab;
    }

    .widget-overlay:active {
        cursor: grabbing;
        cursor: -moz-grabbing;
        cursor: -webkit-grabbing;
    }
}

.widget-overlay {
    position: absolute;
    top: 0;
    right: 0;
    width: 100%;
    height: 100%;
    border-radius: 1rem;
    background: rgba(166, 166, 166, 0.8);
    z-index: 2;
    visibility: hidden;
}

.settings,
.delete {
    margin: 0.7rem 1rem;
    font-size: 2.5rem;
    cursor: pointer;
}

.move {
    font-size: 6rem;
}

.shiftLR {
    font-size: 8rem;
    margin: 0 3rem;
    cursor: pointer;
}

.shiftTB {
    font-size: 8rem;
    margin: 3rem 0;
    cursor: pointer;
}
</style>
