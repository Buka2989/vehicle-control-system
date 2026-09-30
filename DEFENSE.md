# Oral defense preparation

## 1. What problem does this application solve?

It assembles compatible vehicle controls and runs the same trip operations for several vehicle families.

## 2. What are the product types?

`EngineController`, `BrakeSystem`, and `NavigationSystem`.

## 3. What are the product families?

The original families are CityCar, OffRoad, and ElectricPerformance. Autonomous was added later.

## 4. What is Factory Method?

A Creator calls an overridable method to create a product. Subclasses choose the concrete product while sharing the Creator algorithm.

## 5. Where exactly is Factory Method used?

`EngineStartup.createEngineController()` is the factory method. Each concrete `*Startup` overrides it, and inherited `start()` uses it.

## 6. Why is this Factory Method and not just a static factory?

Creation varies through subclass overriding and instance-method dispatch. The inherited startup algorithm calls that method. The static `FactorySelector.resolve()` is a separate simple selection helper.

## 7. What meaningful logic does the Creator contain?

`start(targetSpeed)` checks the requested speed against the created engine limit, cuts power, and applies the requested speed. Invalid speeds are rejected before the engine is driven. Family factories request zero for a safe initial state.

## 8. What is Abstract Factory?

An interface for creating a group of related products without making the client depend on their concrete classes.

## 9. Where exactly is Abstract Factory used?

`VehicleSystemFactory` has methods for the engine, brakes, and navigation. The four concrete family factories implement all three.

## 10. What is the difference between the patterns here?

Factory Method varies one engine through startup subclasses. Abstract Factory provides a whole matching vehicle family through one factory object. Each family factory uses its startup Creator to create the engine.

## 11. Why does the Client not instantiate concrete classes directly?

`VehicleControlService` only needs the three product interfaces. Construction details belong to the factories, so adding a family does not require editing trip operations.

## 12. How does runtime factory selection work?

`Main` reads one argument. `FactorySelector` normalizes it and selects the matching factory. Then the service runs through interfaces.

## 13. How does the architecture guarantee product-family compatibility?

The service takes one factory and obtains every component from it. All built-in factories return only their own family, verified by tests. This guarantee assumes a correct implementation of the factory contract; arbitrary custom factories are not checked by the type system.

## 14. Why is mixing products difficult?

The service has no constructor for individual components and no replacement setters. Its component fields are private and final. Normal application initialization offers only complete factories.

## 15. What are the three main business operations?

`prepareVehicleForRoute` coordinates navigation and brakes. `startTrip` also accelerates the engine. `performEmergencyStop` calculates braking distance and cuts power.

## 16. How do concrete families behave differently?

CityCar plans urban routes at 50 km/h. OffRoad chooses tracks at 30. ElectricPerformance plans charging stops at 90. Autonomous uses mapped operating-zone roads at 40. Engine limits and braking deceleration also differ.

## 17. What happens for unsupported input?

The selector throws `IllegalArgumentException`. Main catches it and prints the message and usage without a stack trace. Missing or extra arguments also show usage.

## 18. How was the fourth family added?

After the three-family version passed 20 tests and was committed, five Autonomous classes, a selector case, and three extension tests were added. All 23 tests then passed.

## 19. Which files changed for the fourth family?

See the exact extension file list below. Only `FactorySelector.java` was modified; the six other files were new.

## 20. Why did existing business logic not need major changes?

It already used product interfaces and the abstract factory. The new implementations satisfy those same contracts.

## 21. How does this demonstrate the Open/Closed Principle?

Trip and startup algorithms are open to extension through new implementations without edits to those algorithms. The explicit selector still needs a small registration change; the whole system is not completely closed to modification.

## 22. What tests exist?

There are 23 executed JUnit cases covering concrete family creation, family cooperation, braking calculations, startup limits, runtime selection, business operations, invalid states, CLI messages, abstractions, and Autonomous behavior.

## 23. Give examples of negative tests.

Unknown or blank family, negative or excessive speed, blank destination, starting an already active trip, and emergency stop without an active trip. Failed operations preserve the relevant state.

## 24. How can you prove that the Client works through abstractions?

`serviceAcceptsIndependentAbstractProductsAndCapsSpeed` passes an anonymous factory with entirely independent test products. It checks preparation, a speed cap of 12 km/h, the speed passed to brakes, and power cut.

