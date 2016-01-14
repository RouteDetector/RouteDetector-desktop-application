package com.routedetector.Client;

import com.routedetector.AssetsJPA.Driver;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *  
 * Class used by 'Driver' tab TableView. Driver object does not have assigned vehicle information,
 * for showing vehicle information, new Class is introduced. This class has two fields, 
 * Driver object, and String object for holding vehicle license plate number.
 * 
 * @author Danijel Sudimac
 * 
 */
public class DriverWrapper {
	
	private Driver driver;
	private String currentVehiclePlateNumber;
	
	public DriverWrapper(Driver driver, String currentVehiclePlateNumber){
		this.driver=driver;
		this.currentVehiclePlateNumber = currentVehiclePlateNumber;
	}
	public String getCurrentVehiclePlateNumber() {
		return currentVehiclePlateNumber;
	}
	public void setCurrentVehiclePlateNumber(String currentVehiclePlateNumber) {
		this.currentVehiclePlateNumber = currentVehiclePlateNumber;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public StringProperty getCurrentVehiclePlateNumberProperty() {
		StringProperty s=new SimpleStringProperty(this.currentVehiclePlateNumber);
		return s;
	}

	public void setCurrentVehiclePlateNumberProperty(StringProperty currentVehiclePlateNumber) {
		this.currentVehiclePlateNumber = currentVehiclePlateNumber.get();
	}
}
