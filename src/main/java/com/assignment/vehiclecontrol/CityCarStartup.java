package com.assignment.vehiclecontrol;

public final class CityCarStartup extends EngineStartup {
    @Override
    protected EngineController createEngineController() {
        return new CityCarEngineController();
    }
}
