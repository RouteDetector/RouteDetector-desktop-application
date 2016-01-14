package com.routedetector.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.JPA.GprsDevice;
import com.routedetector.Client.ConnectionStateHolder;
import com.routedetector.Client.GprsDeviceWrapper;
import com.routedetector.Client.RmiContainer;
import com.routedetector.Client.SocketContainer;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * Class that acts as controller for Gprs devices overview. 
 * 
 * @author  Danijel Sudimac
*/
public class GprsDevicesOverviewController {
    @FXML
    private TableView<GprsDeviceWrapper> gprsTable;
	@FXML
    private TableColumn<GprsDeviceWrapper, String> imeiColumn;
    @FXML
    private TableColumn<GprsDeviceWrapper, Boolean> connectedColumn;
    @FXML
    private TableColumn<GprsDeviceWrapper, String> licensePlateColumn;
    @FXML
    private TableColumn<GprsDeviceWrapper, String> driverColumn;
    @FXML
    private TableColumn<GprsDeviceWrapper, String> coDriverColumn;
    @FXML
    private TableColumn<GprsDeviceWrapper, Boolean> isPublicColumn;
    @FXML
    private HBox topBox;
    @FXML
    private ListView<String> listOfAddedGprsDevices;
    @FXML
    private Button deleteButton;
    @FXML
    private Button newButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button applyButton;
    
    /**
     * Reference of Rmi connection handler class.
     */
    private RmiContainer rmiContainer;
    /**
     * Reference of stage that view is placed into for alert owner setting.
     */
    private Stage motherStage;
    
    private StatusBar statusBar;
    
    private SocketContainer socketContainer;
    
    public SocketContainer getSocketContainer() {
		return socketContainer;
	}

	public void setSocketContainer(SocketContainer socketContainer) {
		this.socketContainer = socketContainer;
	}

	private ConnectionStateHolder stateHolder;
    
    public GprsDevicesOverviewController() {
    }

