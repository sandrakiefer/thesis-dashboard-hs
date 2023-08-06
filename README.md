# Frontend hs-Dashboard

Hochschule RheinMain - Medieninformatik - Bachelorarbeit (September 2022) von Sandra Kiefer

"Plattform zur interaktiven Erstellung personalisierter Dashboards für die integrierte Visualisierung mehrerer Datenquellen"

Referent "Prof. Dr. Wolfgang Weitz" – Korreferent "Prof. Dr. Jörg Berdux" – Externer Betreuer "John-Patrick Rott (M.Sc.) vom Hessischen Rundfunk"

Programmcode zur schriftlichen Ausarbeitung der Bachelorarbeit

![Beispielbild eines personalisierten Dashboards](/frontend/src/assets/readme/beispiel.png)

## Kurzbeschreibung der Bachelorarbeit

Ziel der Bachelorarbeit ist es eine Plattform zur interaktiven Erstellung persoalisierter Dashboards für die integrierte Viesualisierung mehrerer Datenquellen zu design und zu implementieren. Dies wird anhand des Beispiels für die Webseite [hessenschau.de](https://www.hessenschau.de/index.html) gezeigt. Bei den zu visualisierenden Datenquellen handelt es sich um verschiedenen Wetterdaten, Coronazahlen, Sportdaten, Verkehrsdaten und Nachrichten. Diese werden kompakt und übersichtlich indivduell anpassbar im Dashboard angezeigt. Die Nutzer haben dabei die Möglichkeit ihre verschiedenen Dashboards nach ihren Bedürfnissen anzupassen. 

## Installations- und Startanweisungen

Die Anwendung kann über die Datei 'dashboard.jar' mit folgendem Befehl gestartet werden

```bash
  java -jar dashboard.jar
```

Die Webandwendung kann anschließenden mit dem Browser über den Pfad [localhost:9090](http://localhost:9090) erreicht werden.

## Nutzer zum Testen der Anwendung

| E-Mail | Passwort      | Rolle               |
| :-------- | :------- | :------------------------- |
| `ff@test.de` | `FFtestPW!?17` | Nutzer |
| `mm@test.de` | `MMtestPW!?17` | Admin/Redakteur |
