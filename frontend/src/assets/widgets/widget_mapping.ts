export const widget_mapping = [
    { name: "info", component: "info/InfoWidget", service: "", defaultSettings: [] },
    { name: "weather", component: "weather/WeatherWidget", service: "weather/WeatherService", defaultSettings: ["Frankfurt am Main"] },
    { name: "pollen", component: "pollen/PollenWidget", service: "pollen/PollenService", defaultSettings: ["Frankfurt am Main", "Hasel", "Erle", "Esche", "Birke", "Gräser", "Roggen", "Beifuß", "Ambrosia"] },
    { name: "bioweather", component: "bioweather/BioweatherWidget", service: "bioweather/BioweatherService", defaultSettings: ["Allgemeine Befinden"] },
    { name: "corona", component: "corona/CoronaWidget", service: "corona/CoronaService", defaultSettings: ["Frankfurt am Main"] },
    { name: "news", component: "news/NewsWidget", service: "news/NewsService", defaultSettings: ["most"] },
    { name: "trafficStreet", component: "traffic/street/TrafficStreetWidget", service: "traffic/street/TrafficStreetService", defaultSettings: ["A3", "A4", "A5", "A7", "A44", "A45", "A66", "A67"] },
    { name: "sport", component: "sport/SportWidget", service: "sport/SportService", defaultSettings: ["bl1", "Frankfurt"] },
];
