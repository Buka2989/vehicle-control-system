package com.assignment.vehiclecontrol;

public final class ElectricPerformanceBrakeSystem implements BrakeSystem {
    @Override
    public double stoppingDistance(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative.");
        }
        double metersPerSecond = speed / 3.6;
        return metersPerSecond * metersPerSecond / (2 * 10.0);
    }

    @Override
    public String prepare() { return "Regenerative and friction braking"; }
}
