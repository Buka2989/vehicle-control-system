package com.assignment.vehiclecontrol;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: <city|offroad|electric>");
            return;
        }
        switch (args[0]) {
            case "city" -> {
                EngineController engine = new CityCarEngineController();
                engine.accelerateTo(40);
                System.out.println("City trip at " + engine.speed() + " km/h");
            }
            case "offroad" -> {
                EngineController engine = new OffRoadEngineController();
                engine.accelerateTo(40);
                System.out.println("Off-road trip at " + engine.speed() + " km/h");
            }
            case "electric" -> {
                EngineController engine = new ElectricPerformanceEngineController();
                engine.accelerateTo(40);
                System.out.println("Electric trip at " + engine.speed() + " km/h");
            }
            default -> System.out.println("Unsupported vehicle family: " + args[0]);
        }
    }
}
