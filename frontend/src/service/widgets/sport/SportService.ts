import type { Sport } from '@/service/widgets/sport/Sport';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the current league informations of a given team of the league from the endpoint api/sport in the backend.
 *
 * @param league shortcut of the league on openligadb
 * @param team name of the team
 * @returns promise of sport object that contains all information about the given team and league
 */
async function loadSport(league: string, team: string): Promise<Sport> {
  return wrappedFetch(`api/sport/${league}/${team}`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for sport.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Loads a list of all teams of the given league from the endpoint api/sport in the backend.
 *
 * @param league shortcut of the league on openligadb
 * @returns promise of a list containing all possible teams of a league as string
 */
async function loadSportTeams(league: string): Promise<string[]> {
  return wrappedFetch(`api/sport/${league}/teams`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for sport.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Get the shortcut of the current day of the week from the date.
 * 
 * @param date current date
 * @returns first two letters of the day of the week from the date
 */
function getDayName(date: string): string {
  return new Date(date).toLocaleDateString("de-DE", { weekday: "short" });
}

/**
 * Get the formatted number date only containing day and month.
 * 
 * @param date current date
 * @returns date containing day and month in number format
 */
function getShortDate(date: string): string {
  const d = new Date(date);
  return (((d.getDate() > 9) ? d.getDate() : ('0' + d.getDate())) + '.' + ((d.getMonth() > 8) ? (d.getMonth() + 1) : ('0' + (d.getMonth() + 1))) + '.');
}

/**
 * Checks if the game is currently running.
 * 
 * @param date date with time of the game
 * @returns true if the game is currently running, false if not
 */
function checkIfCurrentPlaying(date: string): boolean {
  const from = new Date(date);
  const until = new Date(from.getTime() + (120 * 60000))
  const c = new Date();
  if (c > from && c < until) {
    return true;
  } else {
    return false;
  }
}

/**
 * Get the location from the settings of a sport widget and return it.
 * 
 * @param widget sport widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return "";
}

/**
 * Updates the location in the sport widget settings.
 * 
 * @param widget sport widget to update
 * @param location location to update
 * @returns sport widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getSport() {
  return {
    loadSport,
    loadSportTeams,
    getDayName,
    getShortDate,
    checkIfCurrentPlaying,
  };
}
