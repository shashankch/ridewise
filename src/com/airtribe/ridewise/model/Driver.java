package com.airtribe.ridewise.model;

public class Driver {
    private final String id;
    private final String name;
    private Location currentLocation;
    private boolean available;
    private final VehicleType vehicleType;
    private int completedRides;

    public Driver(String id, String name, Location currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
        this.available = true;
        this.completedRides = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public VehicleType getVehicleType() { return vehicleType; }
    public int getCompletedRides() { return completedRides; }
    public void incrementCompletedRides() { this.completedRides++; }

    @Override
    public String toString() {
        return "Driver{id='" + id + "', name='" + name + "', location=" + currentLocation +
                ", available=" + available + ", vehicleType=" + vehicleType +
                ", completedRides=" + completedRides + "}";
    }
}
