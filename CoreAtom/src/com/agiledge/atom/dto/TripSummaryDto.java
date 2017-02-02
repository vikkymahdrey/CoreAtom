package com.agiledge.atom.dto;

import java.util.ArrayList;
import java.util.HashSet;



public class TripSummaryDto {
private ArrayList<VehicleTypeDto> vehicles;
private HashSet<String> vehicleTypes;

public ArrayList<VehicleTypeDto> getVehicles() {
	return vehicles;
}

public void setVehicles(ArrayList<VehicleTypeDto> vehicles) {
	this.vehicles = vehicles;
}

public HashSet<String> getVehicleTypes() {
	return vehicleTypes;
}

public void setVehicleTypes(HashSet<String> vehicleTypes) {
	this.vehicleTypes = vehicleTypes;
}


}
