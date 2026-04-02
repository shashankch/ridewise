package com.airtribe.ridewise;

import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.service.*;
import com.airtribe.ridewise.strategy.*;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        RideService rideService = new RideService(
                driverService,
                riderService,
                new NearestDriverStrategy(),
                new DefaultFareStrategy()
        );

        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> addRider(riderService);
                    case 2 -> addDriver(driverService);
                    case 3 -> viewAvailableDrivers(driverService);
                    case 4 -> requestRide(rideService, riderService);
                    case 5 -> completeRide(rideService);
                    case 6 -> viewRides(rideService);
                    case 7 -> configureStrategies(rideService);
                    case 8 -> running = false;
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Exiting RideWise. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("==== RideWise Menu ====");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Configure Strategies");
        System.out.println("8. Exit");
        System.out.print("Choose option: ");
    }

    private static void addRider(RiderService riderService) {
        System.out.print("Enter rider name: ");
        String name = scanner.nextLine();
        Location location = readLocation();
        Rider rider = riderService.registerRider(name, location);
        System.out.println("Rider added: " + rider);
    }

    private static void addDriver(DriverService driverService) {
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();
        Location location = readLocation();
        VehicleType vehicleType = readVehicleType();
        Driver driver = driverService.registerDriver(name, location, vehicleType);
        System.out.println("Driver added: " + driver);
    }

    private static void viewAvailableDrivers(DriverService driverService) {
        System.out.println("Available Drivers:");
        driverService.getAvailableDrivers().forEach(System.out::println);
    }

    private static void requestRide(RideService rideService, RiderService riderService) {
        System.out.println("Registered Riders:");
        riderService.getAllRiders().forEach(System.out::println);
        System.out.print("Enter rider ID: ");
        String riderId = scanner.nextLine();
        System.out.print("Enter ride distance (km): ");
        double distance = Double.parseDouble(scanner.nextLine());
        Ride ride = rideService.requestRide(riderId, distance);
        System.out.println("Ride booked successfully!");
        System.out.println(ride);
        System.out.println(ride.getFareReceipt());
    }

    private static void completeRide(RideService rideService) {
        System.out.print("Enter ride ID to complete: ");
        String rideId = scanner.nextLine();
        Ride ride = rideService.completeRide(rideId);
        System.out.println("Ride completed: " + ride);
    }

    private static void viewRides(RideService rideService) {
        System.out.println("All rides:");
        rideService.getAllRides().forEach(System.out::println);
    }

    private static void configureStrategies(RideService rideService) {
        System.out.println("Select Ride Matching Strategy: 1. Nearest  2. Least Active");
        int matchChoice = Integer.parseInt(scanner.nextLine());
        rideService.setRideMatchingStrategy(matchChoice == 2 ? new LeastActiveDriverStrategy() : new NearestDriverStrategy());

        System.out.println("Select Fare Strategy: 1. Default  2. Peak Hour");
        int fareChoice = Integer.parseInt(scanner.nextLine());
        rideService.setFareStrategy(fareChoice == 2 ? new PeakHourFareStrategy() : new DefaultFareStrategy());

        System.out.println("Strategies updated successfully.");
    }

    private static Location readLocation() {
        System.out.print("Enter X coordinate: ");
        double x = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Y coordinate: ");
        double y = Double.parseDouble(scanner.nextLine());
        return new Location(x, y);
    }

    private static VehicleType readVehicleType() {
        System.out.println("Select Vehicle Type: 1. BIKE  2. AUTO  3. CAR");
        int choice = Integer.parseInt(scanner.nextLine());
        return switch (choice) {
            case 1 -> VehicleType.BIKE;
            case 2 -> VehicleType.AUTO;
            case 3 -> VehicleType.CAR;
            default -> throw new IllegalArgumentException("Invalid vehicle type");
        };
    }
}
