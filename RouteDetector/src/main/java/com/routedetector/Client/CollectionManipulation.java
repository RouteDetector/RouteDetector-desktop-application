package com.routedetector.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehicleGroup;
import com.routedetector.JPA.GprsDevice;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * This class consists exclusively of static methods that operate on or return
 * collections.
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class CollectionManipulation{
    /**
    * Creates new ObservableList of Strings from the specified list.
    * Depending of Object type contained in List, String object is for Vehicle object, 
    * license plate number,
    * for Driver is driver name, for DriverGroup group name
    *
    * @param listOfItems is the ObservableList to be searched.
    * @return ObservableList of String objects
    */
	public static ObservableList<String> getListOfStringFromCollection(ObservableList<?> listOfItems){
		List<String> list = new ArrayList<String>();
	    ObservableList<String> observableList = FXCollections.observableList(list);
		Iterator<?> iterator=listOfItems.iterator();
		
		while(iterator.hasNext()){
			Object o=iterator.next();
			if(o instanceof Vehicle){
				observableList.add(((Vehicle)o).getLicensePlate());
			}
			if(o instanceof Driver){
				observableList.add(((Driver)o).getName());
			}
			if(o instanceof VehicleGroup){
				observableList.add(((VehicleGroup)o).getName());
			}
		}
		return observableList;
	}
	/**
	 * Creates map of DriverWrapper objects which are used to populate Driver table on Driver tab.
	 * 
	 * @param crewMemberNameVehicleLicense map of driver names and assigned vehicles.
	 * @param listOfDrivers driver list
	 * @return map of DriverWrapper objects
	 */
	public static Map<String,DriverWrapper> getDriverWrapperMapFromCollection(Map<String,String> crewMemberNameVehicleLicense , List<Driver> listOfDrivers){
		Map<String,DriverWrapper> map = new HashMap<String,DriverWrapper>();

		Iterator<Driver> iteratorOne=listOfDrivers.iterator();
		
		while(iteratorOne.hasNext()){
			Driver d=iteratorOne.next();
			DriverWrapper wrapp=new DriverWrapper(d,crewMemberNameVehicleLicense.get(d.getName()));
			map.put(d.getName(), wrapp);
		}
		return map;
	}
	/**
	 * Creates map of GprsDeviceWrapper objects which are used to populate Gprs devices table on Gprs tab.
	 * 
	 * @param imeiVehicle map of imei Strings and vehicles assigned to device with that imei
	 * @param listOfDevices
	 * @return map of GprsDeviceWrapper object
	 */
	public static Map<String,GprsDeviceWrapper> getGPRSDeviceWrapperMapFromCollection(Map<String,Vehicle> imeiVehicle , List<GprsDevice> listOfDevices){
		Map<String,GprsDeviceWrapper> map = new HashMap<String,GprsDeviceWrapper>();

		Iterator<GprsDevice> iteratorOne=listOfDevices.iterator();
		while(iteratorOne.hasNext()){
			GprsDevice d=iteratorOne.next();
			GprsDeviceWrapper wrapp=new GprsDeviceWrapper(d,null,null, null);
			if(imeiVehicle.get(d.getImei())!=null){
				wrapp.setVehicleLicensePlate(imeiVehicle.get(d.getImei()).getLicensePlate());
				wrapp.setDriverName(imeiVehicle.get(d.getImei()).getCurrentDriver());
				wrapp.setCodriverName(imeiVehicle.get(d.getImei()).getCurrentCoDriver());
			}
			map.put(d.getImei(), wrapp);
		}
		return map;
	}
	/**
	 * Updates crew member-assigned vehicle and imei-assigned vehicle maps.
	 * 
	 * @param crewMemberNameVehicleLicense is crew member-assigned vehicle map
	 * @param imeiVehicle is imei-assigned vehicle map
	 * @param listOfVehicles is list of Vehicle object
	 */
	public static void setVehicleMapsFromCollection(Map<String,String> crewMemberNameVehicleLicense, Map<String,Vehicle> imeiVehicle, List<Vehicle> listOfVehicles){
		imeiVehicle.clear();
		crewMemberNameVehicleLicense.clear();
		
		Iterator<Vehicle> iteratorOne=listOfVehicles.iterator();

		while(iteratorOne.hasNext()){
			Vehicle v=iteratorOne.next();
			if(v.getTrackingId()!=null) imeiVehicle.put(v.getTrackingId().trim(), v);
			
			else imeiVehicle.put(null, v);
					
			if(v.getCurrentDriver()!=null){
				crewMemberNameVehicleLicense.put(v.getCurrentDriver(), v.getLicensePlate());
			}
			if(v.getCurrentCoDriver()!=null){
				crewMemberNameVehicleLicense.put(v.getCurrentCoDriver(), v.getLicensePlate());
			}
		}
	}
	/**
	 * Creates list of names of Drivers who have not been assigned to any or is assigned to subject vehicle.
	 * 
	 * @param mapOfItems map of DriverWrapper objects
	 * @param vehicleLicensePlateNumber vehicle license plate number 
	 * @return list of driver names
	 */
	public static List<String> getListOfFreeDriverNamesFromMap(Map<String,DriverWrapper> mapOfItems, String vehicleLicensePlateNumber){
		List<String> list = new ArrayList<String>();
		
		for(Map.Entry<String, DriverWrapper> entry: mapOfItems.entrySet()){
			DriverWrapper d=entry.getValue();
			
			if(d.getCurrentVehiclePlateNumber()==null || d.getCurrentVehiclePlateNumber().equals(vehicleLicensePlateNumber)|| d.getCurrentVehiclePlateNumber().isEmpty())
				list.add(d.getDriver().getName());			
		}
		return list;
	}
	/**
	 * Creates list of imei String objects which are not assigned to any or is assigned to subject vehicle.
	 * 
	 * @param imeiDeviceMap map of GprsDeviceWrapper objects
	 * @param vehicleLicensePlateNumber is licensePlateNumber of vehicle
	 * @return list of imei String objects
	 */
	public static List<String> getListOfFreeGprsDevicesImeiFromMap(Map<String,GprsDeviceWrapper> imeiDeviceMap, String vehicleLicensePlateNumber){
		List<String> list = new ArrayList<String>();
		
		for(Map.Entry<String, GprsDeviceWrapper> entry: imeiDeviceMap.entrySet()){
			GprsDeviceWrapper wrapper=entry.getValue();
			
			if(wrapper.getVehicleLicensePlate()==null || wrapper.getVehicleLicensePlate().equals(vehicleLicensePlateNumber)|| wrapper.getVehicleLicensePlate().isEmpty())
				list.add(wrapper.getGprsDevice().getImei());			
		}
		return list;
	}
}
