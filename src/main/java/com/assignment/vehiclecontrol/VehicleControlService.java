package com.assignment.vehiclecontrol;

import java.util.Locale;
import java.util.Objects;

public final class VehicleControlService {
    private final EngineController engine;
    private final BrakeSystem brakes;
    private final NavigationSystem navigation;
    private boolean tripActive;

    public VehicleControlService(VehicleSystemFactory factory) {
        Objects.requireNonNull(factory, "Vehicle factory is required.");
        engine = factory.createEngineController();
        brakes = factory.createBrakeSystem();
        navigation = factory.createNavigationSystem();
    }

    public String prepareVehicleForRoute(String destination) {
        String route = navigation.routeTo(destination);
        return route + " | Safety: " + brakes.prepare();
    }

    public String startTrip(String destination) {
        if (tripActive) {
            throw new IllegalStateException("Stop the current trip before starting another.");
        }
        String preparation = prepareVehicleForRoute(destination);
        int targetSpeed = Math.min(navigation.recommendedSpeed(), engine.maximumSpeed());
        engine.accelerateTo(targetSpeed);
        tripActive = true;
        return preparation + " | Driving at " + engine.speed() + " km/h";
    }

    public String performEmergencyStop() {
        if (!tripActive) {
            throw new IllegalStateException("Start a trip before requesting an emergency stop.");
        }
        double distance = brakes.stoppingDistance(engine.speed());
        engine.cutPower();
        tripActive = false;
        return String.format(Locale.ROOT,
                "Emergency stop: %.2f m braking distance | Power cut | Speed: %d km/h",
                distance, engine.speed());
    }
}
