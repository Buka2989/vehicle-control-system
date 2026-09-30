# Assignment 2 — Factory Method & Abstract Factory

## Assignment purpose

A console Vehicle Control System simulates preparing a route, starting a trip, and stopping a vehicle. The same client operates different compatible sets of engine, brake, and navigation components. This is a teaching simulation, not software for controlling a real vehicle.

## Technologies

Java 21, Maven 3.9.12, JUnit 5.11.4, and PlantUML. No application frameworks or external runtime libraries.

## Project structure

```text
pom.xml
mvnw / mvnw.cmd
.mvn/wrapper/maven-wrapper.properties
src/main/java/com/assignment/vehiclecontrol/   28 classes and interfaces
src/test/java/com/assignment/vehiclecontrol/  2 test classes
docs/vehicle-control-system.puml
README.md
DEFENSE.md
REQUIREMENTS_CHECKLIST.md
```

The project uses one package to keep the two patterns easy to follow. The enclosing directory contains an unrelated earlier Python assignment; this directory is a standalone Maven project with its own Git repository.

## Domain model

Product types are `EngineController`, `BrakeSystem`, and `NavigationSystem`.

| Family | Stage | Engine limit | Route speed | Braking deceleration | Route policy |
|---|---|---:|---:|---:|---|
| CityCar | Original | 130 km/h | 50 km/h | 7 m/s² | Urban roads avoiding school zones |
| OffRoad | Original | 100 km/h | 30 km/h | 4 m/s² | Unpaved tracks and low-gradient climbs |
| ElectricPerformance | Original | 250 km/h | 90 km/h | 10 m/s² | Highways with charging stops |
| Autonomous | Extension | 80 km/h | 40 km/h | 6 m/s² | Mapped roads inside the operating zone |

Each family provides all three products: nine original concrete products, twelve after extension. Engines hold speed state and reject speeds outside their limits. Navigation chooses a route policy and recommended speed. Brakes calculate distance using `v² / (2a)`, after converting km/h to m/s. Reaction time, weather, sensors, and real map routing are outside this simulation.

## Factory Method

- Product: `EngineController`.
- Concrete products: the four `*EngineController` classes.
- Creator: `EngineStartup`.
- Concrete Creators: the four `*Startup` classes.
- Factory Method: protected, abstract `createEngineController()`.

`EngineStartup.start(targetSpeed)` creates the product, validates the requested speed against that product's limit, cuts power to establish a stopped state, and applies the target speed. Subclasses change construction while inheriting this algorithm. Family factories use this algorithm with zero as the safe initial speed.

This is Factory Method because subclasses override an instance creation method called by inherited business logic. A static switch returning objects would only be a simple factory. `FactorySelector` is such an initialization helper; it is not our Factory Method demonstration.

## Abstract Factory

`VehicleSystemFactory` defines three product-creation methods. `CityCarFactory`, `OffRoadFactory`, `ElectricPerformanceFactory`, and `AutonomousFactory` each supply one complete family. Engine creation delegates to the matching startup Creator, so both patterns participate in the actual application flow.

## Compatibility

`VehicleControlService` accepts one `VehicleSystemFactory`. It obtains all three products from that factory and stores them in private final fields. There is no constructor accepting separately selected products and no component replacement method. The selector only supplies the tested family factories.

Consistency is guaranteed for the supplied application wiring: each built-in factory creates only its own family. The interface is an extension contract, not a Java type-level proof; a deliberately incorrect custom factory could violate it. Family-creation tests verify all three concrete types, while cooperation tests verify navigation speed is within the engine limit and brakes can stop that engine. There are no family-tag exception checks.

## Runtime factory selection

`Main` passes one command-line value to `FactorySelector.resolve()`. The selector trims whitespace and ignores letter case. Unknown values produce a readable message and usage; missing or extra arguments show usage. These usage cases return normally without a stack trace.

## Business operations

1. `prepareVehicleForRoute(destination)` combines navigation planning with brake safety preparation.
2. `startTrip(destination)` prepares navigation and brakes, then accelerates to the smaller of the navigation recommendation and engine limit. It rejects a second active trip.
3. `performEmergencyStop()` calculates braking distance at the current speed, cuts engine power, and clears the active-trip state. It rejects stopping without an active trip.

## Fourth family extension

Added under `src/main/java/com/assignment/vehiclecontrol/`:

- `AutonomousEngineController.java`
- `AutonomousBrakeSystem.java`
- `AutonomousNavigationSystem.java`
- `AutonomousStartup.java`
- `AutonomousFactory.java`

Also added: `src/test/java/com/assignment/vehiclecontrol/AutonomousFamilyTest.java`.

Modified: `src/main/java/com/assignment/vehiclecontrol/FactorySelector.java` (one selection case and the supported-values string).

No other files changed in extension commit `90a30de`. `Main`, `VehicleControlService`, all product interfaces, and `EngineStartup` stayed unchanged. This documentation was added afterward.

## Tests

Run `mvn test` or `mvn clean test`. There are 23 executed JUnit test cases, including parameterized invocations: 20 original cases and 3 extension cases. They cover complete family creation, compatibility, speed and braking behavior, selector normalization, business state transitions, invalid input, CLI usage, and the fourth family. One test uses independent anonymous product implementations to prove the service works through abstractions and caps speed correctly.

## Running the project

With JDK 21 and Maven available in your terminal, run these commands from this directory:

```shell
mvn clean test
mvn compile
mvn exec:java -Dexec.args=city
mvn exec:java -Dexec.args=offroad
mvn exec:java -Dexec.args=electric
mvn exec:java -Dexec.args=autonomous
mvn exec:java -Dexec.args=unknown
mvn exec:java
```

`exec-maven-plugin` is configured with the Main class. Compile before a standalone `exec:java` on a fresh checkout, or use `mvn compile exec:java -Dexec.args=city`.

The standard Maven wrapper is included: on Windows replace `mvn` with `.\mvnw.cmd`; on Linux/macOS use `./mvnw`. It requires a JDK and downloads Maven on first use if needed.

On the machine where this project was built, a portable Java 21 JDK was downloaded into the parent workspace. In PowerShell, from this project directory:

```powershell
$env:JAVA_HOME = (Get-ChildItem ..\.tooling\java21 -Directory | Select-Object -First 1).FullName
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\mvnw.cmd clean test
.\mvnw.cmd compile exec:java -Dexec.args=city
```

The JDK is outside the submission repository. On another machine set `JAVA_HOME` to your own JDK 21 installation. The POM targets Java 21 using `maven.compiler.release`.

## UML

See [docs/vehicle-control-system.puml](docs/vehicle-control-system.puml) and the rendered [SVG diagram](docs/vehicle-control-system.svg). Factory Method and Abstract Factory have separate labeled groups; the engine product is shared by both patterns. Render with a PlantUML-compatible viewer or `java -jar plantuml.jar -tsvg docs/vehicle-control-system.puml`.

## Git history

The real history starts with direct engine construction, then introduces the engine abstraction, Factory Method, complete product families, Abstract Factory, runtime selection, and 20 passing original tests. Only then was the fourth family added and tested. Documentation and final validation follow those implementation stages.

Useful evidence commands:

```shell
git log --oneline
git show 6a8dfa8:src/main/java/com/assignment/vehiclecontrol/Main.java
git show --stat 90a30de
git diff 90a30de^ 90a30de -- src/main/java/com/assignment/vehiclecontrol/VehicleControlService.java
```

The last command has no output because the business service did not change. The initial client depended directly on concrete classes, duplicated startup operations across switch branches, and needed editing for every new engine family. There were no factories in that first commit.
