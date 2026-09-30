package com.assignment.vehiclecontrol;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: <city|offroad|electric>");
            return;
        }
        EngineStartup startup = switch (args[0]) {
            case "city" -> new CityCarStartup();
            case "offroad" -> new OffRoadStartup();
            case "electric" -> new ElectricPerformanceStartup();
            default -> null;
        };
        if (startup == null) {
            System.out.println("Unsupported vehicle family: " + args[0]);
            return;
        }
        System.out.println("Trip at " + startup.start(40).speed() + " km/h");
    }
}
