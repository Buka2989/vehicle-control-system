package com.assignment.vehiclecontrol;

import java.util.Locale;

public final class FactorySelector {
    private FactorySelector() { }

    public static String supportedFamilies() { return "city|offroad|electric|autonomous"; }

    public static VehicleSystemFactory resolve(String family) {
        if (family == null || family.isBlank()) {
            throw new IllegalArgumentException("Vehicle family is required.");
        }
        return switch (family.strip().toLowerCase(Locale.ROOT)) {
            case "city" -> new CityCarFactory();
            case "offroad" -> new OffRoadFactory();
            case "electric" -> new ElectricPerformanceFactory();
            case "autonomous" -> new AutonomousFactory();
            default -> throw new IllegalArgumentException("Unsupported vehicle family: " + family);
        };
    }
}
