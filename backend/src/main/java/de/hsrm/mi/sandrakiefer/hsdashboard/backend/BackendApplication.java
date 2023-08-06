package de.hsrm.mi.sandrakiefer.hsdashboard.backend;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Backend application of the bachelor thesis
 * "Plattform zur interaktiven Erstellung personalisierter Dashboards für die integrierte Visualisierung mehrerer Datenquellen"
 * 
 * @author Sandra Kiefer
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BackendApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
