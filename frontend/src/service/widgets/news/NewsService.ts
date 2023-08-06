import type { News } from '@/service/widgets/news/News';
import { useWrappedFetch } from '@/service/principal/FetchService';
import type { Widget } from '@/service/dashboard/Dashboard';

/**
 * Stores the jwt token with every request for the backend.
 */
const { wrappedFetch } = useWrappedFetch();

/**
 * Loads the five news headlines of the given type from the endpoint api/news in the backend.
 * 
 * @param type request for newest or most clicked headlines in the news
 * @retruns promise of news object that contains all headlines about the given type
 */
async function loadNews(type: string): Promise<News> {
  return wrappedFetch(`api/news/${type}?number=5`, "GET")
    .then((response) => {
      if (!response.ok) throw new Error("An error occurred during communication with the server for news.");
      return response.json();
    })
    .then((jsondata) => {
      return jsondata;
    });
}

/**
 * Returns a list of all possible news types.
 * 
 * @returns list of all possible news types as string
 */
function loadNewsTypes(): string[] {
  return ["most", "latest"];
}

/**
 * Get the location from the settings of a news widget and return it.
 * 
 * @param widget news widget containing the settings
 * @returns location of th widget (empty if no location)
 */
export function getLocation(widget: Widget): string {
  return "";
}

/**
 * Updates the location in the news widget settings.
 * 
 * @param widget news widget to update
 * @param location location to update
 * @returns news widget with the changed location in the settings (same object if no location)
 */
export function setLocation(widget: Widget, location: string): Widget {
  return widget;
}

/* ---------------- Export function to use in the components ---------------- */

export function getNews() {
  return {
    loadNews,
    loadNewsTypes,
  };
}
