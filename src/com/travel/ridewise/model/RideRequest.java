package com.travel.ridewise.model;

public class RideRequest {
    private final String riderId;
    private final double distance;
    private final VehicleType preferredVehicleType;

    public RideRequest(String riderId, double distance, VehicleType preferredVehicleType) {
        this.riderId = riderId;
        this.distance = distance;
        this.preferredVehicleType = preferredVehicleType;
    }

    public String getRiderId() {
        return riderId;
    }

    public double getDistance() {
        return distance;
    }

    public VehicleType getPreferredVehicleType() {
        return preferredVehicleType;
    }
}
