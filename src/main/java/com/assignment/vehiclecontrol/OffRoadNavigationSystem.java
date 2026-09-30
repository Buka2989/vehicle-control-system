package com.assignment.vehiclecontrol;

public final class OffRoadNavigationSystem implements NavigationSystem {
    @Override
    public String routeTo(String destination) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination must not be blank.");
        }
        return "Route to " + destination.strip() + " via unpaved tracks with low-gradient climbs";
    }

    @Override
    public int recommendedSpeed() { return 30; }
}
