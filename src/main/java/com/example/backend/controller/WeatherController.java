package com.example.backend.controller;

import com.example.backend.entity.Weather;
import com.example.backend.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class WeatherController {
    @Autowired
    WeatherRepository weatherRepository;

    /**
     * @api {get} /api/weather/ Request all weather information for all stations
     * @apiName getAllWeatherStatuses
     *
     * @apiSuccess {ResponseEntity<List<Weather>>} list of all weather information
     */
    @GetMapping("/weather")
    public ResponseEntity<List<Weather>> getAllWeatherStatuses() {
        List<Weather> weatherList = new ArrayList<>(weatherRepository.findAll());
        if (weatherList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(weatherList, HttpStatus.OK);
    }

    /**
     * @api {post} /api/addWeather/ Add weather information to the database
     * @apiName addWeatherStatus
     *
     * @apiBody {Weather} weather Weather to be added to the database
     *
     * @apiSuccess {ResponseEntity<Weather>} successfully added weather information
     */
    @PostMapping("/addWeather")
    public ResponseEntity<Weather> addWeatherStatus(@RequestBody Weather weather) {
        try {
            Weather weatherStatus;
            if (weather.getWeatherPhenomenon() != null) {
                weatherStatus = weatherRepository.save(new Weather(weather.getStation(), weather.getWMOCode(), weather.getTemperature(), weather.getWindSpeed(), weather.getWeatherPhenomenon(), weather.getTime()));
            } else {
                weatherStatus = weatherRepository.save(new Weather(weather.getStation(), weather.getWMOCode(), weather.getTemperature(), weather.getWindSpeed(), weather.getTime()));
            }
            return new ResponseEntity<>(weatherStatus, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @api {get} /api/deliveryFee/ Request delivery fee for given station's location and vehicle type
     * @apiName findDeliveryFee
     *
     * @apiParam {String} station Location where delivery is taking place
     * @apiParam {String} vehicle Vehicle used for the delivery
     *
     * @apiSuccess {ResponseEntity<Double>} delivery fee
     */
    @GetMapping("/deliveryFee")
    public ResponseEntity<?> findDeliveryFee(@RequestParam(value="station")String station_name, @RequestParam(value="vehicle")String vehicle_type) {
        try {
            List<Weather> allStationWeathers = weatherRepository.findByStation(station_name); // Finds the latest weather data for given station
            Weather weatherData = allStationWeathers.get(allStationWeathers.size()-1);

            if (weatherData == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            try { // If no exceptions occurred, then fee is returned
                double fee = weatherData.calculateDeliveryFee(vehicle_type);
                return new ResponseEntity<>(fee, HttpStatus.OK);

            } catch (Exception e) { // If exception occurred then exception's message is returned
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