## 25. What were the initial direct-creation problems?

Commit `6a8dfa8` has a switch in Main with three concrete engine constructors. Main depends on those classes, repeats acceleration and reporting, and must change for another family. Later product sets would also be easy to mix if separately constructed.

## Exact fourth-family changes

Added under `src/main/java/com/assignment/vehiclecontrol/`:

- `AutonomousEngineController.java`
- `AutonomousBrakeSystem.java`
- `AutonomousNavigationSystem.java`
- `AutonomousStartup.java`
- `AutonomousFactory.java`

Also added: `src/test/java/com/assignment/vehiclecontrol/AutonomousFamilyTest.java`.

Modified: `src/main/java/com/assignment/vehiclecontrol/FactorySelector.java` (one selection case and the supported-values string).

No other files changed in extension commit `90a30de`. `Main`, `VehicleControlService`, all product interfaces, and `EngineStartup` stayed unchanged. This documentation was added afterward.

## Class-by-class explanation

- `EngineController`: Contract for speed limits, acceleration, current speed, and power cut.
- `BrakeSystem`: Contract for brake preparation and stopping-distance calculation.
- `NavigationSystem`: Contract for a destination route and recommended speed.
- `EngineStartup`: Abstract Creator; owns the final startup algorithm and protected Factory Method.
- `VehicleSystemFactory`: Abstract Factory contract for the complete set of three products.
- `VehicleControlService`: Client; owns a factory-created component set and coordinates the three operations and trip state.
- `FactorySelector`: Converts the external family value into a built-in factory; owns the supported-values string.
- `Main`: Console entry point; handles arguments and prints the three operation results.
- `CityCarEngineController`: Stateful urban engine with its own speed limit.
- `CityCarBrakeSystem`: Urban brake preparation and deceleration model.
- `CityCarNavigationSystem`: Urban routing policy and recommended speed.
- `CityCarStartup`: Concrete Creator that constructs CityCarEngineController.
- `CityCarFactory`: Concrete Abstract Factory for the complete CityCar family.
- `OffRoadEngineController`: Stateful rough-terrain engine with its own speed limit.
- `OffRoadBrakeSystem`: Rough-terrain brake preparation and deceleration model.
- `OffRoadNavigationSystem`: Rough-terrain routing policy and recommended speed.
- `OffRoadStartup`: Concrete Creator that constructs OffRoadEngineController.
- `OffRoadFactory`: Concrete Abstract Factory for the complete OffRoad family.
- `ElectricPerformanceEngineController`: Stateful electric highway engine with its own speed limit.
- `ElectricPerformanceBrakeSystem`: Electric highway brake preparation and deceleration model.
- `ElectricPerformanceNavigationSystem`: Electric highway routing policy and recommended speed.
- `ElectricPerformanceStartup`: Concrete Creator that constructs ElectricPerformanceEngineController.
- `ElectricPerformanceFactory`: Concrete Abstract Factory for the complete ElectricPerformance family.
- `AutonomousEngineController`: Stateful mapped operating-zone engine with its own speed limit.
- `AutonomousBrakeSystem`: Mapped operating-zone brake preparation and deceleration model.
- `AutonomousNavigationSystem`: Mapped operating-zone routing policy and recommended speed.
- `AutonomousStartup`: Concrete Creator that constructs AutonomousEngineController.
- `AutonomousFactory`: Concrete Abstract Factory for the complete Autonomous family.
- `VehicleControlTest`: Original 20 test cases, including an independent factory and product test doubles.
- `AutonomousFamilyTest`: Three extension tests for construction, behavior, and unchanged service integration.

## Five-minute demonstration

1. Run `mvn clean test` and show 23 passing tests.
2. Run `mvn exec:java -Dexec.args=city`, then `offroad`, `electric`, and `autonomous`; compare speed, route policy, and stopping distance.
3. Run `mvn exec:java -Dexec.args=unknown` to show clean input handling.
4. Open `EngineStartup`, then a concrete Startup and its family factory. Trace `Main -> selector -> service -> factory -> startup -> engine`.
5. Show the UML and `git show --stat 90a30de`. Explain why the service did not change.

The emergency-stop calculation is an idealized braking distance. It excludes reaction distance. Navigation policies and safety preparation are simulated descriptions, while speed changes, validation, trip state, and braking calculations execute real program logic.
