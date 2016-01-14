package com.routedetector.Client;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.DriverGroup;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehicleGroup;
import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.Basic.SecurityServerInterface;
import com.routedetector.JPA.GprsDevice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
* This class is the place where all Rmi calls happens. It saves lists of retrieved data.
* Lists have descriptive names. Some of the methods calls static methods of class StaticJobs to 
* show exception if call fails. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class RmiContainer {
	/**
	* ObservableList fields that contain retrieved information from remote method invocation call.
	*/
 
	private ObservableList<String> fuelTypes;
	private ObservableList<String> vehicleModels;
	private ObservableList<VehicleGroup> vehicleGroups;
	private ObservableList<DriverGroup> driverGroups;
	private ObservableList<String> vehicleManufacturers;
	private ObservableList<String> vehicleTypes;
	private SecurityServerInterface communicator;	
	
	/** Device imei (String object)- VehiclePositionWrapper (list of vehicle positions) map.*/
    private Map<String,VehiclePositionsWrapper> imeiPositionsMap = new HashMap<String,VehiclePositionsWrapper>();
	/** Device imei (String object)- Vehicle map.*/
    private Map<String,Vehicle> imeiVehicleMap = new HashMap<String,Vehicle>();
	/** Driver name (String object)- Vehicle license plate number (String object) map.*/
    private Map<String,String> crewMemberNameVehicleLicenseMap = new HashMap<String,String>();
	/** Device imei (String object)- GprsDeviceWrapper map.*/
    private Map<String,GprsDeviceWrapper> imeiDeviceMap = new HashMap<String,GprsDeviceWrapper>();
	/** Driver name (String object)- DriverWrapper map.*/
    private Map<String, DriverWrapper> nameDriverWrapperMap = new HashMap<String,DriverWrapper>(); 
	
	/** References to Tab object are used to show blinking animation on saving new entries.	*/
	private Tab vehicleTab;
    private Tab driverTab;
    private Tab gprsTab;

    public Tab getGprsTab() {
		return gprsTab;
	}
	public void setGprsTab(Tab gprsTab) {
		this.gprsTab = gprsTab;
	}
	public Tab getVehicleTab() {
		return vehicleTab;
	}
	public void setVehicleTab(Tab vehicleTab) {
		this.vehicleTab = vehicleTab;
	}
	public Tab getDriverTab() {
		return driverTab;
	}
	public void setDriverTab(Tab driverTab) {
		this.driverTab = driverTab;
	}
	public void setVehicleData(Map<String, Vehicle> licenseVehicleMap) {
		this.imeiVehicleMap = licenseVehicleMap;
	}
	public void setDriverData(Map<String,DriverWrapper> nameDriverWrapperMap) {
		this.nameDriverWrapperMap = nameDriverWrapperMap;
	}
	public ObservableList<String> getVehicleTypes() {
		return vehicleTypes;
	}
	public void setVehicleTypes(ObservableList<String> vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}
	public ObservableList<String> getFuelTypes() {
		return fuelTypes;
	}
	public void setFuelTypes(ObservableList<String> fuelTypes) {
		this.fuelTypes = fuelTypes;
	}
	public ObservableList<String> getVehicleModels() {
		return vehicleModels;
	}
	public void setVehicleModels(ObservableList<String> vehicleModels) {
		this.vehicleModels = vehicleModels;
	}
	public ObservableList<VehicleGroup> getVehicleGroups() {
		return vehicleGroups;
	}
	public void setVehicleGroups(ObservableList<VehicleGroup> vehicleGroups) {
		this.vehicleGroups = vehicleGroups;
	}
	public ObservableList<String> getVehicleManufacturers() {
		return vehicleManufacturers;
	}
	public void setVehicleManufacturers(ObservableList<String> vehicleManufacturers) {
		this.vehicleManufacturers = vehicleManufacturers;
	}
	public void setDriverGroups(ObservableList<DriverGroup> driverGroups) {
		this.driverGroups = driverGroups;
	}	
	public SecurityServerInterface getCommunicator() {
		return communicator;
	}
	public void setCommunicator(SecurityServerInterface communicator) {
		this.communicator = communicator;
	}
    public Map<String, Vehicle> getVehicleData() {
        return imeiVehicleMap;
    }
    public Map<String,DriverWrapper> getDriverData() {
        return nameDriverWrapperMap ;
    }
    public Map<String,GprsDeviceWrapper> getGprsData() {
        return imeiDeviceMap ;
    }
	/** Retrieves list of Vehicle objects, updates maps or shows alert if it fails.
	 * 
	 * @param stage object type Stage to be used as parent of exception Alert .
	*/
	public void setVehicleInfo(Stage stage){
    	try{
    		CollectionManipulation.setVehicleMapsFromCollection(crewMemberNameVehicleLicenseMap, imeiVehicleMap, communicator.getVehicles());
    	}catch(Exception e){
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    	}        	
    }
	/** Retrieves list of Driver objects, puts them in map or shows alert if it fails.
	 * 
	 * @param stage object type Stage to be used as parent of exception Alert .
	*/
	public void setDriverInfo(Stage stage){
    	try{
    		nameDriverWrapperMap.clear();
    		nameDriverWrapperMap.putAll(CollectionManipulation.getDriverWrapperMapFromCollection(crewMemberNameVehicleLicenseMap, communicator.getDrivers()));
    	}catch(Exception e){
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    	}        	
    }
	/** Retrieves list of GprsDevice objects which are bounded to user's company email, puts them in map or shows alert if it fails.
	 * 
	 * @param stage object type Stage to be used as parent of exception Alert .
	*/
	public void setGprsDevicesInfo(Stage stage){
    	try{
    		imeiDeviceMap.clear();
    		imeiDeviceMap.putAll(CollectionManipulation.getGPRSDeviceWrapperMapFromCollection(imeiVehicleMap, communicator.getAllGprsDevices()));
    	}catch(Exception e){
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    	}        	
    }
	/** Retrieves list of List<Object> objects. First of them contains Strings of fuel type names,
	 * second vehicle type names, third names of biggest vehicle manufacturers, forth model names, 
	 * and last two VehicleGroup and DriverGroup objects respectively.On failure it shows alert.
	 * 
	 * @param stage object type Stage to be used as parent of exception Alert .
	*/
	@SuppressWarnings("unchecked")
	public void setTypesInfo(Stage stage){
    	try{
    		List<Object> list=communicator.getDropDowns();
    		if(list==null)	return;
    		
    		fuelTypes=FXCollections.observableArrayList((List<String>)list.get(0));
    		vehicleTypes=FXCollections.observableArrayList((List<String>)list.get(1));
    		vehicleManufacturers=FXCollections.observableArrayList((List<String>)list.get(2));
    		vehicleModels=FXCollections.observableArrayList((List<String>)list.get(3));
    		vehicleGroups=FXCollections.observableArrayList((List<VehicleGroup>)list.get(4));
    		driverGroups=FXCollections.observableArrayList((List<DriverGroup>)list.get(5));
    	}catch(Exception e){
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    	}        	
    }
	/** Removes Vehicle object from server database.
	 * 
	 * @param Vehicle object to be removed.
	 * @param Stage object to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String removeVehicleOnRunLater(Vehicle v,Stage stage){
    	try{
	    	if(communicator.removeVehicle(v)){
	    		return "Vehicle information removed";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist vehicle information!",stage);
	    		return "Failed to remove vehicle information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to remove vehicle information!";
	    }
    }
	/** Saves Vehicle object on server database.
	 * 
	 * @param v object of type Vehicle to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String persistVehicleOnRunLater(Vehicle v,Stage stage){
    	try{
	    	if(communicator.persistVehicle(v)){
	    		Image image = new Image("/resources/information.png");
	    		StaticJobs.addBlinkingAnimationToTabOnRunLater(new ImageView(image), vehicleTab);

	    		return "Vehicle information saved";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist vehicle information!",stage);
	    		return "Failed to persist vehicle information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist vehicle information!";
	    }
    }
	/** Saves GprsDevice object on server database.
	 * 
	 * @param d object of type GprsDevice to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String persistGprsDeviceOnRunLater(GprsDevice d,Stage stage){
    	try{
	    	if(communicator.persistGprsDevice(d)){
	    		Image image = new Image("/resources/information.png");
	    		StaticJobs.addBlinkingAnimationToTabOnRunLater(new ImageView(image), gprsTab);

	    		return "Gprs device information saved";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist Gprs device information!",stage);
	    		return "Failed to persist Gprs device information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist vehicle information!";
	    }
    }
	/** Saves changes to Vehicle object on server database.
	 * 
	 * @param v object of type Vehicle to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String mergeVehicleOnRunLater(Vehicle v,Stage stage){
    	try{
	    	if(communicator.updateVehicle(v)){
	    		return "Vehicle information saved.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist vehicle information!",stage);
	    		return "Failed to persist vehicle information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist vehicle information!";
	    }
    }
	/** Removes Driver object from server database.
	 * 
	 * @param d object of type Driver to be removed.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String removeDriverOnRunLater(Driver d,Stage stage){
    	try{
    		String licPlate=crewMemberNameVehicleLicenseMap.get(d.getName());
    		
    		for(Map.Entry<String,Vehicle> entry: imeiVehicleMap.entrySet()){
    			Vehicle v=(Vehicle) entry.getValue();
    			if(v.getLicensePlate().equals(licPlate)){
        			if(v.getCurrentDriver().equals(d.getName())){
        				v.setCurrentDriver(null);
        				this.communicator.updateVehicle(v);
        				break;
        			}
        			if(v.getCurrentCoDriver().equals(d.getName())){
        				v.setCurrentCoDriver(null);
        				this.communicator.updateVehicle(v);
        				break;
        			}
    			}
    		}
    		
	    	if(communicator.removeDriver(d)){
	    		return "Driver information removed.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to remove driver information!",stage);
	    		return "Failed to remove driver information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to remove driver information!";
	    }
    }
	/** Removes GprsDevice object from server database.
	 * 
	 * @param d object of type GprsDevice to be removed.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String removeGprsOnRunLater(GprsDevice d,Stage stage){
    	try{
    		if(imeiVehicleMap.get(d.getImei())!=null){
	    		Vehicle vehicle=imeiVehicleMap.get(d.getImei());
	    		vehicle.setTrackingId(null);
	    		this.communicator.updateVehicle(vehicle);
    		}
	    	if(communicator.removeGprsDevice(d)){
	    		return "Gprs device information removed.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to remove Gprs device information!",stage);
	    		return "Failed to remove Gprs device information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to remove Gprs device information!";
	    }
    }
	/** Saves Driver object on server database.
	 * 
	 * @param d object of type Driver to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String persistDriverOnRunLater(Driver d,Stage stage){
    	try{
	    	if(communicator.persistDriver(d)){
	    		Image image = new Image("/resources/information.png");
	    	    StaticJobs.addBlinkingAnimationToTabOnRunLater(new ImageView(image), driverTab);
	    		return "Driver information saved.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist driver information!",stage);
	    		return "Failed to persist driver information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist driver information!";
	    }
    }
	/** Saves changes to Driver object on server database.
	 * 
	 * @param d object of type Driver to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String mergeDriverOnRunLater(Driver d,Stage stage){
    	try{
	    	if(communicator.updateDriver(d)){
	    		return "Driver information saved.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist driver information!",stage);
	    		return "Failed to persist driver information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist driver information!";
	    }
    }
	/** Saves changes to GprsDevice object on server database.
	 * 
	 * @param d object of type GprsDevice to be saved.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String mergeGprsDeviceOnRunLater(GprsDevice d,Stage stage){
    	try{
	    	if(communicator.updateGprsDevice(d)){
	    		return "Gprs device information saved.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist driver information!",stage);
	    		return "Failed to persist driver information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist driver information!";
	    }
    }
	/** Removes DriverGroup object from server database.
	 * 
	 * @param d object of type DriverGroup to be removed.
	 * @param stage object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String removeDriverGroupOnRunLater(DriverGroup d,Stage stage){
    	try{
	    	if(communicator.removeDriverGroup(d)){
	    		return "Driver department information removed.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to remove driver department information!",stage);
	    		return "Failed to remove driver department information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist driver information!";
	    }
    }
	/** Saves DriverGroup object on server database.
	 * 
	 * @param d object of type DriverGroup to be saved.
	 * @param stage  object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String persistDriverGroupOnRunLater(DriverGroup d,Stage stage){
    	try{
	    	if(communicator.persistDriverGroup(d)){
	    		return "Driver department information saved.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to persist driver department information!",stage);
	    		return "Failed to persist driver department information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to persist driver information!";
	    }
    }
	/** Saves changes to DriverGroup object on server database.
	 * 
	 * @param d object of type DriverGroup to be saved.
	 * @param stage  object of type Stage to be used as parent of exception Alert .
	 * @return String comment to be shown in status bar.
	*/
    public String mergeDriverGroupOnRunLater(DriverGroup d ,Stage stage){
    	try{
	    	if(communicator.updateDriverGroup(d)){
	    		return "Driver department information updated.";
	    	}else{
	    		StaticJobs.showProcessAlertOnRunLater("Failed to update driver department information!",stage);
	    		return "Failed to update driver department information!";
	    	}  
    	}catch(RemoteException e){
	    	StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return "Failed to update driver information!";
	    }
    }
	/** Retrieves last vehicle positions from server database.
	 * 
	 * @param imei is Device's id.
	 * @param minutes is time interval for which positions will be retrieved.
	 * @param stage is Stage object, parent of Alert.
	 * @return List of VehiclePositions.
	*/
    public List<VehiclePosition> getLastVehiclePositonsOnRunLater(String imei ,int minutes, Stage stage){
    	try{
    		if(minutes<30)
    			return communicator.findVehiclePositionsForPeriod( getOwnerCompanyEmailFromImei(imei) , imei, minutes);
    		else if(minutes<120)
    			return communicator.findVehiclePositionsForPeriodWithSkipping( getOwnerCompanyEmailFromImei(imei) , imei, minutes,2);
    		else if(minutes<720)
    			return communicator.findVehiclePositionsForPeriodWithSkipping( getOwnerCompanyEmailFromImei(imei) , imei, minutes,3);
    		else 
    			return getVehiclePositionsForWholePeriod(imei, stage);
    	}catch(Exception e){
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return null;
    	} 
    }
	/** Retrieves last vehicle positions from server database for period of two days.
	 * If method is already called in last one minute, earlier filled list will be 
	 * returned, and RMI will not be called.
	 * 
	 * @param imei is Device's id.
	 * @param stage is Stage object, parent of Alert.
	 * @return List of VehiclePositions.
	*/
    public List<VehiclePosition> getVehiclePositionsForWholePeriod(String imei, Stage stage){

    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -1);
    	if(imeiPositionsMap.get(imei)!=null && imeiPositionsMap.get(imei).getTime().after(cal.getTime()))
    		return imeiPositionsMap.get(imei).getVehiclePositions();
    	try {
 
			List<VehiclePosition> list= communicator.findVehiclePositionsForPeriodWithSkipping(getOwnerCompanyEmailFromImei(imei), imei, 2880, 5);

			Date d=new Date();
			VehiclePositionsWrapper wrapper=new VehiclePositionsWrapper(d,list);
			imeiPositionsMap.clear();
			imeiPositionsMap.put(imei, wrapper);
			return list;
		} catch (RemoteException e) {
    		StaticJobs.showExceptionAlertOnRunLater(e,stage);
    		return null;
		}
    }
    private String getOwnerCompanyEmailFromImei(String imei){
    	return imeiDeviceMap.get(imei).getGprsDevice().getCompanyEmail();
    }
    public ObservableList<VehicleGroup> getVehicleGroup(){
    	return vehicleGroups;
    }
    public ObservableList<DriverGroup> getDriverGroups(){
    	return driverGroups;
    }
}
