package com.routedetector.View;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.Client.SocketContainer;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that acts as controller for VehicleBox view. 
 * 
 * @author  Danijel Sudimac
*/
public class VehicleBoxController {

    @FXML
    private Label licensePlate;
	@FXML
    private Label driverName;
    @FXML
    private Label imei;
    @FXML
    private Label lat;
    @FXML
    private Label lon;
    @FXML
    private Label speed;
    @FXML
    private Label updated;
    @FXML
    private ImageView imageView;
    @FXML
    private ToggleButton showMarkerButton;
    @FXML
    private ToggleButton showLastPositionsButton;
    
    private boolean isShownMarker;
    
    /**Reference to MapPaneController for triggering changes in map view content.*/
    private MapCanvas map;
    
    private VehiclePosition currentPosition;
    
    private Stage motherStage;
    private Driver driver;
    private Vehicle vehicle;
    
    public void disable(){
    	showMarkerButton.setDisable(true);
    }
    public void enable(){
    	showMarkerButton.setDisable(false);
    }
    public void switchOfMarkerButton(){
    	showMarkerButton.setSelected(false);
    	isShownMarker=false;
    }
    public void switchOfLastPositionsButton(){
    	showLastPositionsButton.setSelected(false);
    }
    @FXML
    private void initialize(){
    }
    /**Shows and navigates or hides marker on map.*/
    @FXML
    private void switchShowVehicle(){
    	if(showMarkerButton.isSelected()){
    		isShownMarker=true;
    		map.showPositionOnMap(currentPosition, vehicle, driver,true);
    	}else{
    		map.removeVehicleFromMap(currentPosition.getImei());
    		isShownMarker=false;
    	}
    }
    /**Creates dialog for choosing period, or removes last vehicle positions from map.*/
    @FXML
    private void showLastPositions() throws IOException{
    	if(showLastPositionsButton.isSelected()) {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MapPaneController.class.getResource("LastPositionsDialog.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	
	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Last positions dialog");
	        dialogStage.getIcons().add(new Image("/resources/logo_image.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(motherStage);
	        Scene scene = new Scene(page);
	        scene.getStylesheets().add("/resources/style.css");
	        dialogStage.setScene(scene);
	        dialogStage.setResizable(false);
	        
	        // Set the vehicle into the controller.
	        LastPositionsDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setImei(currentPosition.getImei());
	        controller.setMap(map);
	    	
	        dialogStage.showAndWait();
    	}else{
    		map.removeLastVehiclePositions();
    	}
    }
	public Stage getMotherWindow() {
		return motherStage;
	}
	public void setMotherWindow(Stage motherStage) {
		this.motherStage = motherStage;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate.setText(licensePlate);
	}
	public void setDriverName(String driver) {
		this.driverName.setText(driver);
	}
	public void setImei(String imei) {
		this.imei.setText(imei);
	}
	public void setLat(float lat) {
		this.lat.setText(Float.toString(lat));
	}
	public void setLon(float lon) {
		this.lon.setText(Float.toString(lon));
	}
	public void setSpeed(float speed) {
		this.speed.setText(Float.toString(speed));
	}
	public void setUpdated(Timestamp updated) {
		this.updated.setText(new SimpleDateFormat(SocketContainer.getDatePattern()).format(updated));
	}
	public void setImage(byte[] bytes){
        if(bytes!=null){

        	InputStream in = new ByteArrayInputStream(bytes);
			try {
				BufferedImage bImageFromConvert = ImageIO.read(in);
	        	imageView.setImage(SwingFXUtils.toFXImage(bImageFromConvert, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	public void updateVehicle(Vehicle vehicle){
		this.vehicle=vehicle;
		if(vehicle !=null) this.setLicensePlate(vehicle.getLicensePlate());
	}
	public void updateDriver(Driver driver){
		this.driver=driver;	
		if(driver!=null) this.setDriverName(driver.getName());
	}

    /**
     * Updates vehicle position, vehicle and driver object
     * 
     * @param pos is new VehiclePosition object
     * @param vehicle is associated Vehicle object
     * @param driver is associated Driver object
     */
	public void update( VehiclePosition pos, Vehicle vehicle, Driver driver){
		this.currentPosition=pos;
		this.driver=driver;
		this.vehicle=vehicle;
		
		this.setImei(pos.getImei());
		this.setLat(pos.getLat());
		this.setLon(pos.getLon());
		if(driver!=null){
			this.setDriverName(driver.getName());
			this.setImage(driver.getImage());
		}
		if(vehicle!=null){
			this.setLicensePlate(vehicle.getLicensePlate());
		}
		this.setSpeed(pos.getSpeed());
		Date date=new Date();
		this.setUpdated(new Timestamp(date.getTime()));
	}
	public MapCanvas getMapController() {
		return map;
	}
	public void setMapCanvas(MapCanvas map) {
		this.map = map;
	}
	public boolean isShownMarker() {
		return isShownMarker;
	}
	public void setShownMarker(boolean isShown) {
		this.isShownMarker = isShown;
	}
}
