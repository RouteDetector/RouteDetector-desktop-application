package com.routedetector.View;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * Class that acts as controller for last positions dialog. It offers user to select time interval
 * for which VehiclePosition objects will be requested from the server.
 * 
 * @author Danijel Sudimac
 *
 */
public class LastPositionsDialogController {

   Stage dialogStage;
   
   /**Reference to MapPaneController for triggering changes in map view content.*/
   private MapCanvas map;
   
   private List<String> listOfItems;
   
   @FXML
   private Label imei;
   @FXML
   private ChoiceBox<String> period;
   @FXML
   private Button showButton;
   @FXML
   private Button cancelButton;
   
   @FXML
   private void initialize(){
	   listOfItems=new ArrayList<String>();
	   listOfItems.add("5 minutes.");
	   listOfItems.add("15 minutes.");
	   listOfItems.add("30 minutes.");
	   listOfItems.add("hour.");
	   listOfItems.add("2 hours.");
	   listOfItems.add("6 hours.");
	   listOfItems.add("12 hours.");
	   listOfItems.add("day.");
	   listOfItems.add("2 days.");

	   showButton.defaultButtonProperty().bind(showButton.focusedProperty());
	cancelButton.defaultButtonProperty().bind(cancelButton.focusedProperty());
	    
	   period.setItems(FXCollections.observableArrayList(listOfItems));
	   period.getSelectionModel().select(0);
   } 
   /** Calls method from map canvas to show last positions */
   @FXML
   private void show(){
	   map.showLastVehiclePositionsOnRunLater(imei.getText(), getPeriod());
	   dialogStage.close();
   };
   @FXML
   private void exit(){
	   dialogStage.close();
   };	
   public void setDialogStage(Stage dialogStage) {
	   this.dialogStage = dialogStage;
   }
   public void setImei(String imei) {
	   this.imei.setText(imei);
   }
   public MapCanvas getMap() {
	   return map;
   }
   public void setMap(MapCanvas map) {
	   this.map = map;
   }
   public int getPeriod() {
	   switch(period.getSelectionModel().getSelectedItem()){
		   case "5 minutes.":
			   return 5;
		   case "15 minutes.":
			   return 15;
		   case "30 minutes.":
			   return 30;
		   case "hour.":
			   return 60;
		   case "2 hours.":
			   return 120;
		   case "6 hours.":
			   return 360;
		   case "12 hours.":
			   return 720;
		   case "day.":
			   return 1440;
		   case "2 days.":
			   return 2880;
		   default:
			   return 5;
	   }
   }
}
