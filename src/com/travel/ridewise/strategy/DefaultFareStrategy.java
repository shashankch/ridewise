package com.travel.ridewise.strategy;

import com.travel.ridewise.model.Ride;
import com.travel.ridewise.model.VehicleType;

public class DefaultFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        double baseFare = 50 + (12 * ride.getDistance());
        double multiplier = 1.0;
        if (ride.getDriver() != null) {
            VehicleType type = ride.getDriver().getVehicleType();
            if (type == VehicleType.BIKE) {
                multiplier = 0.8;
            } else if (type == VehicleType.CAR) {
                multiplier = 1.5;
            }
        }
        return baseFare * multiplier;
    }
}
