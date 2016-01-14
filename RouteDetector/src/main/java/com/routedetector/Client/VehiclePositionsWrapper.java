package com.routedetector.Client;

import java.util.Date;
import java.util.List;

import com.routedetector.AssetsJPA.VehiclePosition;
/**
 * 
 *  Class which holds list of vehicle positions collected in last 48 hours, and time 
 *  when list is retrieved in form of Date object
 * 
 * @author Danijel Sudimac
 *
 */
public class VehiclePositionsWrapper {
	private Date time;
	private List<VehiclePosition> vehiclePositions;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public VehiclePositionsWrapper(Date time, List<VehiclePosition> vehiclePositions) {
		this.time = time;
		this.vehiclePositions = vehiclePositions;
	}
	public List<VehiclePosition> getVehiclePositions() {
		return vehiclePositions;
	}
	public void setVehiclePositions(List<VehiclePosition> vehiclePositions) {
		this.vehiclePositions = vehiclePositions;
	}
}
