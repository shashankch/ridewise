package com.travel.ridewise;

import com.travel.ridewise.exception.*;
import com.travel.ridewise.model.*;
import com.travel.ridewise.service.*;
import com.travel.ridewise.strategy.*;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("Running RideWise refactored system test suite...");

        DriverService driverService = new InMemoryDriverService();
        RiderService riderService = new InMemoryRiderService();
        RideService rideService = new RideServiceImpl(
                driverService,
                riderService,
                StrategyRegistry.getMatchingStrategy("NEAREST"),
                StrategyRegistry.getFareStrategy("DEFAULT")
        );

        // 1. Test registration validation
        try {
            riderService.registerRider("", new Location(0, 0));
            throw new AssertionError("Should fail for empty rider name");
        } catch (InvalidInputException e) {
            System.out.println("Pass: Caught invalid rider name exception: " + e.getMessage());
        }

        try {
            driverService.registerDriver("John", new Location(Double.NaN, 0), VehicleType.CAR);
            throw new AssertionError("Should fail for NaN coordinates");
        } catch (InvalidInputException e) {
            System.out.println("Pass: Caught invalid driver coordinate exception: " + e.getMessage());
        }

        // 2. Register Rider and Drivers
        Rider alice = riderService.registerRider("Alice", new Location(0, 0));
        Driver carDriver = driverService.registerDriver("CarDriver", new Location(3, 4), VehicleType.CAR); // Distance = 5
        Driver bikeDriver = driverService.registerDriver("BikeDriver", new Location(1, 1), VehicleType.BIKE); // Distance = 1.414

        System.out.println("Registered Rider: " + alice);
        System.out.println("Registered Car Driver: " + carDriver);
        System.out.println("Registered Bike Driver: " + bikeDriver);

        // 3. Request ride with preferred type CAR
        RideRequest requestCar = new RideRequest(alice.getId(), 10.0, VehicleType.CAR);
        Ride rideCar = rideService.requestRide(requestCar);
        System.out.println("Matched Ride (CAR preference): " + rideCar);
        if (rideCar.getDriver() != carDriver) {
            throw new AssertionError("Should match with CarDriver, but matched with: " + rideCar.getDriver());
        }
        // Base fare = 50 + 12*10 = 170. CAR multiplier = 1.5. Total = 255.0.
        System.out.println("Fare: " + rideCar.getFareReceipt().getAmount());
        if (rideCar.getFareReceipt().getAmount() != 255.0) {
            throw new AssertionError("Expected fare 255.0, got: " + rideCar.getFareReceipt().getAmount());
        }
        System.out.println("Pass: Matched CAR driver and verified pricing multiplier.");

        // 4. Test duplicate active ride request
        try {
            RideRequest requestBike = new RideRequest(alice.getId(), 5.0, VehicleType.BIKE);
            rideService.requestRide(requestBike);
            throw new AssertionError("Should fail since Alice already has an active ride");
        } catch (ActiveRideExistsException e) {
            System.out.println("Pass: Caught active ride exists exception: " + e.getMessage());
        }

        // 5. Complete ride and verify driver released and statistics updated
        rideService.completeRide(rideCar.getId());
        if (!carDriver.isAvailable()) {
            throw new AssertionError("Driver should be available after completion");
        }
        if (carDriver.getCompletedRides() != 1) {
            throw new AssertionError("Completed rides count should be 1");
        }
        if (carDriver.getCurrentLocation().distanceTo(alice.getLocation()) != 0.0) {
            throw new AssertionError("Driver location should be updated to rider's destination");
        }
        System.out.println("Pass: Completed ride and updated driver properties successfully.");

        // 6. Request ride with preferred type BIKE
        RideRequest requestBike = new RideRequest(alice.getId(), 10.0, VehicleType.BIKE);
        Ride rideBike = rideService.requestRide(requestBike);
        System.out.println("Matched Ride (BIKE preference): " + rideBike);
        if (rideBike.getDriver() != bikeDriver) {
            throw new AssertionError("Should match with BikeDriver");
        }
        // Base fare = 50 + 12*10 = 170. BIKE multiplier = 0.8. Total = 136.0.
        if (rideBike.getFareReceipt().getAmount() != 136.0) {
            throw new AssertionError("Expected fare 136.0, got: " + rideBike.getFareReceipt().getAmount());
        }
        System.out.println("Pass: Matched BIKE driver and verified pricing multiplier.");

        // 7. Cancel ride and verify driver released but stats/location not updated
        rideService.cancelRide(rideBike.getId());
        if (!bikeDriver.isAvailable()) {
            throw new AssertionError("Bike driver should be available after cancellation");
        }
        if (bikeDriver.getCompletedRides() != 0) {
            throw new AssertionError("Completed rides count should still be 0");
        }
        System.out.println("Pass: Cancelled ride and verified driver released without side effects.");

        System.out.println("All programmatic test cases passed successfully!");
    }
}
