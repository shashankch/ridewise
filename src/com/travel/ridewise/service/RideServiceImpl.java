package com.travel.ridewise.service;

import com.travel.ridewise.exception.*;
import com.travel.ridewise.model.*;
import com.travel.ridewise.strategy.*;
import com.travel.ridewise.util.IdGenerator;
import java.util.*;

public class RideServiceImpl implements RideService {
    private final DriverService driverService;
    private final RiderService riderService;
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    private final Map<String, Ride> rides = new LinkedHashMap<>();

    public RideServiceImpl(DriverService driverService, RiderService riderService,
                           RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.riderService = riderService;
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    @Override
    public Ride requestRide(RideRequest request) {
        if (request == null) {
            throw new InvalidInputException("Ride request cannot be null.");
        }
        if (request.getDistance() <= 0) {
            throw new InvalidInputException("Ride distance must be greater than zero.");
        }

        Rider rider = riderService.getRiderById(request.getRiderId());
        if (rider == null) {
            throw new RiderNotFoundException("Rider with ID " + request.getRiderId() + " not found.");
        }

        if (hasActiveRide(rider.getId())) {
            throw new ActiveRideExistsException("Rider already has an active ride.");
        }

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No drivers available currently.");
        }

        Driver driver = rideMatchingStrategy.findDriver(rider, availableDrivers, request.getPreferredVehicleType());
        if (driver == null) {
            throw new NoDriverAvailableException("No available drivers match your request criteria.");
        }

        // Book the driver (marks driver unavailable)
        driverService.bookDriver(driver.getId());

        Ride ride = new Ride(IdGenerator.generate("RIDE"), rider, request.getDistance());
        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);

        double fare = fareStrategy.calculateFare(ride);
        ride.setFareReceipt(new FareReceipt(ride.getId(), fare));

        rides.put(ride.getId(), ride);
        return ride;
    }

    @Override
    public Ride completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new RideNotFoundException("Ride with ID " + rideId + " not found.");
        }
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException("Only assigned rides can be completed. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);
        
        // Release driver (make available, increment rides, set new location)
        driverService.releaseDriver(ride.getDriver().getId(), ride.getRider().getLocation());
        
        return ride;
    }

    @Override
    public Ride cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new RideNotFoundException("Ride with ID " + rideId + " not found.");
        }
        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidRideStateException("Completed rides cannot be cancelled.");
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStateException("Ride is already cancelled.");
        }

        ride.setStatus(RideStatus.CANCELLED);
        if (ride.getDriver() != null) {
            driverService.cancelDriverBooking(ride.getDriver().getId());
        }
        return ride;
    }

    @Override
    public Collection<Ride> getAllRides() {
        return rides.values();
    }

    @Override
    public void setRideMatchingStrategy(String strategyName) {
        this.rideMatchingStrategy = StrategyRegistry.getMatchingStrategy(strategyName);
    }

    @Override
    public void setFareStrategy(String strategyName) {
        this.fareStrategy = StrategyRegistry.getFareStrategy(strategyName);
    }

    private boolean hasActiveRide(String riderId) {
        return rides.values().stream()
                .anyMatch(r -> r.getRider().getId().equals(riderId) &&
                        (r.getStatus() == RideStatus.REQUESTED || r.getStatus() == RideStatus.ASSIGNED));
    }
}
