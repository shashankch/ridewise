# SOLID Reflections

This document analyzes how the RideWise architecture applies and enforces SOLID object-oriented design principles.

---

## 1. Single Responsibility Principle (SRP)
> *A class should have one, and only one, reason to change.*

### Prior Violations
Previously, `RideService` modified the state of `Driver` models directly (e.g. marking availability, updating locations, and incrementing rides). This mixed ride workflow logic with driver lifecycle management, creating tight coupling and violating SRP.

### Refactored State
- **Decoupled Responsibilities**: State mutations of drivers are now strictly encapsulated within implementations of the `DriverService` interface (e.g., `InMemoryDriverService` via methods like `bookDriver()` and `releaseDriver()`).
- **Clean Boundaries**: `RideServiceImpl` acts as a coordinator, delegating domain mutations to respective service components rather than doing it itself.
- **DTO Separation**: Introduced the `RideRequest` class to hold ride-booking parameters, separating the request representation from core logic.

---

## 2. Open/Closed Principle (OCP)
> *Software entities should be open for extension, but closed for modification.*

### Application in RideWise
- **Pluggable Strategies**: Matching and pricing strategies are abstracted via the `RideMatchingStrategy` and `FareStrategy` interfaces.
- **Dynamic Registry**: We introduced the `StrategyRegistry` factory class. Adding new strategies (e.g., `HighestRatedDriverStrategy` or `SurgeFareStrategy`) only requires registering them in `StrategyRegistry`. The core booking service `RideServiceImpl` and UI controller `Main` do not need any modifications to support them.
- **Polymorphic Calculations**: Pricing strategies utilize driver and vehicle properties polymorphically (e.g., vehicle-specific multipliers) to handle varying business calculations without modifying core flows.

---

## 3. Liskov Substitution Principle (LSP)
> *Subtypes must be substitutable for their base types.*

### Application in RideWise
- **Strategy Implementations**: The implementations `NearestDriverStrategy` and `LeastActiveDriverStrategy` can be safely substituted wherever `RideMatchingStrategy` is expected, without changing program correctness.
- **Service Decoupling**: Clients talk directly to interface abstractions (`RideService`, `DriverService`, `RiderService`). Whether we run the code with `InMemory` services or future `Database` services, the behavioral contract remains unbroken.

---

## 4. Interface Segregation Principle (ISP)
> *Clients should not be forced to depend on methods they do not use.*

### Application in RideWise
- **Granular Interfaces**: The interfaces for fare calculations (`FareStrategy`) and driver matching (`RideMatchingStrategy`) are kept small and highly focused. They only define methods essential to their specific behavior.
- **Separation of Services**: Interface definitions for `DriverService`, `RiderService`, and `RideService` contain distinct, cohesive method signatures. No client is forced to take a dependency on a massive "omnipotent" service.

---

## 5. Dependency Inversion Principle (DIP)
> *High-level modules should not depend on low-level modules. Both should depend on abstractions.*

### Prior Violations
Previously, the high-level coordinator `RideService` directly depended on concrete service classes (`DriverService` and `RiderService`), leading to tight coupling.

### Refactored State
- **Programming to Interfaces**: `RideServiceImpl` now depends strictly on `DriverService` and `RiderService` interface abstractions.
- **Injectable Abstractions**: Specific database, caching, or memory implementations of these services can be easily swapped out during object assembly in the application's Composition Root (`Main.java`).
