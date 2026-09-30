# Requirements checklist

Verified on 2026-09-30. Locations below use the package `com.assignment.vehiclecontrol` unless a file path is given.

| Assignment requirement | Status | Implementation location | Test/evidence |
|---|---|---|---|
| Custom Vehicle Control System domain | Complete | All production classes; README domain model | Four working console modes |
| Java 21, Maven, JUnit 5 | Complete | pom.xml; Maven wrapper | Clean build on Temurin 21.0.12.1, Maven 3.9.12 |
| Three product types | Complete | EngineController, BrakeSystem, NavigationSystem | Factory creation assertions check all three |
| Exactly three initial families | Complete | CityCar, OffRoad, ElectricPerformance | Original test commit dadac3f precedes extension |
| At least nine original concrete products | Complete | Three product implementations per original family | eb58c14 introduces complete original families |
| Initial implementation without either factory pattern | Complete | Main at 6a8dfa8 | Direct constructors and switch in first commit; initial city run succeeded |
| At least three direct-creation problems | Complete | Initial Main; README; DEFENSE question 25 | Concrete dependencies, duplicated startup, client modification for each extension |
| Product abstractions | Complete | Three product interfaces | Service fields and constructor use interfaces |
| Factory Method with concrete Creators | Complete | EngineStartup and four concrete Startup classes | Creator tests exercise concrete products and speed limits |
| Meaningful Creator logic | Complete | EngineStartup.start | Validation, power reset, acceleration; valid and invalid startup tests |
| Abstract Factory | Complete | VehicleSystemFactory and four family factories | Complete concrete-family creation tests |
| Compatible families by normal construction | Complete | VehicleControlService constructor and private final fields | Three original family cooperation cases; fourth-family checks |
| No family-tag exception workaround | Complete | Factory wiring | No family IDs or wrong-family checks in source; custom factories must honor contract |
| Runtime command-line selection | Complete | FactorySelector; Main | Three normalization cases; all four CLI modes run |
| Unsupported and missing argument handling | Complete | Main; FactorySelector | CLI test plus manual unknown and missing-argument runs |
| Client works through abstractions | Complete | VehicleControlService | Independent anonymous factory/products test proves substituted behavior |
| At least three multi-product operations | Complete | prepareVehicleForRoute, startTrip, performEmergencyStop | Preparation, trip, and emergency-stop tests |
| Actual behavioral differences | Complete | Engine limits, navigation speeds, braking models | Distinct CLI results and numerical braking assertions |
| Fourth complete family added later | Complete | 90a30de; Autonomous classes | Original 20 tests passed before commit dadac3f; extension adds 3 passing tests |
| Minimal changes for fourth family | Complete | README and DEFENSE exact file lists | git show 90a30de: six added files and only FactorySelector modified |
| Business logic unchanged for extension | Complete | VehicleControlService; EngineStartup; Main | Empty diff for these files across 90a30de |
| PlantUML matches source | Complete | docs/vehicle-control-system.puml and .svg | PlantUML syntax check and rendering succeeded; all 28 declared source types matched; relationships reviewed |
| Approximately 20 automated tests | Complete | Two JUnit test classes | 23 cases, zero failures/errors/skips in Surefire reports |
| Negative tests | Complete | VehicleControlTest; AutonomousFamilyTest | Unsafe speed, invalid destination/family, duplicate trip, stopping without active trip |
| Business tests | Complete | VehicleControlTest | Actual route/safety reports, speed cap, braking input, engine stop and restart checked |
| README with setup and exact commands | Complete | README.md | Configured exec plugin; compile exec:java and wrapper commands executed |
| Oral defense guide | Complete | DEFENSE.md | 25 question answers, all production types explained, demo sequence |
| English content and no Java comments | Complete | Production/test source and documentation | Source scan found no line or block comments; content reviewed |
| Simple console-only architecture | Complete | Single package; pom.xml | No application framework, reflection, Lombok, or advanced generic hierarchy |
| Meaningful progressive local Git history | Complete | .git in this project directory | Ten logical commits; initial state committed before refactoring and extension after original tests |
| Clean structure and tracked deliverables | Complete | Standard src/main/java and src/test/java; docs | git diff --check; git status checked before and after final audit commit |

## Executed validation

- Built and ran the initial direct-construction version before its commit.
- Ran original `mvn clean test`: 20 tests, zero failures/errors/skips before adding Autonomous.
- Ran extension `mvn test`: 23 tests, zero failures/errors/skips.
- Ran final `.\mvnw.cmd clean test` on Java 21: 23 tests, zero failures/errors/skips.
- Ran `exec:java` for every family, unknown input, and no argument. Also ran the documented combined `compile exec:java -Dexec.args=city` command through Maven.
- Checked PlantUML syntax, rendered SVG, inspected a PNG preview, compared all 28 production declarations, and reviewed relationships against source.
- Inspected `git log --oneline`, `git status`, `git diff --check`, and the exact extension diff. No original business file changed in that extension.

| CLI family | Driving speed | Calculated braking distance | Distinguishing route |
|---|---:|---:|---|
| city | 50 km/h | 13.78 m | Urban roads avoiding school zones |
| offroad | 30 km/h | 8.68 m | Unpaved tracks with low-gradient climbs |
| electric | 90 km/h | 31.25 m | Highways with charging stops |
| autonomous | 40 km/h | 10.29 m | Mapped roads inside the operating zone |

All stop reports showed zero speed after power cut. Unknown and missing inputs displayed usage without a stack trace.

## Scope of the compatibility claim

The application selects only built-in factories, each of which returns its own complete family. The service cannot receive or replace individual components. An intentionally incorrect third-party factory can still violate the interface contract; compile-time enforcement against such implementations is not claimed. No mixed-family exception check is used.

## Submission notes

Submit the `vehicle-control-system` directory, including its hidden `.git` directory if the instructor needs local development history. Do not include `target` or the parent workspace's `.tooling` downloads. The preceding Python project remains separate. No assignment requirement remains unimplemented.
