package com.assignment.vehiclecontrol;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            return;
        }
        try {
            VehicleSystemFactory factory = FactorySelector.resolve(args[0]);
            VehicleControlService vehicle = new VehicleControlService(factory);
            System.out.println(vehicle.prepareVehicleForRoute("University"));
            System.out.println(vehicle.startTrip("University"));
            System.out.println(vehicle.performEmergencyStop());
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: mvn exec:java -Dexec.args=\"" + FactorySelector.supportedFamilies() + "\"");
    }
}
