package com.travel.ridewise.exception;

public class ActiveRideExistsException extends RideWiseException {
    public ActiveRideExistsException(String message) {
        super(message);
    }
}
