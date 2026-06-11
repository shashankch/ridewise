package com.travel.ridewise.service;

import com.travel.ridewise.model.Location;
import com.travel.ridewise.model.Rider;
import java.util.Collection;

public interface RiderService {
    Rider registerRider(String name, Location location);
    Rider getRiderById(String id);
    Collection<Rider> getAllRiders();
}
