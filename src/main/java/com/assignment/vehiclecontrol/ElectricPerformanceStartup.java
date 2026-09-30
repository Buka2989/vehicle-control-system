package com.assignment.vehiclecontrol;

public final class ElectricPerformanceStartup extends EngineStartup {
    @Override
    protected EngineController createEngineController() {
        return new ElectricPerformanceEngineController();
    }
}
