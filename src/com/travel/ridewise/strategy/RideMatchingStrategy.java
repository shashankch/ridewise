package com.travel.ridewise.strategy;

import com.travel.ridewise.model.Driver;
import com.travel.ridewise.model.Rider;
import com.travel.ridewise.model.VehicleType;
import java.util.List;

public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> drivers, VehicleType preferredType);
}
