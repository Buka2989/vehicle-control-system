package com.assignment.vehiclecontrol;

public final class OffRoadBrakeSystem implements BrakeSystem {
    @Override
    public double stoppingDistance(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative.");
        }
        double metersPerSecond = speed / 3.6;
        return metersPerSecond * metersPerSecond / (2 * 4.0);
    }

    @Override
    public String prepare() { return "Loose-surface ABS and hill hold"; }
}
