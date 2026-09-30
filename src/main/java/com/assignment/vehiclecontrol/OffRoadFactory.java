package com.assignment.vehiclecontrol;

public final class OffRoadFactory implements VehicleSystemFactory {
    @Override
    public EngineController createEngineController() { return new OffRoadStartup().start(0); }

    @Override
    public BrakeSystem createBrakeSystem() { return new OffRoadBrakeSystem(); }

    @Override
    public NavigationSystem createNavigationSystem() { return new OffRoadNavigationSystem(); }
}
