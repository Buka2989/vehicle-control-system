package com.assignment.vehiclecontrol;

public interface EngineController {
    int maximumSpeed();
    void accelerateTo(int target);
    int speed();
    void cutPower();
}