    /**
     * Fills the TableView columns and adds reload button.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        imeiColumn.setCellValueFactory(cellData -> cellData.getValue().getGprsDevice().getImeiProperty());
        licensePlateColumn.setCellValueFactory(cellData -> cellData.getValue().getVehicleLicensePlateProperty());
        connectedColumn.setCellValueFactory(cellData -> cellData.getValue().getGprsDevice().getIsconnectedProperty());
        isPublicColumn.setCellValueFactory(cellData -> cellData.getValue().getGprsDevice().getIsPublicProperty());
        driverColumn.setCellValueFactory(cellData -> cellData.getValue().getDriverNameProperty());
        coDriverColumn.setCellValueFactory(cellData -> cellData.getValue().getCodriverNameProperty());
       
        if(stateHolder!=null)
        	listOfAddedGprsDevices.setItems(stateHolder.getObservedGprsDevicesImeiList());
        
        // Listen for selection changes and show the diver details when changed.
        gprsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGprsDetails(newValue));
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
		editButton.defaultButtonProperty().bind(editButton.focusedProperty());
		deleteButton.defaultButtonProperty().bind(deleteButton.focusedProperty());
		addButton.defaultButtonProperty().bind(addButton.focusedProperty());
		removeButton.defaultButtonProperty().bind(removeButton.focusedProperty());
		applyButton.defaultButtonProperty().bind(applyButton.focusedProperty());
	    
		topBox.getChildren().add(button);

        gprsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listOfAddedGprsDevices.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    public void showGprsDetails(GprsDeviceWrapper d){
    
    }
    public void setRmiContainer(RmiContainer rmiContainer) {
        this.rmiContainer = rmiContainer;
        tableSetItems();
    }
    public void tableSetItems(){
    	gprsTable.setItems(FXCollections.observableList(new ArrayList<GprsDeviceWrapper>(rmiContainer.getGprsData().values())));
    }
    public void setMotherWindow(Stage stage){
    	motherStage=stage;
    }
    /**
     * Calls confirmation dialog before object deletion.
     */
    @FXML
    private void handleDeleteGprs(){
        int selectedIndex = gprsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
        	StaticJobs.showNoSelectionAlert("gprs device",motherStage);
        }else if (!StaticJobs.showDeleteConfirmationDialog("gprs",motherStage)){
        	statusBar.setProgressText("Action canceled by user...");            		
    	}else{
    		deleteGprs();
	    }
    }
    /**
     * Adds selected items from table to list view
     */
    @FXML
    private void addToList(){
        ObservableList<Integer> selectedIndices = gprsTable.getSelectionModel().getSelectedIndices();
        if (selectedIndices.isEmpty()) {
        	StaticJobs.showNoSelectionAlert("gprs device",motherStage);
        }else{
        	ObservableList<GprsDeviceWrapper> wrapper=gprsTable.getSelectionModel().getSelectedItems();
        	Iterator<GprsDeviceWrapper> iterator=wrapper.iterator();
        	
        	while(iterator.hasNext()){
        		String imei=iterator.next().getGprsDevice().getImei();
        		if(!this.listOfAddedGprsDevices.getItems().contains(imei))
        			this.listOfAddedGprsDevices.getItems().add(imei);
        	}
	    }
    }
    /**
     * Removes selected items from list view
     */
    @FXML
    private void removeFromList(){
        ObservableList<Integer> selectedIndices = listOfAddedGprsDevices.getSelectionModel().getSelectedIndices();
        if (selectedIndices.isEmpty()) {
        	StaticJobs.showNoSelectionAlert("gprs device",motherStage);
        }else{
        	this.listOfAddedGprsDevices.getItems().removeAll(listOfAddedGprsDevices.getSelectionModel().getSelectedItems());
	    }
    }
    /**
     * Filling ObservableList of GprsDeviceWrapper object with objects from list view
     */
    @FXML
    public void applyChanges(){
    	stateHolder.getObservedGprsDevicesImeiList().setAll(this.listOfAddedGprsDevices.getItems());
    	socketContainer.sendMessageToServer();
    }
    public boolean areChangesMade(){
    	return !stateHolder.getObservedGprsDevicesImeiList().equals(this.listOfAddedGprsDevices.getItems());
    }
    /**
     * Calls Rmi connection handler class for deletion on new thread
     * 
     */
    private void deleteGprs(){
    	statusBar.setProgressText("Saving changes...");
       	Thread thread=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.removeGprsOnRunLater(gprsTable.getSelectionModel().getSelectedItem().getGprsDevice(),motherStage));            	
                reloadTableOnRunLater();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    /**
     * Creates new GprsDevice object and opens Gprs edit dialog.
     * If afterwards save button is clicked it calls method createGprsDevice
     */
    @FXML
    public void handleNewGprs(){
        GprsDevice tempGprsDevice = new GprsDevice();
        GprsDeviceWrapper gprsWrapper=new GprsDeviceWrapper(tempGprsDevice, null,null,null);
        
        boolean saveClicked = showGprsDeviceEditDialog(gprsWrapper,true);   
        if (!saveClicked){
        	reloadTable();        	
        }else{
        	tempGprsDevice.setCompanyEmail(stateHolder.getLoginInfo().getEmail());
        	createGprsDevice(tempGprsDevice);
	    }
    }
    @FXML
    public void handleEditGprs(){
        GprsDeviceWrapper selectedGprsWrapper = gprsTable.getSelectionModel().getSelectedItem();
        if (selectedGprsWrapper == null) {
        	StaticJobs.showNoSelectionAlert("vehicle",motherStage);
        }else{
        
	        boolean saveClicked = showGprsDeviceEditDialog(selectedGprsWrapper,false);   
	        if (!saveClicked){
	        	reloadTable();        	
	        }else{
        		editGprsDevice(selectedGprsWrapper.getGprsDevice());
		    }
        }
    }

    /**
     * RmiContainer is called to do the job of updating object on server.
     * 
     * @param selectedDevice is selected GprsDevice object.
     */
    private void editGprsDevice(GprsDevice selectedDevice){
       	statusBar.setProgressText("Saving changes...");

       	Thread task=new Thread() {
            public void run() {
            	setStatusOnRunLater(rmiContainer.mergeGprsDeviceOnRunLater(selectedDevice,motherStage));
            	
                reloadTableOnRunLater();
            }
        };
        task.setDaemon(true);
        task.start();
    }
    /**
     * Remote method invocation is called by RmiContainer class in new thread to save object.
     * 
     * @param tempGprsDevice is GprsDevice object that is persisted.
     */
    private void createGprsDevice(GprsDevice tempGprsDevice){
    	statusBar.setProgressText("Saving changes...");

       	Thread task=new Thread() {
            public void run() {            	
            	setStatusOnRunLater(rmiContainer.persistGprsDeviceOnRunLater(tempGprsDevice,motherStage));
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
     * @param gprsWrapper is GprsDeviceWrapper object, subject of editing
     * @param isNew boolean value if object is new or old one
     * @return true if object should be saved
     */
    public boolean showGprsDeviceEditDialog(GprsDeviceWrapper gprsWrapper,boolean isNew) {
        try {
        	
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GprsDevicesOverviewController.class.getResource("GprsEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if(isNew)
            	dialogStage.setTitle("Create Gprs device");
            else
                dialogStage.setTitle("View/Edit Gprs device");
            dialogStage.getIcons().add(new Image("/resources/logo_image.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(motherStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add("/resources/style.css");
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            
            // Set the vehicle into the controller.
            GprsDeviceEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            
            controller.setGprsDevice(gprsWrapper.getGprsDevice(),rmiContainer.getGprsData(), isNew);
            // Show the dialog and wait until the user closes it
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                	statusBar.setProgressText("Gprs New/Edit dialog closed");
                }
            });
            dialogStage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent window){
                	statusBar.setProgressText("Gprs New/Edit dialog opened");
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
    public void reloadGprs(){    	
    	statusBar.setProgressText("Table refreshed.");
    	reloadTable();
    	
    }
    /**
     * Calls Rmi handler class to retrieve fresh list of objects, removes all columns and 
     * puts them back to force TableView refreshing. 
     */
	public void reloadTable(){
        listOfAddedGprsDevices.setItems(stateHolder.getObservedGprsDevicesImeiList());
        
    	rmiContainer.setGprsDevicesInfo(motherStage);
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
    	rmiContainer.setGprsDevicesInfo(motherStage);
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	tableSetItems();
            }
        });
    }

	public void setConnectionStateHolder(ConnectionStateHolder holder) {
		this.stateHolder = holder;
	}
}
