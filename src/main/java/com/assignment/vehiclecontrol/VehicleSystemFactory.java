package com.assignment.vehiclecontrol;

public interface VehicleSystemFactory {
    EngineController createEngineController();
    BrakeSystem createBrakeSystem();
    NavigationSystem createNavigationSystem();
}
