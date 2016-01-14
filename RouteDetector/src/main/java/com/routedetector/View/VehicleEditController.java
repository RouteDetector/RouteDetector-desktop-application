package com.routedetector.View;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehicleGroup;
import com.routedetector.Client.AutoCompleteTextListener;
import com.routedetector.Client.CollectionManipulation;
import com.routedetector.Client.DriverWrapper;
import com.routedetector.Client.GprsDeviceWrapper;
import com.routedetector.Client.StaticJobs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;;

/**
 * Class that acts as controller for vehicle editing view. 
 * 
 * @author  Danijel Sudimac
*/
public class VehicleEditController {

    @FXML
    private TextField trackingId;
    @FXML
    private TextField licPlate;
    @FXML
    private TextField engineNumber;
    @FXML
    private TextField currentDriver;
    @FXML
    private TextField currentCoDriver;
    @FXML
    private ChoiceBox<String> fuelType;
    @FXML
    private ChoiceBox<String> vehicleType;
    @FXML
    private TextField vehicleManufacturer;
    @FXML
    private TextField vehicleModel;
    @FXML
    private TextField engineType;
    @FXML
    private TextField yearOfMake;
    @FXML
    private DatePicker acquisitionDate;
    @FXML
    private TextField vehicleGroup;
    @FXML
    private TextField odometer;
    @FXML
    private TextField chronometer;
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private TextField lenght;
    @FXML
    private TextField palletNumber;
    @FXML
    private TextField payloadWeight;
    @FXML
    private TextField curbWeight;
    @FXML
    private TextField grossWeight;
    @FXML
    private CheckBox doubleDeck;
    @FXML
    private TextField costPerKm;
    @FXML
    private TextField costPerHour;
    @FXML
    private TextField literPer100Km;
    @FXML
    private TextField literPerHour;
    @FXML
    private TextField fuelTankCapacity;
    @FXML
    private TextField numberOfSecuringStraps;
    @FXML
    private TextArea otherEquipment;
    @FXML
    private CheckBox palletCarrier;
    @FXML
    private CheckBox meetRail;
    @FXML
    private CheckBox refrigerated;
    @FXML
    private TextField country;
    @FXML
    private TextField continent;
    @FXML
    private TextField ownership;
    @FXML
    private TextArea moreInfoFieldOne;
    @FXML
    private TextArea moreInfoFieldTwo;
    @FXML
    private RadioButton editableButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    /**
     * Reference of stage that view is placed into for alert owner setting.
     */
    private Stage dialogStage;
    /**
     *Vehicle object which fields are edited.
     */
    private Vehicle vehicle;

    private boolean saveClicked;
    private boolean isNew;
    
    /** Lists and maps for populating pop-ups on auto-complete fields*/
    private List<String> freeDrivers;
    private List<String> freeGprsDevices;
    private Map<String, Vehicle> vehicleList;
    
    private DecimalFormat decimalFormat = new DecimalFormat();
    
