<template>
    <div class="WeatherWidget container-fluid" v-if="weather">
        <div class="row justify-content-center align-items-center">
            <div class="col-7">
                <div class="row justify-content-center align-items-center">
                    <div class="col-5 ps-4">
                        <div class="float-start tempBig">
                            {{ Math.round(weather.tempCurrent) }}&deg;
                        </div>
                    </div>
                    <div class="col-7 d-flex justify-content-center">
                        <svg class="weather-icon-big">
                            <title>{{ weather.iconTextCurrent }}</title>
                            <use :href="`${weathericons}#icon${weather.iconCurrent}`" />
                        </svg>
                    </div>
                </div>
                <div class="row text-start ps-3">
                    <div class="col">{{ weather.location }}</div>
                </div>
            </div>
            <div class="col-5 pe-2">
                <div class="float-end smallText">
                    <span class="tempMin">T: {{ Math.round(weather.tempMinToday) }}&deg;</span>
                    <span style="font-size: 0.75rem">&nbsp;</span>
                    <span class="tempMax">H: {{ Math.round(weather.tempMaxToday) }}&deg;</span>
                </div>
                <br />
                <div class="float-end extraSmallText">
                    Regen: {{ weather.rainProbabilityCurrent }} &percnt;
                </div>
                <br>
                <div class="float-end extraSmallText">
                    Wind: {{ Math.round(weather.windSpeedCurrent) }} km/h
                </div>
            </div>
        </div>
        <hr />
        <!-- hourly forecast -->
        <div class="row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center smallText">
                {{ getHourPlusX(index) }}h
            </div>
        </div>
        <div class="row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center">
                <svg class="weather-icon-small">
                    <use :href="`${weathericons}#icon${weather.iconNextSixHours[index - 1]}`" />
                </svg>
            </div>
        </div>
        <div class=" row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center">
                {{ Math.round(weather.tempNextSixHours[index - 1]) }}&deg;
            </div>
        </div>
        <hr />
        <!-- daily forecast -->
        <div class="row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center smallText">
                {{ getDayNamePlusX(index) }}
            </div>
        </div>
        <div class="row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center">
                <svg class="weather-icon-small">
                    <use :href="`${weathericons}#icon${weather.iconNextSixDays[index - 1]}`" />
                </svg>
            </div>
        </div>
        <div class="row justify-content-center">
            <div v-for="index in 6" :key="index" class="col-2 d-flex justify-content-center extraSmallText">
                <p>
                    <span class="tempMin">{{ Math.round(weather.tempMinNextSixDays[index - 1]) }}&deg;</span>
                    <span style="font-size: 0.5rem">&nbsp;</span>
                    <span class="tempMax">{{ Math.round(weather.tempMaxNextSixDays[index - 1]) }}&deg;</span>
                </p>
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
    <div class="modal fade" :id="'weatherWidgetSettings' + widgetId" v-if="weather && locations">
        <div class="modal-dialog">
            <div class="modal-content p-2">
                <div class="modal-header">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-10 settingsHeadline">
                                Wetter - Einstellungen
                            </div>
                            <div class="col-2 text-end">
                                <font-awesome-icon data-bs-dismiss="modal" icon="fa-solid fa-xmark" class="dashClose"
                                    @click="updateWidget()" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body pb-4">
                    <!-- location selection -->
                    <div class="row px-4">
                        <div class="col-12 text-start pb-1">
                            Für welchen Standort soll das Wetter angezeigt werden?
                        </div>
                        <div class="col-12">
                            <LocationSelectSearch :locations="locations" :selected="settingsLocation" :key="widget.pos"
                                @selected-location="settingsLocation = $event" />
                        </div>
                    </div>
                </div>
                <!-- edit data of widgte, only for editors -->
                <div class="modal-footer pb-4 w-100" v-if="isEditor">
                    <div class="row px-3 w-100">
                        <div class="col-12 pb-2">
                            <textarea class="form-control" rows="7" v-model="weatherString" />
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
import { defineComponent, onMounted, ref, watch, toRef } from 'vue';
import type { PropType } from 'vue';
import weathericons from '@/assets/svg/weathericons.svg';
import LocationSelectSearch from '@/components/widgets/LocationSelectSearch.vue';
import { getWeather } from '@/service/widgets/weather/WeatherService';
import type { Weather } from '@/service/widgets/weather/Weather';
import type { Widget } from '@/service/dashboard/Dashboard';
import { useDashboardService_loadInfo } from '@/service/dashboard/DashboardService';

export default defineComponent({
    name: "WeatherWidget",
    components: { LocationSelectSearch },
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
        const settingsLocation = ref<string>(widget.value.settings[0]);
        watch(widget, () => {
            widgetId.value = widget.value.pos;
            settingsLocation.value = widget.value.settings[0];
        });
        /**
         * Load weather information from service
         */
        const { loadWeather, loadWeatherLocations, getHourPlusX, getDayNamePlusX } = getWeather();
        const weather = ref<Weather>();
        const locations = ref<string[]>();
        onMounted(async () => {
            await loadWeatherLocations().then(response => { locations.value = response });
            await update();
        });
        /** 
         * Update weather data on changes
         */
        watch(settingsLocation, async () => {
            await update();
        });
        async function update() {
            await loadWeather(settingsLocation.value).then(response => { weather.value = response });
        }
        function updateWidget() {
            context.emit("update-widget", [settingsLocation.value]);
        }
        /** 
         * Editor temporarily manipulates the data for testing purposes
         */
        const { isEditor } = useDashboardService_loadInfo();
        const weatherString = ref(JSON.stringify(weather.value, null, 2));
        watch(weather, () => {
            weatherString.value = JSON.stringify(weather.value, null, 2);
        });
        async function editWidget() {
            if (isEditor && weatherString.value !== "") weather.value = JSON.parse(weatherString.value);
            context.emit("close-edit");
        }
        /**
         * Return data for visualization in the view
         */
        return {
            widgetId,
            weather,
            locations,
            settingsLocation,
            isEditor,
            weatherString,
            weathericons,
            getHourPlusX,
            getDayNamePlusX,
            updateWidget,
            editWidget,
        };
    },
});
</script>

<style lang="scss">
@import "../../../assets/scss/variables";

.tempMin {
    color: rgb(0, 82, 147);
}

.tempMax {
    color: rgb(204, 26, 20);
}

.tempBig {
    font-size: 2.7rem;
}

.weather-icon-big {
    height: 4rem;
    padding-left: 1rem;
    padding-top: 0.2rem;
}

.weather-icon-small {
    height: 3rem;
}
</style>
