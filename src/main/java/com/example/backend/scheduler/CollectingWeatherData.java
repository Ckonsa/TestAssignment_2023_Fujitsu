package com.example.backend.scheduler;


import com.example.backend.entity.Weather;
import com.example.backend.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CollectingWeatherData {

    @Autowired
    private WeatherRepository weatherRepository;
    private final String dataURL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    /**
     * Collect data from URL about weather stations Tallinn-Harku, Tartu-Tõravere and Pärnu every hour 15 minutes after a full hour.
     * First reads from URL the information and then sends POST request to the API to add weather information to the database.
     * @throws Exception when something goes wrong with reading data
     */
    @Scheduled(cron = "0 15 * * * ?")
    public void collectData() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        URLConnection con = new URL(this.dataURL).openConnection();
        Document doc = db.parse(con.getInputStream());

        Element e = doc.getDocumentElement();
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("station"); // Gets all stations

        for (int i = 0; i < nodeList.getLength(); i++) { // Goes through all the station nodes

            Node station = nodeList.item(i);
            if (station.getNodeType() == Node.ELEMENT_NODE) {
                Element stationElement = (Element) station;
                NodeList nodelist = stationElement.getElementsByTagName("name"); // Gets station's name
                Element element = (Element) nodelist.item(0);
                String attributeValue = element.getChildNodes().item(0).getNodeValue();

                // If station is one of the ones we need the information on
                if (attributeValue.equals("Tallinn-Harku") || attributeValue.equals("Tartu-Tõravere") || attributeValue.equals("Pärnu")) {
                    Weather weatherData = new Weather();
                    weatherData.setStation(attributeValue);
                    String[] attributes = {"wmocode", "phenomenon", "airtemperature","windspeed"}; // Goes through needed attributes

                    for (String attribute: attributes) {
                        nodelist = stationElement.getElementsByTagName(attribute);
                        element = (Element) nodelist.item(0);
                        if (element.getChildNodes().item(0) != null) { // Checks if the attribute has a value
                            attributeValue = element.getChildNodes().item(0).getNodeValue();

                            switch (attribute) { // Adds attribute to the weather data
                                case "wmocode" -> weatherData.setWMOCode(Integer.parseInt(attributeValue));
                                case "phenomenon" -> weatherData.setWeatherPhenomenon(attributeValue);
                                case "airtemperature" -> weatherData.setTemperature(Float.parseFloat(attributeValue));
                                case "windspeed" -> weatherData.setWindSpeed(Float.parseFloat(attributeValue));
                            }
                        }
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Takes time of the information reading
                    weatherData.setTime(LocalDateTime.now().format(formatter));

                    weatherRepository.save(weatherData); // Saves the weather data to the database
                }
            }
        }
    }
}