    /**
     * Sets object's fields from view before saving and closing the stage.
     */
    @FXML
	private void handleSave() {
	    if (isInputValid()) {
	        vehicle.setVehicleType(this.vehicleType.getSelectionModel().getSelectedItem());
	        vehicle.setVehicleModel(this.vehicleModel.getText().trim());
	        vehicle.setVehicleManufacturer(this.vehicleManufacturer.getText().trim());
	        vehicle.setVehicleGroup(this.vehicleGroup.getText().trim());

	        vehicle.getFuelInfo().setFuelType(this.fuelType.getSelectionModel().getSelectedItem());
	        vehicle.getOtherVehicleInfo().setContinent(this.continent.getText().trim());
	        vehicle.getOtherVehicleInfo().setCountry(this.country.getText().trim());
	        vehicle.setCurrentCoDriver(this.currentCoDriver.getText().trim());
	        vehicle.setCurrentDriver(this.currentDriver.getText().trim());

	        vehicle.getVehicleLoadspace().setDoubleDeck(this.doubleDeck.isSelected());
	        vehicle.setEngineNumber(this.engineNumber.getText().trim());
	        vehicle.setEngineType(this.engineType.getText().trim());
	        vehicle.setLicensePlate( this.licPlate.getText().trim());
	        vehicle.getVehicleEquipment().setMeetRail(this.meetRail.isSelected());
	        vehicle.getOtherVehicleInfo().setField1(this.moreInfoFieldOne.getText().trim());
	        vehicle.getOtherVehicleInfo().setField2(this.moreInfoFieldTwo.getText().trim());
	        vehicle.getVehicleEquipment().setOtherEquipment(this.otherEquipment.getText().trim());
	        vehicle.getOtherVehicleInfo().setOwnership(this.ownership.getText().trim());
	        vehicle.getVehicleEquipment().setPalletCarrier(this.palletCarrier.isSelected());
	        vehicle.getVehicleEquipment().setRefrigerated(this.refrigerated.isSelected());
	        vehicle.setTrackingId(this.trackingId.getText().trim());
	        if(this.acquisitionDate.getValue()!=null)vehicle.setAcquisitionDate(Date.from(this.acquisitionDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

	        saveClicked = true;
	        dialogStage.close();
	    }
	}
    /**
     * Adds listener to Radio Button.
     */
	@FXML
    private void initialize() {
		saveButton.defaultButtonProperty().bind(saveButton.focusedProperty());
		cancelButton.defaultButtonProperty().bind(cancelButton.focusedProperty());
		
		editableButton.setOnAction(new EventHandler<ActionEvent>() {
			 
			public void handle(ActionEvent e) { 
				if(editableButton.isSelected()) setEditable(true);
				else setEditable(false);
			}
		});
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    /**
     * Initializing view using passed parameters.
     * 
     * @param vehicle is selected Vehicle object
     * @param fuelTypes is list of fuel type names
     * @param vehicleTypes is list of vehicle type names
     * @param vehicleManufacturers is list of manufacturer names
     * @param vehicleModels is list of model names
     * @param vehicleGroups is list of all VehicleGroup objects
     * @param vehicleList map of Vehicle objects for finding not-assigned drivers
     * @param gprsDevices is map of GprsDevicesWrapper objects for finding not-assigned grps devices
     * @param isNew is for setting editable button on/off
     */
    public void setVehicle(Vehicle vehicle, ObservableList<String> fuelTypes, ObservableList<String> vehicleTypes, ObservableList<String> vehicleManufacturers, ObservableList<String> vehicleModels, ObservableList<VehicleGroup> vehicleGroups, Map<String, Vehicle> vehicleList, Map<String, GprsDeviceWrapper> gprsDevices, Map<String,DriverWrapper> driverWrappers,boolean isNew) {
    	this.vehicle=vehicle;
    	this.vehicleList=vehicleList;
        this.vehicleType.setItems(vehicleTypes);
        if(vehicle.getVehicleType()!=null) this.vehicleType.getSelectionModel().select(vehicle.getVehicleType());
        if(vehicle.getVehicleModel()!=null) this.vehicleModel.setText(vehicle.getVehicleModel());
        if(vehicle.getVehicleManufacturer()!=null) this.vehicleManufacturer.setText(vehicle.getVehicleManufacturer());
        if(vehicle.getVehicleGroup()!=null) this.vehicleGroup.setText(vehicle.getVehicleGroup());
        this.fuelType.setItems(fuelTypes);
        if(vehicle.getFuelInfo().getFuelType()!=null) this.fuelType.getSelectionModel().select(vehicle.getFuelInfo().getFuelType());
        if(vehicle.getChronometerH()!=null) this.chronometer.setText(String.valueOf(vehicle.getChronometerH()));
        if(vehicle.getOtherVehicleInfo().getContinent()!=null) this.continent.setText(vehicle.getOtherVehicleInfo().getContinent());
        if(vehicle.getVehicleCost().getCostPerH()!=null) this.costPerHour.setText(String.valueOf(vehicle.getVehicleCost().getCostPerH()));
        if(vehicle.getVehicleCost().getCostPerKm()!=null) this.costPerKm.setText(String.valueOf(vehicle.getVehicleCost().getCostPerKm()));
        if(vehicle.getOtherVehicleInfo().getCountry()!=null) this.country.setText(vehicle.getOtherVehicleInfo().getCountry());
        if(vehicle.getVehicleLoadspace().getCurbWeightKg()!=null) this.curbWeight.setText(String.valueOf(vehicle.getVehicleLoadspace().getCurbWeightKg()));
        if(vehicle.getCurrentCoDriver()!=null) this.currentCoDriver.setText(vehicle.getCurrentCoDriver());
        if(vehicle.getCurrentDriver()!=null) this.currentDriver.setText(vehicle.getCurrentDriver());
        if(vehicle.getTrackingId()!=null) this.trackingId.setText(vehicle.getTrackingId());
        
        new AutoCompleteTextListener(this.vehicleModel,vehicleModels);
        new AutoCompleteTextListener( this.vehicleManufacturer,vehicleManufacturers);
        new AutoCompleteTextListener( this.vehicleGroup, CollectionManipulation.getListOfStringFromCollection(vehicleGroups));
 
        freeGprsDevices = CollectionManipulation.getListOfFreeGprsDevicesImeiFromMap(gprsDevices, vehicle.getLicensePlate());
        freeDrivers = CollectionManipulation.getListOfFreeDriverNamesFromMap(driverWrappers, vehicle.getLicensePlate());
        new AutoCompleteTextListener( this.currentCoDriver,FXCollections.observableArrayList(freeDrivers));
        new AutoCompleteTextListener( this.currentDriver,FXCollections.observableArrayList(freeDrivers));
        new AutoCompleteTextListener(this.trackingId,FXCollections.observableArrayList(freeGprsDevices));

        if(vehicle.getVehicleLoadspace().getDoubleDeck()!=null) this.doubleDeck.setSelected(vehicle.getVehicleLoadspace().getDoubleDeck());
        if(vehicle.getEngineNumber()!=null) this.engineNumber.setText(vehicle.getEngineNumber());
        if(vehicle.getEngineType()!=null) this.engineType.setText(vehicle.getEngineType());
        if(vehicle.getFuelInfo().getFuelTankCapacity()!=null) this.fuelTankCapacity.setText(String.valueOf(vehicle.getFuelInfo().getFuelTankCapacity()));
        if(vehicle.getVehicleLoadspace().getGrossWeightKg()!=null) this.grossWeight.setText(String.valueOf(vehicle.getVehicleLoadspace().getGrossWeightKg()));
        if(vehicle.getVehicleLoadspace().getHeightCm()!=null) this.height.setText(String.valueOf(vehicle.getVehicleLoadspace().getHeightCm()));
        if(vehicle.getVehicleLoadspace().getLenghtCm()!=null) this.lenght.setText(String.valueOf(vehicle.getVehicleLoadspace().getLenghtCm()));
        if(vehicle.getLicensePlate()!=null)  this.licPlate.setText(vehicle.getLicensePlate());
        if(vehicle.getFuelInfo().getConsumptionLPerKm()!=null) this.literPer100Km.setText(String.valueOf(vehicle.getFuelInfo().getConsumptionLPerKm()));
        if(vehicle.getFuelInfo().getConsumptionLPerHour()!=null) this.literPerHour.setText(String.valueOf(vehicle.getFuelInfo().getConsumptionLPerHour()));
        if(vehicle.getVehicleEquipment().getMeetRail()!=null) this.meetRail.setSelected(vehicle.getVehicleEquipment().getMeetRail());
        if(vehicle.getOtherVehicleInfo().getField1()!=null) this.moreInfoFieldOne.setText(vehicle.getOtherVehicleInfo().getField1());
        if(vehicle.getOtherVehicleInfo().getField2()!=null) this.moreInfoFieldTwo.setText(vehicle.getOtherVehicleInfo().getField2());
        if(vehicle.getVehicleEquipment().getSecuringStraps()!=null) this.numberOfSecuringStraps.setText(String.valueOf(vehicle.getVehicleEquipment().getSecuringStraps()));
        if(vehicle.getOdometerKm()!=null) this.odometer.setText(String.valueOf(vehicle.getOdometerKm()));
        if(vehicle.getVehicleEquipment().getOtherEquipment()!=null) this.otherEquipment.setText(vehicle.getVehicleEquipment().getOtherEquipment());
        if(vehicle.getOtherVehicleInfo().getOwnership()!=null) this.ownership.setText(vehicle.getOtherVehicleInfo().getOwnership());
        if(vehicle.getVehicleEquipment().getPalletCarrier()!=null) this.palletCarrier.setSelected(vehicle.getVehicleEquipment().getPalletCarrier());
        if(vehicle.getVehicleLoadspace().getPalletNumberCapacity()!=null) this.palletNumber.setText(String.valueOf(vehicle.getVehicleLoadspace().getPalletNumberCapacity()));
        if(vehicle.getVehicleLoadspace().getPayloadWeightKg()!=null) this.payloadWeight.setText(String.valueOf(vehicle.getVehicleLoadspace().getPayloadWeightKg()));
        if(vehicle.getVehicleEquipment().getRefrigerated()!=null) this.refrigerated.setSelected(vehicle.getVehicleEquipment().getRefrigerated());
        if(vehicle.getVehicleLoadspace().getWidthCm()!=null) this.width.setText(String.valueOf(vehicle.getVehicleLoadspace().getWidthCm()));
        if(vehicle.getYearOfMake()!=null) this.yearOfMake.setText(String.valueOf(vehicle.getYearOfMake()));
        if(vehicle.getAcquisitionDate()!=null) this.acquisitionDate.setValue(vehicle.getAcquisitionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        this.isNew=isNew;
        editableButton.setSelected(isNew);
        setEditable(isNew);
    }
    public boolean isSaveClicked() {
        return saveClicked;
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    /**
     * Checks if view has invalid input, and converts input to appropriate types, saves them to 
     * the fields which will be used to fill object before saving it.
     * 
     * @return true if its ok.
     */
    private boolean isInputValid() {

		decimalFormat.setParseBigDecimal(true);
        String errorMessage = "";
        if(isNew){
	        if (this.licPlate.getText() == null || this.licPlate.getText().isEmpty()) {
	    		this.licPlate.setStyle("-fx-border-color: red;");
	            errorMessage += "Licence plate information is required!\n"; 
	        }
	        if(vehicleList.get(this.licPlate.getText().trim().toLowerCase())!=null){
	    		this.licPlate.setStyle("-fx-border-color: red;");
	            errorMessage += "Vehicle licence plate number must be unique!\n"; 
	        }
        }
        if (this.chronometer.getText() != null && !this.chronometer.getText().isEmpty()) {
        	try{
        		vehicle.setChronometerH(Integer.parseInt(this.chronometer.getText()));
        	}catch(NumberFormatException e){
        		this.chronometer.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's chronometer!\n"; 
        	}
        }
        if (this.yearOfMake.getText() != null  && !this.yearOfMake.getText().isEmpty()) {
        	try{
        		vehicle.setYearOfMake(Integer.parseInt(this.yearOfMake.getText()));
        	}catch(NumberFormatException e){
        		this.yearOfMake.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's year of make!\n"; 
        	}
        }
        if (this.width.getText() != null  && !this.width.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setWidthCm(Integer.parseInt(this.width.getText()));
        	}catch(NumberFormatException e){
        		this.width.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's loadspace width!\n"; 
        	}
        }
        if (this.payloadWeight.getText() != null  && !this.payloadWeight.getText().isEmpty() ) {
        	try{
        		vehicle.getVehicleLoadspace().setPayloadWeightKg(Float.parseFloat(this.payloadWeight.getText()));
        	}catch(NumberFormatException e){
        		this.payloadWeight.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's payload weight!\n"; 
        	}
        }
        if (this.palletNumber.getText() != null  && !this.palletNumber.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setPalletNumberCapacity(Integer.parseInt(this.palletNumber.getText()));
        	}catch(NumberFormatException e){
        		this.palletNumber.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's chronometer!\n"; 
        	}
        }
        if (this.odometer.getText() != null  && !this.odometer.getText().isEmpty()) {
        	try{
        		vehicle.setOdometerKm(Integer.parseInt(this.odometer.getText()));
        	}catch(NumberFormatException e){
        		this.odometer.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's odometer!\n"; 
        	}
        }
        if (this.numberOfSecuringStraps.getText() != null  && !this.numberOfSecuringStraps.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleEquipment().setSecuringStraps(Integer.parseInt(this.numberOfSecuringStraps.getText()));
        	}catch(NumberFormatException e){
        		this.numberOfSecuringStraps.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's num. of securing straps!\n"; 
        	}
        }
        if (this.literPerHour.getText() != null  && !this.literPerHour.getText().isEmpty()) {
        	try{
        		vehicle.getFuelInfo().setConsumptionLPerHour(Float.parseFloat(this.literPerHour.getText()));
        	}catch(NumberFormatException e){
        		this.literPerHour.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for consumption per hour!\n"; 
        	}
        }
        if (this.literPer100Km.getText() != null  && !this.literPer100Km.getText().isEmpty()) {
        	try{
        		vehicle.getFuelInfo().setConsumptionLPerHour(Float.parseFloat(this.literPer100Km.getText()));
        	}catch(NumberFormatException e){
        		this.literPer100Km.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for consumption per 100 km!\n"; 
        	}
        }
        if (this.height.getText() != null  && !this.height.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setHeightCm(Integer.parseInt(this.height.getText()));
        	}catch(NumberFormatException e){
        		this.height.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's loadspace height!\n"; 
        	}
        }
        if (this.lenght.getText() != null  && !this.lenght.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setLenghtCm(Integer.parseInt(this.lenght.getText()));
        	}catch(NumberFormatException e){
        		this.lenght.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's loadspace lenght!\n"; 
        	}
        }
        if (this.grossWeight.getText() != null  && !this.grossWeight.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setGrossWeightKg(Float.parseFloat(this.grossWeight.getText()));
        	}catch(NumberFormatException e){
        		this.grossWeight.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's gross weight!\n"; 
        	}
        }
        if (this.curbWeight.getText() != null  && !this.curbWeight.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleLoadspace().setCurbWeightKg(Float.parseFloat(this.curbWeight.getText()));
        	}catch(NumberFormatException e){
        		this.curbWeight.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's loadspace curb weight!\n"; 
        	}
        }
        if (this.fuelTankCapacity.getText() != null  && !this.fuelTankCapacity.getText().isEmpty()) {
        	try{
        		vehicle.getFuelInfo().setFuelTankCapacity(Integer.parseInt(this.fuelTankCapacity.getText()));
        	}catch(NumberFormatException e){
        		this.fuelTankCapacity.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's loadspace!\n"; 
        	}
        }
        if (this.costPerHour.getText() != null  && !this.costPerHour.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleCost().setCostPerH(new BigDecimal(Double.parseDouble(this.costPerHour.getText())));
        	}catch (NumberFormatException e) {
        		this.costPerHour.setStyle("-fx-border-color: red;");
				e.printStackTrace();
			}
        }	
        if (this.costPerKm.getText() != null  && !this.costPerKm.getText().isEmpty()) {
        	try{
        		vehicle.getVehicleCost().setCostPerKm(new BigDecimal(Double.parseDouble(this.costPerKm.getText())));
        	}catch(NumberFormatException e){
        		this.costPerKm.setStyle("-fx-border-color: red;");
	            errorMessage += "No valid input for vehicle's cost per km!\n"; 
        	}
        }
        
        if (this.currentDriver.getText().equals(this.currentCoDriver.getText())) {
            errorMessage += "Driver and co-driver cannot be the same person!\n"; 
    		this.currentDriver.setStyle("-fx-border-color: red;");
    		this.currentCoDriver.setStyle("-fx-border-color: red;");
        }
        if (!freeDrivers.contains(this.currentDriver.getText()) && (this.currentDriver.getText()!=null && !this.currentDriver.getText().trim().isEmpty())) {
        	errorMessage += "Driver must be selected from the listed ones!\n"; 
    		this.currentDriver.setStyle("-fx-border-color: red;");
        }
        if (!freeGprsDevices.contains(this.trackingId.getText()) && (this.trackingId.getText()!=null && !this.trackingId.getText().trim().isEmpty())) {
            errorMessage += "Gprs device imei must be selected from the listed ones!\n"; 
    		this.trackingId.setStyle("-fx-border-color: red;");
        }
        if (!freeDrivers.contains(this.currentCoDriver.getText()) && (this.currentCoDriver.getText()!=null && !this.currentCoDriver.getText().trim().isEmpty())) {
            errorMessage += "Co-driver must be selected from the listed ones!\n"; 
    		this.currentDriver.setStyle("-fx-border-color: red;");
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
    /**
     * Enables/Disables almost all controls in view
     * 
     * @param true if its editable.
     */
    public void setEditable(boolean b) {

        this.licPlate.setEditable(b);
        this.engineNumber.setEditable(b);
        this.trackingId.setDisable(!b);
        this.currentDriver.setDisable(!b);
        this.currentCoDriver.setDisable(!b);
        this.vehicleGroup.setDisable(!b);
        this.fuelTankCapacity.setEditable(b);
        this.literPer100Km.setEditable(b);
        this.literPerHour.setEditable(b);
        this.fuelType.setDisable(!b);
        
        this.vehicleType.setDisable(!b);
        this.vehicleModel.setDisable(!b);
        this.vehicleManufacturer.setDisable(!b);
        this.engineType.setEditable(b);
        this.yearOfMake.setEditable(b);
        this.acquisitionDate.setEditable(b);
        
        this.chronometer.setEditable(b);
        this.odometer.setEditable(b);
        this.costPerHour.setEditable(b);
        this.costPerKm.setEditable(b);
        
        this.continent.setEditable(b);
        this.country.setEditable(b);
        this.ownership.setEditable(b);
        this.moreInfoFieldOne.setEditable(b);
        this.moreInfoFieldTwo.setEditable(b);

        this.width.setEditable(b);
        this.height.setEditable(b);
        this.lenght.setEditable(b);
        this.palletNumber.setEditable(b);
        this.payloadWeight.setEditable(b);
        this.curbWeight.setEditable(b);
        this.grossWeight.setEditable(b);
        this.doubleDeck.setDisable(!b);

        this.palletCarrier.setDisable(!b);
        this.refrigerated.setDisable(!b);
        this.meetRail.setDisable(!b);
        this.numberOfSecuringStraps.setEditable(b);
        this.otherEquipment.setEditable(b);
        
        this.saveButton.setDisable(!b);
        
        if(!isNew) this.licPlate.setDisable(true);
        
        if(b){
        	editableButton.setStyle("-fx-text-fill:black;");        	
        }else{
        	editableButton.setStyle("-fx-text-fill:red;");
        	StaticJobs.addBlinkingAnimation(editableButton);
        }
        
    }

}