package com.routedetector.Client;

import com.routedetector.JPA.GprsDevice;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * 
 * @author Danijel Sudimac
 * 
 * Class used by 'Gprs' tab TableView. GprsDevice object does not have assigned vehicle and driver information,
 * for showing those information, new Class is introduced. This class has four fields, 
 * GprsDevice object, and String objects for holding vehicle license plate number, and assigned drivers to this vehicle.
 *
 */
public class GprsDeviceWrapper {
	private GprsDevice gprsDevice;
	private String vehicleLicensePlate;
	private String driverName;
	private String codriverName;
	
	public GprsDeviceWrapper(GprsDevice gprsDevice,String vehicleLicensePlate, String driverName, String coDriverName){
		this.gprsDevice=gprsDevice;
		this.vehicleLicensePlate=vehicleLicensePlate;
		this.driverName=driverName;
		this.codriverName=coDriverName;
	}

	public GprsDevice getGprsDevice() {
		return gprsDevice;
	}

	public void setGprsDevice(GprsDevice gprsDevice) {
		this.gprsDevice = gprsDevice;
	}

	public String getVehicleLicensePlate() {
		return vehicleLicensePlate;
	}

	public void setVehicleLicensePlate(String vehicleLicensePlate) {
		this.vehicleLicensePlate = vehicleLicensePlate;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getCodriverName() {
		return codriverName;
	}

	public void setCodriverName(String codriverName) {
		this.codriverName = codriverName;
	}
	
	public StringProperty getVehicleLicensePlateProperty() {
		StringProperty s=new SimpleStringProperty(this.vehicleLicensePlate);
		return s;
	}

	public void setCurrentVehiclePlateNumberProperty(StringProperty vehicleLicensePlate) {
		this.vehicleLicensePlate = vehicleLicensePlate.get();
	}
	
	public StringProperty getDriverNameProperty() {
		StringProperty s=new SimpleStringProperty(this.driverName);
		return s;
	}

	public void setDriverName(StringProperty driverName) {
		this.driverName = driverName.get();
	}
	public StringProperty getCodriverNameProperty() {
		StringProperty s=new SimpleStringProperty(this.codriverName);
		return s;
	}

	public void setCodriverName(StringProperty codriverName) {
		this.codriverName = codriverName.get();
	}
}
