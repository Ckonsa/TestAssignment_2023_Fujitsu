package com.example.backend.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "station")
    private String station;
    @Column(name = "WMOCode")
    private int WMOCode;
    @Column(name = "temperature")
    private float temperature;
    @Column(name = "windSpeed")
    private float windSpeed;
    @Column(name = "weatherPhenomenon")
    private String weatherPhenomenon;
    @Column(name = "time")
    // Time is in format yyyy-MM-dd HH:mm:ss
    private String time;
    public Weather() {
    }
    public Weather(String station, int WMOCode, float temperature, float windSpeed, String weatherPhenomenon, String time) {
        // Constructor for weather data with weather phenomenon
        this.station = station;
        this.WMOCode = WMOCode;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.time = time;
    }
    public Weather(String station, int WMOCode, float temperature, float windSpeed, String time) {
        // Constructor for weather data without weather phenomenon
        this.station = station;
        this.WMOCode = WMOCode;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getWMOCode() {
        return WMOCode;
    }

    public void setWMOCode(int WMOCode) {
        this.WMOCode = WMOCode;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Calculates regional base fee according to the weather station location and vehicle
     * @param vehicle_type which is going to be used for delivery
     * @return regional base fee as a double value
     * @throws Exception when invalid vehicle or city is given
     */
    private double calculateRBF(String vehicle_type) throws Exception {
        if (this.station.equals("Tallinn-Harku")) {
            return switch (vehicle_type) {
                case "Car" -> 4.0;
                case "Scooter" -> 3.5;
                case "Bike" -> 3.0;
                default -> throw new Exception("Delivery is unavailable with entered vehicle.");
            };
        } else if (this.station.equals("Tartu-Tõravere")) {
            return switch (vehicle_type) {
                case "Car" -> 3.5;
                case "Scooter" -> 3.0;
                case "Bike" -> 2.5;
                default -> throw new Exception("Delivery is unavailable with entered vehicle.");
            };
        } else if (this.station.equals("Pärnu")) {
            return switch (vehicle_type) {
                case "Car" -> 3.0;
                case "Scooter" -> 2.5;
                case "Bike" -> 2.0;
                default -> throw new Exception("Delivery is unavailable with entered vehicle.");
            };
        } else {
            throw new Exception("Delivery is unavailable in entered city.");
        }
    }

    /**
     * Calculates extra fee based on measured air temperature
     * @return extra fee based on air temperature as a double value
     */
    private double calculateATEF() {
        if (this.temperature < -10.0) {
            return 1.0;
        } else if (0.0 >= this.temperature && this.temperature >= -10.0) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates extra fee based on measured wind speed
     * @return extra fee based on wind speed as a double value
     * @throws Exception when usage of selected vehicle in current weather conditions is forbidden
     */
    private  double calculateWSEF() throws Exception {
        if (this.windSpeed > 20.0) {
            throw new Exception("Usage of selected vehicle type is forbidden.");
        } else if (this.windSpeed <= 20.0 && this.windSpeed >= 10.0) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates extra fee based on given weather phenomenon
     * @return extra fee based on weather phenomenon as a double value
     * @throws Exception when usage of selected vehicle in current weather conditions is forbidden
     */
    private double calculateWPEF() throws Exception {
        if (this.weatherPhenomenon == null) {
            return 0.0; // If weather phenomena is not given, then no extra fee is added.
        }

        String weatherPhenomenon = this.weatherPhenomenon.toLowerCase();
        if (weatherPhenomenon.matches("(.*snow.*)|(.*sleet.*)")) {
            return 1.0;
        } else if (weatherPhenomenon.matches(".*rain.*")) {
            return 0.5;
        } else if (weatherPhenomenon.matches("(.*glaze.*)|(.*hail.*)|(.*thunder.*)")) {
            throw new Exception("Usage of selected vehicle type is forbidden.");
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates delivery fee with given rules
     * @param vehicle_type which is going to be used for delivery
     * @return delivery fee for a delivery in station's location with given vehicle
     * @throws Exception when usage of selected vehicle in current weather conditions is forbidden or invalid vehicle or city is given
     */
    public double calculateDeliveryFee(String vehicle_type) throws Exception {
        double fee = calculateRBF(vehicle_type); // Every vehicle has a regional based fee

        if (vehicle_type.equals("Bike") || vehicle_type.equals("Scooter")) { // For bikes and scooters ATEF and WPEF are calculated and added if needed
            fee += calculateATEF();
            fee += calculateWPEF();
            if (vehicle_type.equals("Bike")) { // Only for bikes WSEF is calculated and added if needed
                fee += calculateWSEF();
            }
        }

        return fee;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", station_name='" + station + '\'' +
                ", WMO_code=" + WMOCode +
                ", temperature=" + temperature +
                ", wind_speed=" + windSpeed +
                ", weather_phenomenon='" + weatherPhenomenon + '\'' +
                ", observation_time=" + time +
                '}';
    }
}
