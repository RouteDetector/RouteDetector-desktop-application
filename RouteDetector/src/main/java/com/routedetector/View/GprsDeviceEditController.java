package com.routedetector.View;


import java.util.Map;


import com.routedetector.Client.GprsDeviceWrapper;
import com.routedetector.JPA.GprsDevice;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Class that acts as controller for gprs device editing view. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class GprsDeviceEditController {

    @FXML
    private TextField imei;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ChoiceBox<String> visibility;
    /**
     * Reference of stage that view is placed into for alert owner setting.
     */
    private Stage dialogStage;
    /**
     * Gprs device object which fields are edited.
     */
    private GprsDevice gprsDevice;
    
    /** Map of existing gprs device objects*/
    private  Map<String, GprsDeviceWrapper> gprsMap;
    
    private boolean saveClicked;

    @FXML
    private void initialize() {
    	saveButton.defaultButtonProperty().bind(saveButton.focusedProperty());
    	cancelButton.defaultButtonProperty().bind(cancelButton.focusedProperty());
    	
    	visibility.setItems(FXCollections.observableArrayList("Private","Public"));
    	visibility.getSelectionModel().select(0);
    }
    /**
     * Sets object's fields from view before saving and closing the stage.
     */
    @FXML
	private void handleSave() {
    	if(this.isInputValid()){
	        gprsDevice.setImei(this.imei.getText().trim());
	        switch(visibility.getSelectionModel().getSelectedIndex()){
		        case 0:
		        	gprsDevice.setIsPublic(false);
		        	break;
		        case 1:
		        	gprsDevice.setIsPublic(true);
		        	break;
		        }
	        saveClicked = true;
	        dialogStage.close();
    	}
	}
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializing view using passed parameters.
     * 
     * @param gprsDevice is selected GprsDevice object
     * @param gprsMap is list of existing GprsDevice objects
     */
    public void setGprsDevice(GprsDevice gprsDevice, Map<String, GprsDeviceWrapper> gprsMap, boolean isNew) {
        this.gprsDevice=gprsDevice;
        this.gprsMap=gprsMap;
        this.imei.setText(gprsDevice.getImei());
        if(gprsDevice.getIsPublic()!=null && gprsDevice.getIsPublic()) 
        	this.visibility.getSelectionModel().select(1);
        this.imei.setDisable(!isNew);
    }
    public boolean isSaveClicked() {
        return saveClicked;
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    /**
     * Checks if view has invalid input.
     * 
     * @return true if its ok.
     */
    private boolean isInputValid() {
        String errorMessage = "";
        
        if(this.imei.getText()==null || this.imei.getText().isEmpty()){
    		this.imei.setStyle("-fx-border-color: red;");
            errorMessage += "Gprs device IMEI must not be null!\n"; 	        	
        }
        if(gprsMap.get(this.imei.getText().trim().toLowerCase())!=null){
    		this.imei.setStyle("-fx-border-color: red;");
            errorMessage += "Gprs device IMEI must be unique!\n"; 	        	
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
