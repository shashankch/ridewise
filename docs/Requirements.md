# Requirements

This document outlines the functional and non-functional requirements of the RideWise system.

## Functional Requirements

- **Register Riders**: Capture name and coordinate location (X, Y). Validates names are non-empty and coordinates are valid.
- **Register Drivers**: Capture name, starting coordinate location, and vehicle type (BIKE, AUTO, CAR).
- **Show Available Drivers**: Display drivers who are currently set to available.
- **Request Ride**: Book a ride for a registered rider:
  - Supports entering trip distance (km) and a preferred vehicle type.
  - Checks if the rider exists and is not currently in an active ride.
  - Validates that the distance is positive (> 0).
- **Match Ride Using Strategy**: Plug in a strategy (`Nearest` or `Least Active`) to assign the driver.
  - Automatically filters drivers by the rider's preferred vehicle type.
- **Calculate Fare**: Dynamically calculate pricing using strategies:
  - Multiplies pricing based on vehicle type (`BIKE` = 0.8x, `AUTO` = 1.0x, `CAR` = 1.5x).
  - Peak-hour pricing calculates 1.5x surge if booked during peak hours.
- **Complete Ride**: Completes an assigned ride, makes the driver available again, increments their completed rides, and updates their location to the rider's location.
- **Cancel Ride**: Cancels an assigned ride, frees the driver, but does not alter completed rides or driver location.
- **View All Rides**: Display log of booked rides and statuses.

## Non-Functional Requirements

- **Dependency Inversion (DIP)**: Core services depend on interface abstractions.
- **Single Responsibility (SRP)**: Clean separation between rider, driver, and ride state mutations.
- **Robust Exception Handling**: Custom business exception class hierarchy catches user errors gracefully rather than crashing.
- **Extendable Strategies**: Dynamic registry allows registering new matching/fare strategies without modifying existing services.
