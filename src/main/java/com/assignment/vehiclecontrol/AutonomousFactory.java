package com.assignment.vehiclecontrol;

public final class AutonomousFactory implements VehicleSystemFactory {
    @Override
    public EngineController createEngineController() { return new AutonomousStartup().start(0); }

    @Override
    public BrakeSystem createBrakeSystem() { return new AutonomousBrakeSystem(); }

    @Override
    public NavigationSystem createNavigationSystem() { return new AutonomousNavigationSystem(); }
}
