package com.assignment.vehiclecontrol;

public final class AutonomousNavigationSystem implements NavigationSystem {
    @Override
    public String routeTo(String destination) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination must not be blank.");
        }
        return "Route to " + destination.strip() + " via mapped roads inside the operating zone";
    }

    @Override
    public int recommendedSpeed() { return 40; }
}
