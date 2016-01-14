package com.routedetector.View;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import com.routedetector.Client.SocketContainer;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.Client.RmiContainer;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
* This class acts as controller of MainContainer view which holds all the rest containers and views.
* It holds methods for menu controls and one nested class which opens RouteDetector web site
* on default browser.
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class MainContainerController {
	@FXML
    private Tab vehicleTab;
	@FXML
    private Tab gprsTab;
	@FXML
    private Tab driverTab;
	@FXML
    private Tab mapTab;
	@FXML
    private Tab statisticsTab;
	@FXML
    private BorderPane borderPane;
	@FXML
    private TabPane tabs;
	@FXML
    private MenuItem connectedItem;

	/**
	 * Enables/disables menu item and changes its text.
	 * 
	 * @param isConnected true if item should be enabled
	 */
	public void setConnectedItemValue(boolean isConnected) {
		connectedItem.setDisable(isConnected);
		
		if(isConnected)
			connectedItem.setText("Connected");
		else
			connectedItem.setText("Connect...");
	}
	/**
	 * References to other controllers
	 */
	private VehicleOverviewController vehicleOverviewController;
	private DriverOverviewController driverOverviewController;
	private MapPaneController mapPaneController;
	private GprsDevicesOverviewController gprsDevicesOverviewController;
	
	public GprsDevicesOverviewController getGprsDevicesOverviewController() {
		return gprsDevicesOverviewController;
	}

	public void setGprsDevicesOverviewController(GprsDevicesOverviewController gprsDevicesOverviewController) {
		this.gprsDevicesOverviewController = gprsDevicesOverviewController;
	}
	/**
	 * Reference to class that handles socket connection
	 */
	private SocketContainer socketContainer;
	
	/**
	 * Reference to initStage
	 */
    private Stage motherStage;
    
    /** Objects required for properties file reading*/
    private Properties prop;
    private InputStream in;
    
	private StatusBar statusBar;
	
	public VehicleOverviewController getVehicleOverviewController() {
		return vehicleOverviewController;
	}

	public void setVehicleOverviewController(VehicleOverviewController vehicleOverviewController) {
		this.vehicleOverviewController = vehicleOverviewController;
	}

	public DriverOverviewController getDriverOverviewController() {
		return driverOverviewController;
	}

	public void setDriverOverviewController(DriverOverviewController driverOverviewController) {
		this.driverOverviewController = driverOverviewController;
	}
	public TabPane getTabs() {
		return tabs;
	}

	public void setTabs(TabPane tabs) {
		this.tabs = tabs;
	}

	public Tab getMapTab() {
		return mapTab;
	}

	public void setMapTab(Tab mapTab) {
		this.mapTab = mapTab;
	}
	public Tab getStatisticsTab() {
		return statisticsTab;
	}

	public void setStatisticsTab(Tab statisticsTab) {
		this.statisticsTab = statisticsTab;
	}
	public Tab getGprsTab() {
		return gprsTab;
	}

	public void setGprsTab(Tab gprsTab) {
		this.gprsTab = gprsTab;
	}
	
	public Tab getDriverTab() {
		return driverTab;
	}

	public void setDriverTab(Tab driverTab) {
		this.driverTab = driverTab;
	}
	
	private RmiContainer rmiContainer;
	
    public Tab getVehicleTab() {
		return vehicleTab;
	}

	public void setVehicleTab(Tab vehicleTab) {
		this.vehicleTab = vehicleTab;
	}
    public MainContainerController(){
    }
	
	@FXML
    private void initialize() {
	}

	public RmiContainer getRmiContainer() {
		return rmiContainer;
	}

	public void setRmiContainer(RmiContainer rmiContainer) {
		this.rmiContainer = rmiContainer;
	}
	public void showWindow(){
		
	}
	public void setStatusBar(StatusBar statusBar) {
		this.statusBar=statusBar;
		borderPane.setBottom(statusBar);
	}
	@FXML
    private void close() {
		motherStage.close();
	}
	/**
	 * Calls method from vehicle overview controller class
	 */
	@FXML
    private void newVehicle() {
		vehicleOverviewController.handleNewVehicle();

	}
	/**
	 * Calls method from driver overview controller class
	 */
	@FXML
    private void newDriver() {
		driverOverviewController.handleNewDriver();

	}
	/**
	 * Calls method from gprs devices overview controller class
	 */
	@FXML
    private void newGprsDevice() {
		gprsDevicesOverviewController.handleNewGprs();

	}
	/**
	 * Initializes and starts service object.
	 */
	@FXML
    private void openSite() {
		statusBar.setProgressText("Opening web browser...");
		WebPageService service=new WebPageService();
		service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
		      @Override
		      public void handle(WorkerStateEvent workerStateEvent) {
		          prop=null;
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
						}
					}
		      }
		  });
		service.start();
	}
	@FXML
    private void connect() {
		socketContainer.changeSocketConnection();
	}

	public Stage getMotherStage() {
		return motherStage;
	}

	public void setMotherStage(Stage motherStage) {
		this.motherStage = motherStage;
	}
	
    public SocketContainer getSocketContainer() {
		return socketContainer;
	}

	public void setSocketContainer(SocketContainer socketContainer) {
		this.socketContainer = socketContainer;
	}
	public MapPaneController getMapPaneController() {
		return mapPaneController;
	}

	public void setMapPaneController(MapPaneController mapPaneController) {
		this.mapPaneController = mapPaneController;
	}
    private void showProgressRunLater(String message) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	statusBar.setProgressText(message);
            }
        });
    }
	/**
     * Loads web-site URL from properties file and opens it in default browser.
     *
     */
	public class WebPageService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {                	
                	try {
            	    	prop = new Properties();
            	    	in = new FileInputStream("./properties/additional.properties");
            	    	//in = new FileInputStream("D:/ostava/properties/additional.properties");
            	    	prop.load(in);
            	    	in.close();
            	    	String webSite=prop.getProperty("web_site");
            			prop.clear();
            			if(Desktop.isDesktopSupported())
            			{
            			  Desktop.getDesktop().browse(new URI(webSite));
            			}
                		showProgressRunLater("Browser opened.");
            		}catch (IOException e) {
            			StaticJobs.showExceptionAlertOnRunLater(e,motherStage);
                		showProgressRunLater("Browser opening canceled.");
            		}finally {
            			if (in != null) {
            				try {
            					in.close();
            				} catch (IOException e) {
            					StaticJobs.showExceptionAlertOnRunLater(e,motherStage);
            				}
            			}
            		}
    				return null;
                }
            };
        }
    }

}
