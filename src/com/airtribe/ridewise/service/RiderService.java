package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.*;

public class RiderService {
    private final Map<String, Rider> riders = new HashMap<>();

    public Rider registerRider(String name, Location location) {
        Rider rider = new Rider(IdGenerator.generate("RIDER"), name, location);
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider getRiderById(String id) {
        return riders.get(id);
    }

    public Collection<Rider> getAllRiders() {
        return riders.values();
    }
}
