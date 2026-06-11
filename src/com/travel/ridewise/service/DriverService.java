package com.travel.ridewise.service;

import com.travel.ridewise.model.Driver;
import com.travel.ridewise.model.Location;
import com.travel.ridewise.model.VehicleType;
import java.util.Collection;
import java.util.List;

public interface DriverService {
    Driver registerDriver(String name, Location location, VehicleType vehicleType);
    Driver getDriverById(String id);
    List<Driver> getAvailableDrivers();
    Collection<Driver> getAllDrivers();
    void bookDriver(String driverId);
    void releaseDriver(String driverId, Location endLocation);
    void cancelDriverBooking(String driverId);
}
