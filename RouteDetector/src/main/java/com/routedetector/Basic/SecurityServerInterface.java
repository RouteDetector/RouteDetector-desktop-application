package com.routedetector.Basic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.DriverGroup;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.JPA.GprsDevice;
/**
 * 
 * @author Danijel Sudimac
 * 
 * This interface is in charge for RMI communication with server.
 *
 */
public interface SecurityServerInterface extends Remote{
	public List<Vehicle> getVehicles() throws RemoteException;
	public List<Driver> getDrivers() throws RemoteException;
	public List<Object> getDropDowns() throws RemoteException;
	public List<GprsDevice> getAllGprsDevices() throws RemoteException;
	public List<VehiclePosition> findVehiclePositionsForPeriod(String schema, String imei, int minutes) throws RemoteException;
	public List<VehiclePosition> findVehiclePositionsForPeriodWithSkipping(String companyEmail, String imei, int minutes, int step) throws RemoteException;
	public boolean persistVehicle(Vehicle v) throws RemoteException;
	public boolean removeVehicle(Vehicle v) throws RemoteException;
	public boolean updateVehicle(Vehicle v) throws RemoteException;
	public boolean persistDriver(Driver d) throws RemoteException;
	public boolean removeDriver(Driver d) throws RemoteException;
	public boolean updateDriver(Driver d) throws RemoteException;
	public boolean persistGprsDevice(GprsDevice d) throws RemoteException;
	public boolean removeGprsDevice(GprsDevice d) throws RemoteException;
	public boolean updateGprsDevice(GprsDevice d) throws RemoteException;
	public boolean persistDriverGroup(DriverGroup d) throws RemoteException;
	public boolean updateDriverGroup(DriverGroup d) throws RemoteException;
	public boolean removeDriverGroup(DriverGroup d) throws RemoteException;
	public String getSchema() throws RemoteException;
}
