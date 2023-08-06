import type { Weather } from '@/service/widgets/weather/Weather';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the weather forecast of a given location from the endpoint api/weather in the backend.
 *
 * @param location location in hessen
 * @returns promise of weather object that contains all information about the given location
 */
async function loadWeather(location: string): Promise<Weather> {
  return wrappedFetch(`api/weather/${location}`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the weather forecast.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all possible weather forecast locations from the endpoint api/weather in the backend.
 * 
 * @returns promise of a list containing all possible locations for weather as string
 */
async function loadWeatherLocations(): Promise<string[]> {
  return wrappedFetch("api/weather/locations", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the weather forecast.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the current hour as a number, plus x hours.
 * (needed for hourly weather forecast)
 * 
 * @param x how many hours should be added to the current time
 * @returns cuurent time as hour with x hours
 */
function getHourPlusX(x: number): number {
  const d = new Date();
  d.setTime(d.getTime() + x * 60 * 60 * 1000);
  return d.getHours();
}

/**
 * Get the current day name as a string, plus x days.
 * (needed for daily weather forecast)
 * 
 * @param x how many days should be added to the current date
 * @returns cuurent date as day with x days
 */
function getDayNamePlusX(x: number): string {
  const d = new Date();
  d.setDate(d.getDate() + x);
  return d.toLocaleDateString("de-DE", { weekday: "short" });
}

/**
 * Get the location from the settings of a weather widget and return it.
 * 
 * @param widget weather widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return widget.settings[0];
}

/**
 * Updates the location in the weather widget settings.
 * 
 * @param widget weather widget to update
 * @param location location to update
 * @returns weather widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  widget.settings[0] = location;
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getWeather() {
  return {
    loadWeather,
    loadWeatherLocations,
    getHourPlusX,
    getDayNamePlusX,
  };
}
