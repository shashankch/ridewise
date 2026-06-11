package com.travel.ridewise.strategy;

import com.travel.ridewise.model.Ride;

public interface FareStrategy {
    double calculateFare(Ride ride);
}
