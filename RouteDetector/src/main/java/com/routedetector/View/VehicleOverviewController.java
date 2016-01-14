package com.routedetector.View;

import java.io.IOException;
import java.util.ArrayList;

import com.routedetector.AssetsJPA.FuelInfo;
import com.routedetector.AssetsJPA.OtherVehicleInfo;
import com.routedetector.AssetsJPA.Vehicle;
import com.routedetector.AssetsJPA.VehicleCost;
import com.routedetector.AssetsJPA.VehicleEquipment;
import com.routedetector.AssetsJPA.VehicleLoadspace;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.Client.RmiContainer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

/**
 * Class that acts as controller for vehicle overview. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class VehicleOverviewController {
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> licenseColumn;
    @FXML
    private TableColumn<Vehicle, String> typeColumn;
    @FXML
    private TableColumn<Vehicle, String> driverColumn;
    @FXML
    private TableColumn<Vehicle, String> manufacturerColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, String> trackingIdColumn;
    @FXML
    private TableColumn<Vehicle, String> countryColumn;
    @FXML
    private TableColumn<Vehicle, String> continentColumn;
    @FXML
    private TableColumn<Vehicle, String> ownershipColumn;
    @FXML
    private TableColumn<Vehicle, String> departmentColumn;
    @FXML
    private HBox topBox;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button newButton;
    
    /**
     * Reference of Rmi connection handler class.
     */
    private RmiContainer rmiContainer;
    /**
     * Reference of stage that view is placed into for alert owner setting.
     */
    private Stage motherStage;
    
    private StatusBar statusBar;
    
    public VehicleOverviewController() {
    }

    /**
     * Fills the TableView columns and adds reload button.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getVehicleTypeProperty());
        driverColumn.setCellValueFactory(cellData -> cellData.getValue().getCurrentDriverProperty());
        licenseColumn.setCellValueFactory(cellData -> cellData.getValue().getLicensePlateProperty());
        manufacturerColumn.setCellValueFactory(cellData -> cellData.getValue().getVehicleManufacturerProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().getVehicleModelProperty());
        trackingIdColumn.setCellValueFactory(cellData -> cellData.getValue().getTrackingIdProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().getOtherVehicleInfo().getCountryProperty());
        continentColumn.setCellValueFactory(cellData -> cellData.getValue().getOtherVehicleInfo().getContinentProperty());
        ownershipColumn.setCellValueFactory(cellData -> cellData.getValue().getOtherVehicleInfo().getOwnershipProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().getVehicleGroupProperty());

        // Listen for selection changes and show the person details when changed.
        vehicleTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showVehicleDetails(newValue));
        
        Button button=new Button();
        button.getStyleClass().add("transparent");
        button.setTooltip(new Tooltip("Reload table"));
        button.getStyleClass().add("reload-button");
        button.getStyleClass().add("picture-button");
		button.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	reloadTable();
	        }
	    });

	    button.defaultButtonProperty().bind(button.focusedProperty());
		newButton.defaultButtonProperty().bind(newButton.focusedProperty());
		deleteButton.defaultButtonProperty().bind(deleteButton.focusedProperty());
		editButton.defaultButtonProperty().bind(editButton.focusedProperty());
	    
		topBox.getChildren().add(button);
    }
    private void showVehicleDetails(Vehicle v){
    }
    public void setRmiContainer(RmiContainer rmiContainer) {
        this.rmiContainer = rmiContainer;
        tableSetItems();
    }
    public void tableSetItems(){
        // Add observable list data to the table
        vehicleTable.setItems(FXCollections.observableList(new ArrayList<Vehicle>(rmiContainer.getVehicleData().values())));
    }
    public void setMotherWindow(Stage stage){
    	motherStage=stage;
    }
    /**
     * Calls confirmation dialog before object deletion.
     */
    @FXML
    private void handleDeleteVehicle(){
        int selectedIndex = vehicleTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
        	StaticJobs.showNoSelectionAlert("vehicle",motherStage);
        }else if (!StaticJobs.showDeleteConfirmationDialog("vehicle",motherStage)){
        	statusBar.setProgressText("Action canceled by user...");            		
    	}else{
    		deleteVehicle();
	    }
    }
    /**
     * Calls Rmi connection handler class for deletion on new thread
     * 
     */
    private void deleteVehicle(){
    	statusBar.setProgressText("Saving changes...");
       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.removeVehicleOnRunLater(vehicleTable.getSelectionModel().getSelectedItem(),motherStage));
                reloadTableOnRunLater();
            }
        };
        task.setDaemon(true);
        task.start();
    }
    /**
     * Creates new Vehicle object and opens vehicle edit dialog.
     * If afterwards save button is clicked it calls method createVehicle
     */
    @FXML
    public void handleNewVehicle() {
        Vehicle tempVehicle = new Vehicle();
        FuelInfo f=new FuelInfo();
        OtherVehicleInfo otherInfo=new OtherVehicleInfo();
        VehicleLoadspace loadspace=new VehicleLoadspace();
        VehicleEquipment equipment=new VehicleEquipment();
        VehicleCost cost=new VehicleCost();
        tempVehicle.setOtherVehicleInfo(otherInfo);
        tempVehicle.setFuelInfo(f);
        tempVehicle.setVehicleLoadspace(loadspace);
        tempVehicle.setVehicleEquipment(equipment);
        tempVehicle.setVehicleCost(cost);
        boolean saveClicked = showVehicleEditDialog(tempVehicle,true); 
        if (!saveClicked){
        	reloadTable();
        }else{
        	createVehicle(tempVehicle);
	    }
    }
    /**
     * Remote method invocation is called by RmiContainer class in new thread to save objec.
     * 
     * @param tempVehicle is Vehicle object that is persisted.
     */
    private void createVehicle(Vehicle tempVehicle){
    	statusBar.setProgressText("Saving changes...");
       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.persistVehicleOnRunLater(tempVehicle,motherStage));
            	
                reloadTableOnRunLater();
            }
            };
        task.setDaemon(true);
        task.start();
    }
    /**
     * If Vehicle is selected, opens vehicle edit dialog, if not shows Alert.
     * If afterwards save button is clicked it calls method editVehicle
     */
    @FXML
    private void handleEditVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
        	StaticJobs.showNoSelectionAlert("vehicle",motherStage);
        }else{
            boolean saveClicked = showVehicleEditDialog(selectedVehicle,false);

            if (!saveClicked){
            	reloadTable();        	
            }else{
            	editVehicle(selectedVehicle);
        	}
        }
        
    }
    /**
     * RmiContainer is called to do the job of updating object on server.
     * 
     * @param selectedVehicle is selected Vehicle object.
     */
    private void editVehicle(Vehicle selectedVehicle){
       	statusBar.setProgressText("Saving changes...");

       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.mergeVehicleOnRunLater(selectedVehicle,motherStage));
            	
                reloadTableOnRunLater();
            }
        };
        task.setDaemon(true);
        task.start();
    }
    @FXML
    private void manageGroups(){
    	
    }
    /**
     * Method creates new stage for editing object, and returns boolean value if object should be 
     * saved or not.
     * 
     * @param vehicle is Vehicle object, subject of editing
     * @param isNew boolean value if object is new or old one
     * @return true if object should be saved
     */
    public boolean showVehicleEditDialog(Vehicle vehicle,boolean isNew) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DriverOverviewController.class.getResource("VehicleEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.getIcons().add(new Image("/resources/logo_image.png"));
            if(isNew)
            	dialogStage.setTitle("Create Vehicle");
            else
                dialogStage.setTitle("View/Edit Vehicle");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(motherStage);
            
            Scene scene = new Scene(page);
            scene.getStylesheets().add("/resources/style.css");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            // Set the vehicle into the controller.
            VehicleEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setVehicle(vehicle,rmiContainer.getFuelTypes(),rmiContainer.getVehicleTypes(),rmiContainer.getVehicleManufacturers(), rmiContainer.getVehicleModels(),rmiContainer.getVehicleGroup(),rmiContainer.getVehicleData(), rmiContainer.getGprsData(),rmiContainer.getDriverData(),isNew);
            
            // Show the dialog and wait until the user closes it
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                	statusBar.setProgressText("Vehicle New/Edit dialog closed");
                }
            });
            dialogStage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent window)
                {
                	statusBar.setProgressText("Vehicle New/Edit dialog opened");
                }
            });
            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
    @FXML
    public void reloadVehicle() {
    	statusBar.setProgressText("Table refreshed.");
    	reloadTable();
    }

    /**
     * Calls Rmi handler class to retrieve fresh list of objects, removes all columns and 
     * puts them back to force TableView refreshing. 
     */
	public void reloadTable(){
    	rmiContainer.setVehicleInfo(motherStage);
    	tableSetItems();
    }
    private void setStatusOnRunLater(String message) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	statusBar.setProgressText(message);
            }
        });
    }
    public void reloadTableOnRunLater() {
    	rmiContainer.setVehicleInfo(motherStage);
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	tableSetItems();
            }
        });
    }
}
