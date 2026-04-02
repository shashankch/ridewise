package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.*;

public class RideService {
    private final DriverService driverService;
    private final RiderService riderService;
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    private final Map<String, Ride> rides = new LinkedHashMap<>();

    public RideService(DriverService driverService, RiderService riderService,
                       RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.riderService = riderService;
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String riderId, double distance) {
        Rider rider = riderService.getRiderById(riderId);
        if (rider == null) throw new IllegalArgumentException("Invalid rider ID");

        Ride ride = new Ride(IdGenerator.generate("RIDE"), rider, distance);
        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No drivers available currently.");
        }

        Driver driver = rideMatchingStrategy.findDriver(rider, availableDrivers);
        if (driver == null) {
            throw new NoDriverAvailableException("No matching driver found.");
        }

        driver.setAvailable(false);
        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);

        double fare = fareStrategy.calculateFare(ride);
        ride.setFareReceipt(new FareReceipt(ride.getId(), fare));
        rides.put(ride.getId(), ride);
        return ride;
    }

    public Ride completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) throw new IllegalArgumentException("Invalid ride ID");
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only assigned rides can be completed.");
        }

        ride.setStatus(RideStatus.COMPLETED);
        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driver.incrementCompletedRides();
        driver.setCurrentLocation(ride.getRider().getLocation());
        return ride;
    }

    public Ride cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) throw new IllegalArgumentException("Invalid ride ID");
        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Completed ride cannot be cancelled.");
        }

        ride.setStatus(RideStatus.CANCELLED);
        if (ride.getDriver() != null) {
            ride.getDriver().setAvailable(true);
        }
        return ride;
    }

    public Collection<Ride> getAllRides() {
        return rides.values();
    }

    public void setRideMatchingStrategy(RideMatchingStrategy rideMatchingStrategy) {
        this.rideMatchingStrategy = rideMatchingStrategy;
    }

    public void setFareStrategy(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }
}
