package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        double baseFare = 50 + (12 * ride.getDistance());
        LocalTime now = LocalTime.now();
        boolean peak = (now.getHour() >= 8 && now.getHour() <= 10) || (now.getHour() >= 17 && now.getHour() <= 20);
        return peak ? baseFare * 1.5 : baseFare;
    }
}
