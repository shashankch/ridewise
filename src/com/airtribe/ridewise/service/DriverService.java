package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.*;
import java.util.stream.Collectors;

public class DriverService {
    private final Map<String, Driver> drivers = new HashMap<>();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        Driver driver = new Driver(IdGenerator.generate("DRIVER"), name, location, vehicleType);
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        return drivers.get(id);
    }

    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream().filter(Driver::isAvailable).collect(Collectors.toList());
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }

    public Collection<Driver> getAllDrivers() {
        return drivers.values();
    }
}
