package com.travel.ridewise.service;

import com.travel.ridewise.model.Ride;
import com.travel.ridewise.model.RideRequest;
import java.util.Collection;

public interface RideService {
    Ride requestRide(RideRequest request);
    Ride completeRide(String rideId);
    Ride cancelRide(String rideId);
    Collection<Ride> getAllRides();
    void setRideMatchingStrategy(String strategyName);
    void setFareStrategy(String strategyName);
}
