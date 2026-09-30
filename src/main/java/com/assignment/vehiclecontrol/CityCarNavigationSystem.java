package com.assignment.vehiclecontrol;

public final class CityCarNavigationSystem implements NavigationSystem {
    @Override
    public String routeTo(String destination) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination must not be blank.");
        }
        return "Route to " + destination.strip() + " via urban roads avoiding school zones";
    }

    @Override
    public int recommendedSpeed() { return 50; }
}
