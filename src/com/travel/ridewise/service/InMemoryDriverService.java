package com.travel.ridewise.service;

import com.travel.ridewise.exception.DriverNotFoundException;
import com.travel.ridewise.exception.InvalidInputException;
import com.travel.ridewise.model.Driver;
import com.travel.ridewise.model.Location;
import com.travel.ridewise.model.VehicleType;
import com.travel.ridewise.util.IdGenerator;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryDriverService implements DriverService {
    private final Map<String, Driver> drivers = new HashMap<>();

    @Override
    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Driver name cannot be empty.");
        }
        if (location == null || Double.isNaN(location.getX()) || Double.isNaN(location.getY())) {
            throw new InvalidInputException("Invalid driver location coordinates.");
        }
        if (vehicleType == null) {
            throw new InvalidInputException("Vehicle type must be specified.");
        }

        Driver driver = new Driver(IdGenerator.generate("DRIVER"), name, location, vehicleType);
        drivers.put(driver.getId(), driver);
        return driver;
    }

    @Override
    public Driver getDriverById(String id) {
        return drivers.get(id);
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream().filter(Driver::isAvailable).collect(Collectors.toList());
    }

    @Override
    public Collection<Driver> getAllDrivers() {
        return drivers.values();
    }

    @Override
    public void bookDriver(String driverId) {
        Driver driver = getDriverById(driverId);
        if (driver == null) {
            throw new DriverNotFoundException("Driver with ID " + driverId + " not found.");
        }
        driver.setAvailable(false);
    }

    @Override
    public void releaseDriver(String driverId, Location endLocation) {
        Driver driver = getDriverById(driverId);
        if (driver == null) {
            throw new DriverNotFoundException("Driver with ID " + driverId + " not found.");
        }
        if (endLocation == null || Double.isNaN(endLocation.getX()) || Double.isNaN(endLocation.getY())) {
            throw new InvalidInputException("Invalid release location coordinates.");
        }
        driver.setAvailable(true);
        driver.incrementCompletedRides();
        driver.setCurrentLocation(endLocation);
    }

    @Override
    public void cancelDriverBooking(String driverId) {
        Driver driver = getDriverById(driverId);
        if (driver == null) {
            throw new DriverNotFoundException("Driver with ID " + driverId + " not found.");
        }
        driver.setAvailable(true);
    }
}
