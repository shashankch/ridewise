package com.travel.ridewise.strategy;

import com.travel.ridewise.model.Driver;
import com.travel.ridewise.model.Rider;
import com.travel.ridewise.model.VehicleType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers, VehicleType preferredType) {
        List<Driver> filtered = drivers.stream()
                .filter(d -> preferredType == null || d.getVehicleType() == preferredType)
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return null;
        }
        return filtered.stream()
                .min(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(rider.getLocation())))
                .orElse(null);
    }
}
