package com.assignment.vehiclecontrol;

public final class ElectricPerformanceFactory implements VehicleSystemFactory {
    @Override
    public EngineController createEngineController() { return new ElectricPerformanceStartup().start(0); }

    @Override
    public BrakeSystem createBrakeSystem() { return new ElectricPerformanceBrakeSystem(); }

    @Override
    public NavigationSystem createNavigationSystem() { return new ElectricPerformanceNavigationSystem(); }
}
