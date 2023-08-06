<template>
    <div class="InfoWidget container-fluid">
        <div class="row align-items-center">
            <div class="col-9 headline text-start ps-4">
                Ihr hessenschau Dashboard
            </div>
            <div class="col-3 d-flex justify-content-end align-items-center">
                <svg class="iconInfo">
                    <image xlink:href="/src/assets/svg/hs_icon.svg" class="iconInfo" />
                </svg>
            </div>
        </div>
        <hr />
        <div class="row align-items-center pb-2 pt-2">
            <div class="col-2">
                <button type="button" class="btn btn-secondary infoButton" disabled>
                    <font-awesome-icon icon="fa-solid fa-gear" class="infoIcon" />
                </button>
            </div>
            <div class="col-10 text-start infoText">
                Öffnen Sie die Einstellungen, um Ihr Dashboard zu bearbeiten.
            </div>
        </div>
        <div class="row align-items-center pb-2">
            <div class="col-2">
                <button type="button" class="btn btn-secondary infoButton" disabled>
                    <font-awesome-icon icon="fa-solid fa-circle-plus" class="infoIcon" />
                </button>
            </div>
            <div class="col-10 text-start infoText">
                Fügen Sie Widgets hinzu, die für Sie relevante Informationen beinhalten.
            </div>
        </div>
        <div class="row align-items-center pb-2">
            <div class="col-2">
                <button type="button" class="btn btn-secondary infoButton" disabled>
                    <font-awesome-icon icon="fa-solid fa-up-down-left-right" class="infoIcon" />
                </button>
            </div>
            <div class="col-10 text-start infoText">
                Aktivieren Sie den Bearbeitungsmodus und ordnen Sie die Widgets wie gewünscht an.
            </div>
        </div>
        <div class="row align-items-center pb-2">
            <div class="col-2">
                <button type="button" class="btn btn-secondary infoButton" disabled>
                    <font-awesome-icon icon="fa-solid fa-share-nodes" class="infoIcon" />
                </button>
            </div>
            <div class="col-10 text-start infoText">
                Speichern und teilen Sie Ihr erstelltes Dashboard mit anderen.
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { PropType } from 'vue';
import type { Widget } from '@/service/dashboard/Dashboard';

export default defineComponent({
    name: "InfoWidget",
    props: {
        widget: { type: Object as PropType<Widget>, required: true },
    },
});
</script>

<style lang="scss">
.iconInfo {
    height: 3.2rem;
}

.infoButton {
    padding: 0.2rem !important;
    width: 2.7rem;
    height: 2.7rem;
}

.infoIcon {
    width: 2.2rem;
    height: 2.2rem;
}

.infoText {
    font-size: 0.9rem;
}
</style>
