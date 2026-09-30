package com.assignment.vehiclecontrol;

public final class ElectricPerformanceNavigationSystem implements NavigationSystem {
    @Override
    public String routeTo(String destination) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination must not be blank.");
        }
        return "Route to " + destination.strip() + " via highways with charging stops";
    }

    @Override
    public int recommendedSpeed() { return 90; }
}
