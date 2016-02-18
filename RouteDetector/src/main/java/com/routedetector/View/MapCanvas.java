package com.routedetector.View;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehiclePosition;

/**
 * Interface with methods for WebView management. WebEngine loads local html file,
 * which shows Map.
 * 
 * @author Danijel Sudimac
 *
 */
public interface MapCanvas {
	public void initializeNewVehiclePositionOnRunLater(VehiclePosition v);
	public void showPositionOnMap(VehiclePosition v, Vehicle vehicle, Driver driver,boolean navigate);
	public void removeVehicleFromMap(String imei);
	public void showLastVehiclePositionsOnRunLater(String imei, int interval);
	public void removeLastVehiclePositions();
	public void disableDeviceFromMapWithImeiOnRunLater(String imei);
	public void removeAllOnRunLater();
	public void removeAll();
	public void disableAll();
}
