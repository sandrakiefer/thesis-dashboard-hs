package de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.FetchJsonData;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.tools.exceptions.ApiNotAvailableException;
import de.hsrm.mi.sandrakiefer.hsdashboard.backend.data.weather.exceptions.NoWeatherForecastForThisLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    
    @Value("#{${data.locations}}")
    private Map<String,String> locations;

    @Autowired
    private FetchJsonData fetchJsonData;
    
    private Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value="weather")
    public Weather getWeatherFromLocation(String location) throws NoWeatherForecastForThisLocationException, ApiNotAvailableException {
        if (!locations.containsKey(location)) {
            String msg = String.format("No Weather Forecast for location '%s' available", location);
            logger.error(msg);
            throw new NoWeatherForecastForThisLocationException(msg);
        }
        String urlHourly = String.format("https://api.prod.wetterdaten.hr.de/api/v1/vorhersagen/3tage_1h_deutschland/%s", locations.get(location));
        String urlToday = String.format("https://api.prod.wetterdaten.hr.de/api/v1/vorhersagen/heute_24h_deutschland/%s", locations.get(location));
        String urlDaily = String.format("https://api.prod.wetterdaten.hr.de/api/v1/vorhersagen/10tage_24h_deutschland/%s", locations.get(location));
        int time = LocalDateTime.now().getHour();
        int day = 0;
        Weather w = new Weather();
        JsonNode nodeHourly = fetchJsonData.fetchDataFromUrl(urlHourly).get(0);
        w.setLocation(location);
        if (time == 23) {
            w.setTempCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/luftTemperaturMinimum/wert", time, 0)).asDouble());
            w.setRainProbabilityCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/niederschlagsWahrscheinlichkeit/wert", time, 0)).asDouble());
            w.setHumidityCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/luftfeuchtigkeitMaximum/wert", time, 0)).asDouble());
            w.setWindSpeedCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/mittlereWindGeschwindigkeit/wert", time, 0)).asDouble());
            w.setIconCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/wettersymbol/wert", time, 0)).asInt());
            w.setIconTextCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/wettersymbol/text", time, 0)).textValue());
        } else {
            w.setTempCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/luftTemperaturMinimum/wert", time, time + 1)).asDouble());
            w.setRainProbabilityCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/niederschlagsWahrscheinlichkeit/wert", time, time + 1)).asDouble());
            w.setHumidityCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/luftfeuchtigkeitMaximum/wert", time, time + 1)).asDouble());
            w.setWindSpeedCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/mittlereWindGeschwindigkeit/wert", time, time + 1)).asDouble());
            w.setIconCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/wettersymbol/wert", time, time + 1)).asInt());
            w.setIconTextCurrent(nodeHourly.at(String.format("/tag00/%02d:00-%02d:00/wettersymbol/text", time, time + 1)).textValue());
        }
        List<Double> tempNextSixHours = new ArrayList<Double>();
        List<Integer> iconNextSixHours = new ArrayList<Integer>();
        for (int i = 1; i <= 6; i++) {
            time++;
            if (time > 23) {
                day++;
                time = 0;
            }
            if (time == 23) {
                tempNextSixHours.add(nodeHourly.at(String.format("/tag%02d/%02d:00-%02d:00/luftTemperaturMinimum/wert", day, time, 0)).asDouble());
                iconNextSixHours.add(nodeHourly.at(String.format("/tag%02d/%02d:00-%02d:00/wettersymbol/wert", day, time, 0)).asInt());
            } else {
                tempNextSixHours.add(nodeHourly.at(String.format("/tag%02d/%02d:00-%02d:00/luftTemperaturMinimum/wert", day, time, time + 1)).asDouble());
                iconNextSixHours.add(nodeHourly.at(String.format("/tag%02d/%02d:00-%02d:00/wettersymbol/wert", day, time, time + 1)).asInt());
            }
        }
        w.setTempNextSixHours(tempNextSixHours);
        w.setIconNextSixHours(iconNextSixHours);
        JsonNode nodeToday = fetchJsonData.fetchDataFromUrl(urlToday).get(0);
        w.setTempMinToday(nodeToday.at("/tag00/00:00-00:00/luftTemperaturMinimum/wert").asDouble());
        w.setTempMaxToday(nodeToday.at("/tag00/00:00-00:00/luftTemperaturMaximum/wert").asDouble());
        JsonNode nodeDaily = fetchJsonData.fetchDataFromUrl(urlDaily).get(0);
        List<Double> tempMinNextSixDays = new ArrayList<Double>();
        List<Double> tempMaxNextSixDays = new ArrayList<Double>();
        List<Integer> iconNextSixDays = new ArrayList<Integer>();
        for (int i = 1; i <= 6; i++) {
            tempMinNextSixDays.add(nodeDaily.at(String.format("/tag%02d/00:00-00:00/luftTemperaturMinimum/wert", i)).asDouble());
            tempMaxNextSixDays.add(nodeDaily.at(String.format("/tag%02d/00:00-00:00/luftTemperaturMaximum/wert", i)).asDouble());
            iconNextSixDays.add(nodeDaily.at(String.format("/tag%02d/00:00-00:00/wettersymbol/wert", i)).asInt());
        }
        w.setTempMinNextSixDays(tempMinNextSixDays);
        w.setTempMaxNextSixDays(tempMaxNextSixDays);
        w.setIconNextSixDays(iconNextSixDays);
        return w;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllWeatherLocations() {
        return new ArrayList<String>(locations.keySet());
    }
    
}
