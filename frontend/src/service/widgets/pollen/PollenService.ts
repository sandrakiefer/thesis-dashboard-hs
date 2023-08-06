import type { Pollen } from '@/service/widgets/pollen/Pollen';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the pollenforecast for the given types of a given location from the endpoint api/pollen in the backend.
 *
 * @param location location in hessen
 * @param types pollen types from which the pollution is to be returned
 * @returns promise of pollen object that contains all information about the given pollen types at the given location
 */
async function loadPollen(location: string, types: string[]): Promise<Pollen> {
  return wrappedFetch(`api/pollen/${location}/${types}`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for pollen forecast.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all possible pollen types from the endpoint api/pollen in the backend.
 * 
 * @returns promise of a list containing all possible pollen types as string
 */
async function loadPollenTypes(): Promise<string[]> {
  return wrappedFetch("api/pollen/pollentypes", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for pollen forecast.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all possible pollen forecast locations from the endpoint api/pollen in the backend.
 * 
 * @returns promise of a list containing all possible pollen locations as string
 */
async function loadPollenLocations(): Promise<string[]> {
  return wrappedFetch("api/pollen/locations", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for pollen forecast.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the current color for the pollen pollution index.
 * 
 * @param x number of the pollen pollution index
 * @returns color representation as string
 */
function getColor(x: number): string {
  switch (x) {
    case 0:
      return "green";
    case 1:
      return "yellow";
    case 2:
      return "orange";
    case 3:
      return "red";
  }
  return "";
}

/**
 * Get the location from the settings of a pollen widget and return it.
 * 
 * @param widget pollen widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return widget.settings[0];
}

/**
 * Updates the location in the pollen widget settings.
 * 
 * @param widget pollen widget to update
 * @param location location to update
 * @returns pollen widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  widget.settings[0] = location;
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getPollen() {
  return {
    loadPollen,
    loadPollenTypes,
    loadPollenLocations,
    getColor,
  };
}
