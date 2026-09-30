package com.assignment.vehiclecontrol;

public final class OffRoadStartup extends EngineStartup {
    @Override
    protected EngineController createEngineController() {
        return new OffRoadEngineController();
    }
}
