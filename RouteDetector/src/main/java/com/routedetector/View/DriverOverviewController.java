package com.routedetector.View;

import java.io.IOException;
import java.util.ArrayList;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.DriverEmergency;
import com.routedetector.AssetsJPA.DriverLicense;
import com.routedetector.AssetsJPA.DriverMobileTerminal;
import com.routedetector.AssetsJPA.DriverPersonalData;
import com.routedetector.AssetsJPA.DriverProfessionalData;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.Client.DriverWrapper;
import com.routedetector.Client.RmiContainer;

import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;

/**
 * Class that acts as controller for driver overview. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class DriverOverviewController {
    @FXML
    private TableView<DriverWrapper> driverTable;
	@FXML
    private TableColumn<DriverWrapper, String> nameColumn;
    @FXML
    private TableColumn<DriverWrapper, String> vehicleLicenseColumn;
    @FXML
    private TableColumn<DriverWrapper, String> tachographColumn;
    @FXML
    private TableColumn<DriverWrapper, String> mobilePhoneColumn;
    @FXML
    private TableColumn<DriverWrapper, String> departmentColumn;
    @FXML
    private TableColumn<DriverWrapper, String> driverLicenceColumn;
    @FXML
    private TableColumn<DriverWrapper, String> classesColumn;
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
    
    public DriverOverviewController() {
    }

    /**
     * Fills the TableView columns and adds reload button.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getNameProperty());
        vehicleLicenseColumn.setCellValueFactory(cellData -> cellData.getValue().getCurrentVehiclePlateNumberProperty());
        tachographColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getTachographDriverCardProperty());
        mobilePhoneColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getMobilePhoneProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getDriverProfessionalData().getDepartmentProperty());
        driverLicenceColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getDriverLicense().getNumberProperty());
        classesColumn.setCellValueFactory(cellData -> cellData.getValue().getDriver().getDriverLicense().getClassesProperty());

        // Listen for selection changes and show the diver details when changed.
        driverTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDriverDetails(newValue));
        Button button=new Button();
        button.setTooltip(new Tooltip("Reload table"));
        button.getStyleClass().add("transparent");
        button.getStyleClass().add("picture-button");
        button.getStyleClass().add("reload-button");
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
    public void showDriverDetails(DriverWrapper d){
    
    }
    public void setRmiContainer(RmiContainer rmiContainer) {
        this.rmiContainer = rmiContainer;
        tableSetItems();
    }
    public void tableSetItems(){
    	driverTable.setItems(FXCollections.observableList(new ArrayList<DriverWrapper>(rmiContainer.getDriverData().values())));
	}
    public void setMotherWindow(Stage stage){
    	motherStage=stage;
    }
    /**
     * Calls confirmation dialog before object deletion.
     */
    @FXML
    private void handleDeleteDriver(){
        int selectedIndex = driverTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
        	StaticJobs.showNoSelectionAlert("driver",motherStage);
        }else if (!StaticJobs.showDeleteConfirmationDialog("driver",motherStage)){
        	statusBar.setProgressText("Action canceled by user...");            		
    	}else{
    		deleteDriver();
	    }
    }
    /**
     * Calls Rmi connection handler class for deletion on new thread
     * 
     */
    private void deleteDriver(){
    	statusBar.setProgressText("Saving changes...");
       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.removeDriverOnRunLater(driverTable.getSelectionModel().getSelectedItem().getDriver(),motherStage));
                reloadTableOnRunLater();
            }
        };
        task.setDaemon(true);
        task.start();
    }
    /**
     * Creates new Driver object and opens driver edit dialog.
     * If afterwards save button is clicked it calls method createDriver
     */
    @FXML
    public void handleNewDriver(){
        Driver tempDriver = new Driver();
        DriverEmergency e=new DriverEmergency();
        DriverLicense l=new DriverLicense();
        DriverMobileTerminal t=new DriverMobileTerminal();
        DriverPersonalData pers=new DriverPersonalData();
        DriverProfessionalData prof=new DriverProfessionalData();
        tempDriver.setDriverLicense(l);
        tempDriver.setDriverMobileTerminal(t);
        tempDriver.setDriverEmergency(e);
        tempDriver.setDriverPersonalData(pers);
        tempDriver.setDriverProfessionalData(prof);
        DriverWrapper driverWrapper=new DriverWrapper(tempDriver,null);
        
        boolean saveClicked = showDriverEditDialog(driverWrapper,true);   
        if (!saveClicked){
        	reloadTable();        	
        }else{
        	createDriver(tempDriver);
	    }
    }
    /**
     * Remote method invocation is called by RmiContainer class in new thread to save object.
     * 
     * @param tempDriver is Driver object that is persisted.
     */
    private void createDriver(Driver tempDriver){
    	statusBar.setProgressText("Saving changes...");

       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.persistDriverOnRunLater(tempDriver,motherStage));
                reloadTableOnRunLater();
            }
		};
		task.setDaemon(true);
		task.start();
    }
    /**
     * If Driver is selected, opens driver edit dialog, if not shows Alert.
     * If afterwards save button is clicked it calls method editDriver
     */
    @FXML
    private void handleEditDriver(){
        DriverWrapper selectedDriverWrapper = driverTable.getSelectionModel().getSelectedItem();
        if (selectedDriverWrapper == null) {
        	StaticJobs.showNoSelectionAlert("vehicle",motherStage);
        }else{
            boolean saveClicked = showDriverEditDialog(selectedDriverWrapper,false);

            if (!saveClicked){
            	reloadTable();        	
            }else{
        		editDriver(selectedDriverWrapper);
    	    }
        }
    }
    /**
     * RmiContainer is called to do the job of updating object on server.
     * 
     * @param selectedDriverWrapper is selected DriverWrapper object.
     */
    private void editDriver(DriverWrapper selectedDriverWrapper){
       	statusBar.setProgressText("Saving changes...");

       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.mergeDriverOnRunLater(selectedDriverWrapper.getDriver(),motherStage));
                reloadTableOnRunLater();
            }
        };
        task.setDaemon(true);
        task.start();
    }
    /**
     * Method creates new stage for editing object, and returns boolean value if object should be 
     * saved or not.
     * 
     * @param driverWrapper is DriverWrapper object, subject of editing
     * @param isNew boolean value if object is new or old one
     * @return true if object should be saved
     */
    public boolean showDriverEditDialog(DriverWrapper driverWrapper,boolean isNew) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DriverOverviewController.class.getResource("DriverEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if(isNew)
            	dialogStage.setTitle("Create Driver");
            else
            	dialogStage.setTitle("View/Edit Driver");
            dialogStage.getIcons().add(new Image("/resources/logo_image.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(motherStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add("/resources/style.css");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            // Set the vehicle into the controller.
            DriverEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            
            controller.setDriver(driverWrapper,rmiContainer.getDriverData(), rmiContainer.getDriverGroups(),isNew);
            // Show the dialog and wait until the user closes it
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                	statusBar.setProgressText("Driver New/Edit dialog closed");
                }
            });
            dialogStage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent window)
                {
                	statusBar.setProgressText("Driver New/Edit dialog opened");
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
    public void reloadDriver(){
    	statusBar.setProgressText("Table refreshed.");
    	reloadTable();
    }
    /**
     * Calls Rmi handler class to retrieve fresh list of objects, removes all columns and 
     * puts them back to force TableView refreshing. 
     */
	public void reloadTable(){
    	rmiContainer.setDriverInfo(motherStage);
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
    	rmiContainer.setDriverInfo(motherStage);
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	tableSetItems();
            }
        });
    }
}
