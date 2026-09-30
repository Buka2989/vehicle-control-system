package com.assignment.vehiclecontrol;

public final class AutonomousStartup extends EngineStartup {
    @Override
    protected EngineController createEngineController() {
        return new AutonomousEngineController();
    }
}
