package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.Comparator;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        return drivers.stream()
                .min(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(rider.getLocation())))
                .orElse(null);
    }
}
