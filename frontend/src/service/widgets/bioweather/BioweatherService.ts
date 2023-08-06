import type { Bioweather } from '@/service/widgets/bioweather/Bioweather';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the bio weather of given categories from the endpoint api/bioweather in the backend.
 * 
 * @param categories possible categories from which the bioweather is to be returned
 * @returns promise of bioweather object that contains all information about the given categories
 */
async function loadBioweather(categories: string[]): Promise<Bioweather> {
  return wrappedFetch(`api/bioweather/${categories}`, "GET")
    .then(response => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the bio weather.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all possible bioweather categories from the endpoint api/bioweather in the backend.
 * 
 * @returns promise of a list containing all possible bioweather categories as string
 */
async function loadBioweatherCategories(): Promise<string[]> {
  return wrappedFetch("api/bioweather/categories", "GET")
    .then(response => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the bio weather.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the location from the settings of a bio weather widget and return it.
 * 
 * @param widget bio weather widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return "";
}

/**
 * Updates the location in the bio weather widget settings.
 * 
 * @param widget bio weather widget to update
 * @param location location to update
 * @returns bio weather widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getBioweather() {
  return {
    loadBioweather,
    loadBioweatherCategories,
  };
}
