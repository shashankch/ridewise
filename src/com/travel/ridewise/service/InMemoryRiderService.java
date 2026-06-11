package com.travel.ridewise.service;

import com.travel.ridewise.exception.InvalidInputException;
import com.travel.ridewise.model.Location;
import com.travel.ridewise.model.Rider;
import com.travel.ridewise.util.IdGenerator;
import java.util.*;

public class InMemoryRiderService implements RiderService {
    private final Map<String, Rider> riders = new HashMap<>();

    @Override
    public Rider registerRider(String name, Location location) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Rider name cannot be empty.");
        }
        if (location == null || Double.isNaN(location.getX()) || Double.isNaN(location.getY())) {
            throw new InvalidInputException("Invalid rider location coordinates.");
        }

        Rider rider = new Rider(IdGenerator.generate("RIDER"), name, location);
        riders.put(rider.getId(), rider);
        return rider;
    }

    @Override
    public Rider getRiderById(String id) {
        return riders.get(id);
    }

    @Override
    public Collection<Rider> getAllRiders() {
        return riders.values();
    }
}
