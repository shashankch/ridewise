package com.travel.ridewise.strategy;

import com.travel.ridewise.model.Ride;
import com.travel.ridewise.model.VehicleType;
import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {
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
        double fare = baseFare * multiplier;
        LocalTime now = LocalTime.now();
        boolean peak = (now.getHour() >= 8 && now.getHour() <= 10) || (now.getHour() >= 17 && now.getHour() <= 20);
        return peak ? fare * 1.5 : fare;
    }
}
