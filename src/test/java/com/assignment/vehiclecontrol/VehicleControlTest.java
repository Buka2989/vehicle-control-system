package com.assignment.vehiclecontrol;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class VehicleControlTest {
    @ParameterizedTest
    @CsvSource({"city,CityCar", "offroad,OffRoad", "electric,ElectricPerformance"})
    void factoryCreatesCompleteConcreteFamily(String key, String prefix) {
        VehicleSystemFactory factory = FactorySelector.resolve(key);
        assertEquals(prefix + "EngineController", factory.createEngineController().getClass().getSimpleName());
        assertEquals(prefix + "BrakeSystem", factory.createBrakeSystem().getClass().getSimpleName());
        assertEquals(prefix + "NavigationSystem", factory.createNavigationSystem().getClass().getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"city", "offroad", "electric"})
    void familyComponentsCooperateWithinEngineLimits(String key) {
        VehicleSystemFactory factory = FactorySelector.resolve(key);
        EngineController engine = factory.createEngineController();
        int speed = factory.createNavigationSystem().recommendedSpeed();
        assertTrue(speed <= engine.maximumSpeed());
        engine.accelerateTo(speed);
        assertEquals(speed, engine.speed());
        assertTrue(factory.createBrakeSystem().stoppingDistance(engine.speed()) > 0);
        engine.cutPower();
        assertEquals(0, engine.speed());
    }

    @Test
    void brakingModelsUseDifferentDeceleration() {
        assertEquals(50.0, new OffRoadBrakeSystem().stoppingDistance(72), 0.001);
        assertEquals(28.5714, new CityCarBrakeSystem().stoppingDistance(72), 0.001);
        assertEquals(20.0, new ElectricPerformanceBrakeSystem().stoppingDistance(72), 0.001);
        assertEquals(0.0, new CityCarBrakeSystem().stoppingDistance(0));
        assertThrows(IllegalArgumentException.class, () -> new CityCarBrakeSystem().stoppingDistance(-1));
    }

    @Test
    void creatorStartsSelectedEngineAtRequestedSpeed() {
        EngineController engine = new ElectricPerformanceStartup().start(200);
        assertInstanceOf(ElectricPerformanceEngineController.class, engine);
        assertEquals(200, engine.speed());
        assertEquals(100, new OffRoadStartup().start(100).speed());
    }

    @Test
    void creatorAndEngineRejectUnsafeSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new OffRoadStartup().start(101));
        assertThrows(IllegalArgumentException.class, () -> new CityCarStartup().start(-1));
        EngineController engine = new CityCarStartup().start(30);
        assertThrows(IllegalArgumentException.class, () -> engine.accelerateTo(131));
        assertEquals(30, engine.speed());
    }

    @ParameterizedTest
    @CsvSource({" CITY ,CityCarFactory", "OffRoad,OffRoadFactory", "ELECTRIC,ElectricPerformanceFactory"})
    void selectorNormalizesExternalInput(String input, String expected) {
        assertEquals(expected, FactorySelector.resolve(input).getClass().getSimpleName());
    }

    @Test
    void selectorRejectsUnknownAndMissingFamily() {
        assertThrows(IllegalArgumentException.class, () -> FactorySelector.resolve("airplane"));
        assertThrows(IllegalArgumentException.class, () -> FactorySelector.resolve(" "));
        assertThrows(IllegalArgumentException.class, () -> FactorySelector.resolve(null));
    }

    @Test
    void preparationCombinesRouteAndSafety() {
        VehicleControlService service = new VehicleControlService(new OffRoadFactory());
        String report = service.prepareVehicleForRoute("Forest");
        assertTrue(report.contains("Forest via unpaved tracks"));
        assertTrue(report.contains("hill hold"));
        assertThrows(IllegalStateException.class, service::performEmergencyStop);
    }

    @Test
    void startTripUsesNavigationSpeedAndBrakePreparation() {
        VehicleControlService service = new VehicleControlService(new ElectricPerformanceFactory());
        String report = service.startTrip("University");
        assertTrue(report.contains("charging stops"));
        assertTrue(report.contains("Regenerative and friction braking"));
        assertTrue(report.contains("90 km/h"));
    }

    @Test
    void emergencyStopUsesCurrentSpeedAndAllowsNextTrip() {
        VehicleControlService service = new VehicleControlService(new CityCarFactory());
        service.startTrip("University");
        assertEquals("Emergency stop: 13.78 m braking distance | Power cut | Speed: 0 km/h", service.performEmergencyStop());
        assertThrows(IllegalStateException.class, service::performEmergencyStop);
        assertTrue(service.startTrip("Home").contains("Home"));
    }

    @Test
    void duplicateStartDoesNotReplaceActiveTrip() {
        VehicleControlService service = new VehicleControlService(new CityCarFactory());
        service.startTrip("University");
        assertThrows(IllegalStateException.class, () -> service.startTrip("Home"));
        assertTrue(service.performEmergencyStop().contains("13.78 m"));
    }

    @Test
    void invalidDestinationDoesNotStartTrip() {
        VehicleControlService service = new VehicleControlService(new CityCarFactory());
        assertThrows(IllegalArgumentException.class, () -> service.startTrip(" "));
        assertThrows(IllegalArgumentException.class, () -> service.prepareVehicleForRoute(null));
        assertThrows(IllegalStateException.class, service::performEmergencyStop);
        assertTrue(service.startTrip("Home").contains("Home"));
    }

    @Test
    void serviceAcceptsIndependentAbstractProductsAndCapsSpeed() {
        int[] speed = {0};
        boolean[] prepared = {false};
        int[] brakingSpeed = {-1};
        VehicleSystemFactory factory = new VehicleSystemFactory() {
            public EngineController createEngineController() {
                return new EngineController() {
                    public int maximumSpeed() { return 12; }
                    public void accelerateTo(int target) { speed[0] = target; }
                    public int speed() { return speed[0]; }
                    public void cutPower() { speed[0] = 0; }
                };
            }
            public BrakeSystem createBrakeSystem() {
                return new BrakeSystem() {
                    public double stoppingDistance(int value) { brakingSpeed[0] = value; return 3.0; }
                    public String prepare() { prepared[0] = true; return "Test safety"; }
                };
            }
            public NavigationSystem createNavigationSystem() {
                return new NavigationSystem() {
                    public String routeTo(String destination) { return "Test route to " + destination; }
                    public int recommendedSpeed() { return 20; }
                };
            }
        };
        VehicleControlService service = new VehicleControlService(factory);
        assertTrue(service.startTrip("Lab").contains("Test route to Lab"));
        assertTrue(prepared[0]);
        assertEquals(12, speed[0]);
        assertTrue(service.performEmergencyStop().contains("3.00 m"));
        assertEquals(12, brakingSpeed[0]);
        assertEquals(0, speed[0]);
    }

    @Test
    void mainExplainsMissingAndUnsupportedArguments() {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output)) {
            System.setOut(capture);
            Main.main(new String[0]);
            Main.main(new String[]{"unknown"});
            Main.main(new String[]{"city", "extra"});
        } finally {
            System.setOut(original);
        }
        assertTrue(output.toString().contains("Usage:"));
        assertTrue(output.toString().contains("Unsupported vehicle family: unknown"));
        assertFalse(output.toString().contains("Driving at"));
    }
}
