package com.assignment.vehiclecontrol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutonomousFamilyTest {
    @Test
    void selectorCreatesCompleteFourthFamily() {
        VehicleSystemFactory factory = FactorySelector.resolve("autonomous");
        assertInstanceOf(AutonomousFactory.class, factory);
        assertInstanceOf(AutonomousEngineController.class, factory.createEngineController());
        assertInstanceOf(AutonomousBrakeSystem.class, factory.createBrakeSystem());
        assertInstanceOf(AutonomousNavigationSystem.class, factory.createNavigationSystem());
    }

    @Test
    void autonomousComponentsRespectConservativeLimits() {
        EngineController engine = new AutonomousStartup().start(80);
        assertEquals(80, engine.speed());
        assertThrows(IllegalArgumentException.class, () -> new AutonomousStartup().start(81));
        assertEquals(40, new AutonomousNavigationSystem().recommendedSpeed());
        assertEquals(33.3333, new AutonomousBrakeSystem().stoppingDistance(72), 0.001);
    }

    @Test
    void unchangedServiceRunsAutonomousTrip() {
        VehicleControlService service = new VehicleControlService(FactorySelector.resolve("autonomous"));
        assertTrue(service.prepareVehicleForRoute("Campus").contains("Sensor-triggered braking"));
        String trip = service.startTrip("Campus");
        assertTrue(trip.contains("operating zone"));
        assertTrue(trip.contains("40 km/h"));
        assertEquals("Emergency stop: 10.29 m braking distance | Power cut | Speed: 0 km/h", service.performEmergencyStop());
    }
}
