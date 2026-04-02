package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        return 50 + (12 * ride.getDistance());
    }
}
