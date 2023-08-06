import type { Corona } from '@/service/widgets/corona/Corona';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the corona cases of a given location from the endpoint api/corona in the backend.
 *
 * @param location location in hessen
 * @returns promise of corona object that contains all information about the given location
 */
async function loadCorona(location: string): Promise<Corona> {
  return wrappedFetch(`api/corona/${location}`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for corona.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all possible locations from the endpoint api/corona in the backend.
 * 
 * @returns promise of a list containing all possible locations for corona as string
 */
async function loadCoronaLocations(): Promise<string[]> {
  return wrappedFetch("api/corona/locations", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for corona.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the location from the settings of a corona widget and return it.
 * 
 * @param widget corona widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return widget.settings[0];
}

/**
 * Updates the location in the corona widget settings.
 * 
 * @param widget corona widget to update
 * @param location location to update
 * @returns corona widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  widget.settings[0] = location;
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getCorona() {
  return {
    loadCorona,
    loadCoronaLocations,
  };
}
