package com.routedetector.View;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.Client.DriverWrapper;
import com.routedetector.Client.RmiContainer;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import netscape.javascript.JSObject;
/**
 * This class is in charge for adding web content to application. It is a controller 
 * for MapPane view. It shows Google maps but it may be changed to OpenStreetMaps in future.
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class MapPaneController implements MapCanvas{
	@FXML
    private BorderPane mapView;
	@FXML
    private HBox topBox;
	@FXML
    private VBox rightPanel;
	
	/**Maps of VehicleBox containers e.g. GridPanes and their controllers, so they can be easily added 
	 * or removed from rightPanel.
	*/
	private static Map<String, VehicleBoxController> vehicleBoxControllers=new HashMap<String, VehicleBoxController>();
	private static Map<String, GridPane> vehicleBoxContainers=new HashMap<String, GridPane>();
	
    private WebView webView;
    private WebEngine webEngine;

    private StatusBar statusBar;
    
	public StatusBar getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
	/**
	 * References to Rmi handler class
	 */
	private RmiContainer rmiContainer;
	private Stage motherStage;
	
    public MapPaneController(){
    }

	/**
	 * Initializes reload button.
	 */	
	@FXML
    private void initialize() {
        Button button=new Button();
        button.setTooltip(new Tooltip("Reload map"));
        button.getStyleClass().add("picture-button");
        button.getStyleClass().add("transparent");
        button.getStyleClass().add("reload-button");
		button.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	    		loadMapView();
	        }
	    });
		topBox.getChildren().add(button);
		
	    button.defaultButtonProperty().bind(button.focusedProperty());
	    
		loadMapView();
	}
	/**
	 * Loads html content into WebView.
	 */
	public void loadMapView(){
	    webView = new WebView();
	    webEngine = webView.getEngine();
 	    //webEngine.load(getClass().getResource("/map_resources/googlemap.html").toString());
	    File f=new File("./map_resources/googlemap.html");
 	    try {
			webEngine.load(new URL("file:///" + f.getAbsolutePath()).toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        // process page loading
       webEngine.getLoadWorker().stateProperty().addListener(
           (ObservableValue<? extends State> ov, State oldState, 
               State newState) -> {
                   if (newState == State.SUCCEEDED) {
                       JSObject win
                               = (JSObject) webEngine.executeScript("window");
                       win.setMember("app", new JavaApp());
                   }
       });
 	  
       
	    mapView.setCenter(webView);
	    reloadVehicleBoxes();
	}
	/**
	 *  Handles new vehicle position on main thread.
	 *  
	 *  @param v is new VehiclePostion received
	 */
	public void initializeNewVehiclePositionOnRunLater(VehiclePosition v){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initializeNewVehiclePosition( v);
			}
		});
	}
	/**
	 * Updates Vehicle and Driver information in every VehicleBoxController
	 */
	public void reloadVehicleBoxes(){
		Iterator<Entry<String,VehicleBoxController>> iterator=vehicleBoxControllers.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,VehicleBoxController> entry=iterator.next();
			String imei=entry.getKey();
			VehicleBoxController controller=entry.getValue();
			
			Vehicle vehicle=rmiContainer.getVehicleData().get(imei);
			if(vehicle==null) return;

			controller.updateVehicle(vehicle);
			
			DriverWrapper driverWrapper=rmiContainer.getDriverData().get(vehicle.getCurrentDriver());
			
			Driver driver=null;
			if(driverWrapper!=null) 
				driver=driverWrapper.getDriver();
			
			controller.updateDriver(driver);
			controller.switchOfMarkerButton();
			controller.switchOfLastPositionsButton();
		}
	}
	/** 
	 * Updates VehicleBoxController with new information if exists, if not calls createVehicleBoxPanel 
	 * 
	 * @param v is new VehiclePosition object
	 */
	public void initializeNewVehiclePosition(VehiclePosition v){
		VehicleBoxController controller=vehicleBoxControllers.get(v.getImei());
		if(controller!=null){
			if(!v.getIsConnected())
				this.disableDeviceFromMapWithImei(v.getImei());
			else{
				Vehicle vehicle=rmiContainer.getVehicleData().get(v.getImei());
				Driver driver=null;
				if(vehicle!=null)
					driver=rmiContainer.getDriverData().get(vehicle.getCurrentDriver()).getDriver();
		
				GridPane panel = vehicleBoxContainers.get(v.getImei());
				controller.update( v,vehicle,driver);
				controller.enable();
				if(controller.isShownMarker())
					showPositionOnMap(v, vehicle, driver,false);
				
			    StaticJobs.changeBackgroundColor(panel);
			}
		}
		else{
			createVehicleBoxPanel( v);
			if(!v.getIsConnected())
				this.disableDeviceFromMapWithImei(v.getImei());
				
		}
		setStatusBar("New vehicle postion received");
	}
	public void disableDeviceFromMapWithImei(String imei){
		statusBar.setProgressText("One of the gprs devices is disconnected.");
		if(vehicleBoxContainers.get(imei)!=null){
			vehicleBoxControllers.get(imei).switchOfLastPositionsButton();
			vehicleBoxControllers.get(imei).switchOfMarkerButton();
			vehicleBoxControllers.get(imei).disable();

			removeVehicleFromMap(imei);
			StaticJobs.fadeOutBackground(vehicleBoxContainers.get(imei));
		}
		removeLastVehiclePositions();
	}
	public void disableDeviceFromMapWithImeiOnRunLater(String imei){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				disableDeviceFromMapWithImei(imei);
			}
		});
	}
	public void removeAllOnRunLater(){
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	removeAll();
            }
        });
	}
	public void removeAll(){
		for(Map.Entry<String,VehicleBoxController> entry:vehicleBoxControllers.entrySet()){
			removeVehicleFromMap(entry.getKey());	
		}
		removeLastVehiclePositions();
		rightPanel.getChildren().removeAll(vehicleBoxContainers.values());
		vehicleBoxContainers.clear();
		vehicleBoxControllers.clear();
		setStatusBar("All positions removed from map");
	}
	public void disableAll(){
		for(Map.Entry<String,VehicleBoxController> entry:vehicleBoxControllers.entrySet()){
			disableDeviceFromMapWithImei(entry.getKey());	
		}
	}
	public void disableAllOnRunLater(){
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	disableAll();
            }
        });
	}
	/**
	 * Creates new VehicleBox view and controller.
	 * 
	 * @param v
	 */
	public void createVehicleBoxPanel(VehiclePosition v){
        try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(DriverOverviewController.class.getResource("VehicleBox.fxml"));
			GridPane panel = (GridPane) loader.load();
			VehicleBoxController controller=loader.getController();
			
			Vehicle vehicle=rmiContainer.getVehicleData().get(v.getImei());
			Driver driver=null;
			if(vehicle!=null){
			
				DriverWrapper driverWrapper=rmiContainer.getDriverData().get(vehicle.getCurrentDriver());
			
				if(driverWrapper!=null) 
					driver=driverWrapper.getDriver();
			}
			controller.setMapCanvas(this);
			controller.setMotherWindow(motherStage);
			controller.update(v,vehicle,driver);
			
			rightPanel.getChildren().add(panel);
			vehicleBoxControllers.put(v.getImei(), controller);
			vehicleBoxContainers.put(v.getImei(),panel);
		    StaticJobs.changeBackgroundColor(panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Shows and optionally navigates to marker on map.
	 * 
	 * @param v carries Marker position information
	 * @param vehicle carries vehicle license plate number
	 * @param driver carries driver name and picture
	 * @param navigate true if map should center on marker 
	 */
	public void showPositionOnMap(VehiclePosition v, Vehicle vehicle, Driver driver, boolean navigate){
		String vehicleLicense="unknown";
		String driverName="unknown";
		String imageToString ="null";
		byte[] imageByteArray=null;
		
		if(driver!=null){
			imageByteArray=driver.getImage();
			driverName=driver.getName();
		}
		
		
		if(vehicle!=null){
			vehicleLicense=vehicle.getLicensePlate();
		}

		if(imageByteArray!=null && imageByteArray.length!=0)
			imageToString = Base64.getEncoder().encodeToString(imageByteArray);
			
		if(navigate){
				webEngine.executeScript(" addVehicleAndNavigate('"+v.getImei()+"',"+v.getLat()+","+v.getLon()+","+v.getOrientation()+","+v.getSpeed()+", '"+v.getTime()+"','"+vehicleLicense+"','"+driverName+"','"+imageToString+"');");
		}else{
				webEngine.executeScript("addVehicle('"+v.getImei()+"',"+v.getLat()+","+v.getLon()+","+v.getOrientation()+","+v.getSpeed()+", '"+v.getTime()+"','"+vehicleLicense+"','"+driverName+"','"+imageToString+"');");				
		}
		//webEngine.executeScript("addVehicleAndNavigate(1 , -33.8688,  151.2195, 150 , 30 , '12:50:20', 123456789 , 'Danijel Sudimac',null);");
		statusBar.setProgressText("Vehicle position updated.");
	}
	/**
	 * Removes marker on map.
	 * 
	 * @param imei is gprs id with which marker can be found
	 */
	public void removeVehicleFromMap(String imei){
		webEngine.executeScript("removeVehicle('"+imei+"');");
		//webEngine.executeScript("removeVehicle(1);");
		statusBar.setProgressText("Vehicle position marker removed.");
	}
	/**
	 * Calls RmiContainer for list of VehiclePosition objects.
	 * 
	 * @param imei is gprs id with which marker is identified
	 * @param interval is number of minutes, period for which positions are required
	 */
	public void showLastVehiclePositionsOnRunLater(String imei, int interval){
		setStatusBar("Retrieving positions for "+interval+" minutes interval.");

       	Thread thread=new Thread() {
            public void run() {
        		List<VehiclePosition> positions= rmiContainer.getLastVehiclePositonsOnRunLater(imei ,interval, motherStage);
        		showLastVehiclePositionsOnRunLater(positions, interval);
            }
        };
        thread.setDaemon(true);
        thread.start();
	}
	/**
	 * Shows bunch of markers on map, which represents vehicle's last positions.
	 * 
	 * @param positions is list of VehiclePostions with which marker is identified
	 * @param interval is number of minutes, period for which positions are required
	 */
	public void showLastVehiclePositionsOnRunLater(List<VehiclePosition> positions, int interval){
		if(positions==null || positions.isEmpty()) return;
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
        		for(VehiclePosition p:positions){
        			if(p.getIsConnected())
        				webEngine.executeScript("showLastVehiclePositions('"+p.getImei()+"',"+p.getLat()+","+p.getLon()+","+p.getOrientation()+","+p.getSpeed()+", '"+p.getTime()+"');");		
        		}
        		
        		webEngine.executeScript("showPath();");

        		for(VehiclePosition p:positions){
        			if(p.getIsConnected()){
            			webEngine.executeScript("navigateToLocation("+p.getLat()+","+p.getLon()+");");
            			break;
        			}
        		}
        		setStatusBar("Positions for "+interval+" minutes interval shown.");
            }
        });

	}
	/**Removes all markers that represents last vehicle positions*/
	public void removeLastVehiclePositions(){
		webEngine.executeScript("removeLastVehiclePositions();");
		webEngine.executeScript("hidePath();");
		statusBar.setProgressText("Last vehicle postions removed from map.");
	}
	public RmiContainer getRmiContainer() {
		return rmiContainer;
	}
	public void setRmiContainer(RmiContainer rmiContainer) {
		this.rmiContainer = rmiContainer;
	}
	public Stage getMotherWindow() {
		return motherStage;
	}
	public void setMotherWindow(Stage motherStage) {
		this.motherStage = motherStage;
	}
	private void setStatusBar(String message){
		statusBar.setProgressText(message);		
	}
    // JavaScript interface object
    public class JavaApp {
 
        public void switchOffMarkerButton(String imei) {
        	if(vehicleBoxControllers.get(imei)!=null)
        		vehicleBoxControllers.get(imei).switchOfMarkerButton();
        }	
    }
}
