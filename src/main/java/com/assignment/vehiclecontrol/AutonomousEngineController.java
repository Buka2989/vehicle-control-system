package com.assignment.vehiclecontrol;

public final class AutonomousEngineController implements EngineController {
    private int speed;

    public int maximumSpeed() { return 80; }

    public void accelerateTo(int target) {
        if (target < 0 || target > maximumSpeed()) {
            throw new IllegalArgumentException("Speed is outside the vehicle limit.");
        }
        speed = target;
    }

    public int speed() { return speed; }

    public void cutPower() { speed = 0; }
}
