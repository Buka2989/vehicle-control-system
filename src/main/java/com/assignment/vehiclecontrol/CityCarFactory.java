package com.assignment.vehiclecontrol;

public final class CityCarFactory implements VehicleSystemFactory {
    @Override
    public EngineController createEngineController() { return new CityCarStartup().start(0); }

    @Override
    public BrakeSystem createBrakeSystem() { return new CityCarBrakeSystem(); }

    @Override
    public NavigationSystem createNavigationSystem() { return new CityCarNavigationSystem(); }
}
