package com.assignment.vehiclecontrol;

public final class Main {
    public static void main(String[] args) {
        VehicleSystemFactory factory = new CityCarFactory();
        VehicleControlService vehicle = new VehicleControlService(factory);
        System.out.println(vehicle.prepareVehicleForRoute("University"));
        System.out.println(vehicle.startTrip("University"));
        System.out.println(vehicle.performEmergencyStop());
    }
}
