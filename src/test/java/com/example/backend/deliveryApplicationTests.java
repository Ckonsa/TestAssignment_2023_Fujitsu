package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * There should be different tests to check application's work. I am not familiar enough with testing to create a good testing environment.
 * I am giving my best to give you the thought process of how I would create the tests for the application.
 *
 * At first there should be a separate database for testing, so the data in the actual database would be safe from changes.
 * Testing database should be created before running tests and cleaned after tests are done running.
 * Before running tests, three objects should be inserted to the testing database.
 * {
 *         "station": "Tallinn-Harku",
 *         "temperature": -11.5,
 *         "windSpeed": 14.2,
 *         "weatherPhenomenon": "Overcast",
 *         "time": "2023-03-11 15:15:00",
 *         "wmocode": 26038
 * },
 * {
 *         "station": "Tartu-Tõravere",
 *         "temperature": 2,
 *         "windSpeed": 21.1,
 *         "weatherPhenomenon": "Light snow shower",
 *         "time": "2023-03-11 15:15:00",
 *         "wmocode": 26242
 * },
 * {
 *         "station": "Pärnu",
 *         "temperature": -4.5,
 *         "windSpeed": 9.3,
 *         "weatherPhenomenon": "Glaze",
 *         "time": "2023-03-11 15:15:00",
 *         "wmocode": 41803
 * }
 *
 * Further on I will describe the tests I would create for the application.
 */
@SpringBootTest
class deliveryApplicationTests {

    /**
     * In this test I would check if delivery fee is calculated correctly if no fees besides regional base fee is added.
     * Input: station = "Pärnu", vehicle = "Car"
     * Expected output: 3.0
     */
    @Test
    public void deliveryWithNoAdditionalFees() {}

    /**
     * In this test I would check if delivery fee is calculated correctly if ATEF is added.
     * Input: station = "Tallinn-Harku", vehicle = "Scooter"
     * Expected output: 4.5
     */
    @Test
    public void deliveryWithATEF() {}

    /**
     * In this test I would check if delivery fee is calculated correctly if WSEF is added.
     * Input: station = "Tallinn-Harku", vehicle = "Bike"
     * Expected output: 4.5
     */
    @Test
    public void deliveryWithWSEF() {}

    /**
     * In this test I would check if delivery fee is calculated correctly if WPEF is added.
     * Input: station = "Tartu-Tõravere", vehicle = "Scooter"
     * Expected output: 4.0
     */
    @Test
    public void deliveryWithWPEF() {}

    /**
     * In this test I would check if error message is given when WSEF is calculated.
     * Input: station = "Tartu-Tõravere", vehicle = "Bike"
     * Expected output: “Usage of selected vehicle type is forbidden”
     */
    @Test
    public void errorMessageWithWSEF() {}

    /**
     * In this test I would check if error message is given when WPEF is calculated.
     * Input: station = "Pärnu", vehicle = "Scooter"
     * Expected output: “Usage of selected vehicle type is forbidden”
     */
    @Test
    public void errorMessageWithWPEF() {}


}
