import type { TrafficStreet } from '@/service/widgets/traffic/street/TrafficStreet';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the traffic informations of the given streets from the endpoint api/traffic/streets in the backend.
 *
 * @param streets list of streetnames
 * @returns promise of traffic street object that contains all traffic information about the given streets
 */
async function loadTrafficStreet(streets: string[]): Promise<TrafficStreet> {
  return wrappedFetch(`api/traffic/streets/${streets}`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the traffic on the streets.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads the traffic informations of the given streets from the endpoint api/traffic/streets in the backend.
 * 
 * @retruns promise of a list of strings containing all possible streets of type autobahn
 */
async function loadTrafficStreetA(): Promise<string[]> {
  return wrappedFetch("api/traffic/streets/A", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the traffic on the streets.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads the traffic informations of the given streets from the endpoint api/traffic/streets in the backend.
 * 
 * @returns promise of a list of strings containing all possible streets of type bundesstrasse
 */
async function loadTrafficStreetB(): Promise<string[]> {
  return wrappedFetch("api/traffic/streets/B", "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for the traffic on the streets.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the location from the settings of a traffic street widget and return it.
 * 
 * @param widget traffic street widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return "";
}

/**
 * Updates the location in the traffic street widget settings.
 * 
 * @param widget traffic street widget to update
 * @param location location to update
 * @returns traffic street widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getTrafficStreet() {
  return {
    loadTrafficStreet,
    loadTrafficStreetA,
    loadTrafficStreetB,
  };
}
