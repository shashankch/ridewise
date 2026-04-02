# RideWise - Modular Ride-Sharing (Uber/Ola style) System (Java LLD)

RideWise is a console-based ride-sharing system built to demonstrate **LLD**, **OOP**, **SOLID**, and **Strategy Pattern** using **composition over inheritance**.

## Features

- Register riders
- Register drivers
- View available drivers
- Request ride
- Driver assignment via pluggable matching strategy
- Fare calculation via pluggable pricing strategy
- Complete / cancel ride
- Track ride lifecycle:
  - REQUESTED
  - ASSIGNED
  - COMPLETED
  - CANCELLED

## Design Highlights

- **SRP**: Dedicated services for Rider, Driver, and Ride operations
- **OCP**: Add new pricing or matching strategies without changing service logic
- **DIP**: `RideService` depends on `RideMatchingStrategy` and `FareStrategy` abstractions
- **Low Coupling**: UI only talks to service layer
- **High Cohesion**: Each package owns one responsibility
- **DRY**: No duplication in ride allocation logic
- **KISS**: simple entity modeling
- **YAGNI**: MVP before features explosion
- **Law of Demeter**: Services communicate with collaborators directly (no deep method chains)

## Package Structure

```text
src/com/airtribe/ridewise/
├── Main.java
├── model/
├── strategy/
├── service/
├── exception/
└── util/
```

## How to Compile & Run

From project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.airtribe.ridewise.Main
```

## Sample Flow

1. Add Rider
2. Add Driver
3. Configure strategy
4. Request Ride
5. Complete Ride
6. View all rides

## Extending the System

### Add new ride matching strategy

Implement:

```java
public class HighestRatedDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        return null;
    }
}
```

### Add new fare strategy

Implement:

```java
public class SurgeFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        return 0;
    }
}
```

Then inject into `RideService`.

## Assumptions

- In-memory storage only
- Rider location acts as pickup location
- Distance is provided manually in MVP
- Vehicle type is stored for extensibility, not filtered in MVP

## Documentation

- [Class Model](docs/Class_Model.md)
- [Object Relationships](docs/Object_Relationships.md)
- [Requirements](docs/Requirements.md)
- [Solid Reflections](docs/SOLID_Reflection.md)
