package com.assignment.vehiclecontrol;

public final class CityCarBrakeSystem implements BrakeSystem {
    @Override
    public double stoppingDistance(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative.");
        }
        double metersPerSecond = speed / 3.6;
        return metersPerSecond * metersPerSecond / (2 * 7.0);
    }

    @Override
    public String prepare() { return "ABS for paved streets"; }
}
