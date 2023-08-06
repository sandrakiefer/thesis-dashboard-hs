<template>
    <div class="NewsWidget container-fluid" v-if="news">
        <div class="row align-items-center">
            <div class="col-3 d-flex justify-content-center align-items-center">
                <svg class="iconTop">
                    <use href="/src/assets/svg/headlineicons.svg#news" />
                </svg>
            </div>
            <div class="col-9 headline text-start">
                Nachrichten:
                <span v-if="settingsType === 'most'">Meistgeklickt</span>
                <span v-if="settingsType === 'latest'">Neueste Beiträge</span>
            </div>
        </div>
        <hr />
        <div v-for="index in 5" :key="index" class="row align-items-center ps-2 pt-1">
            <div class="col-1 text-center headline">
                {{ index }}.
            </div>
            <div class="col-11 text-start headlines">
                <a target="_blank" rel="noopener noreferrer" :href="news.headlines[index - 1].link" class="title"
                    draggable="false">
                    {{ news.headlines[index - 1].title }}
                </a>
            </div>
        </div>
    </div>
    <!-- loading visualisation -->
    <div v-else class="container-fluid">
        <div class="text-center">
            <div class="spinner-border text-secondary" style="width: 3rem; height: 3rem;">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
    <!-- settings of the widget -->
    <div class="modal fade" :id="'newsWidgetSettings' + widgetId" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Nachrichten - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- news type selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Nach welcher Sortierung sollen die Schlagzeilen in den Nachrichten angezeigt werden?
                        </div>
                        <div class="col-12 text-start pt-2">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="newsRadioMost" id="mostRadioBtn"
                                    value="most" v-model="settingsType">
                                <label class="form-check-label" for="mostRadioBtn">
                                    Meistgeklickte Nachrichten
                                </label>
                            </div>
                        </div>
                        <div class="col-12 text-start">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="newsRadioNewest" id="newestRadioBtn"
                                    value="latest" v-model="settingsType">
                                <label class="form-check-label" for="newestRadioBtn">
                                    Neueste Nachrichten
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="newsString" />
                        </div>
                        <div class="col-12">
                            <button type="button" class="btn btn-secondary w-100" data-bs-dismiss="modal"
                                @click="editWidget()">
                                Daten zeitweise zum Testen manipulieren
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch, computed, toRef } from 'vue';
import type { PropType } from 'vue';
import { getNews } from '@/service/widgets/news/NewsService';
import type { News } from '@/service/widgets/news/News';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "NewsWidget",
    emits: ["update-widget", "close-edit"],
    props: {
        widget: { type: Object as PropType<Widget>, required: true },
    },
    setup(props, context) {
        /**
         * Load current widget information from props/dashboard and keep it reactive
         */
        const widget = toRef(props, "widget");
        const widgetId = ref<number>(widget.value.pos);
        const settingsType = ref<string>(widget.value.settings[0]);
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsType.value = widget.value.settings[0];
        });
        /**
         * Load news information from service
         */
        const { loadNews, loadNewsTypes } = getNews();
        const news = ref<News>();
        const types = computed(() => loadNewsTypes());
        onMounted(async () => {
            await update();
        });
        /** 
         * Update news data on changes
         */
        watch(settingsType, async () => {
            await update();
        });
        async function update() {
            await loadNews(settingsType.value).then(response => { news.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [settingsType.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const newsString = ref(JSON.stringify(news.value, null, 2));
        watch(news, () => {
            newsString.value = JSON.stringify(news.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && newsString.value !== "") news.value = JSON.parse(newsString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            news,
            types,
            settingsType,
            isEditor,
            newsString,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
@import "../../../assets/scss/variables";

.title {
    text-decoration: none;
    color: $hsblue;
}

.headlines {
    font-size: 0.9rem;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
}
</style>
