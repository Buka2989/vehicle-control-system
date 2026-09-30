package com.assignment.vehiclecontrol;

public abstract class EngineStartup {
    protected abstract EngineController createEngineController();

    public final EngineController start(int targetSpeed) {
        EngineController engine = createEngineController();
        if (targetSpeed < 0 || targetSpeed > engine.maximumSpeed()) {
            throw new IllegalArgumentException("Requested startup speed exceeds the engine limits.");
        }
        engine.cutPower();
        engine.accelerateTo(targetSpeed);
        return engine;
    }
}
