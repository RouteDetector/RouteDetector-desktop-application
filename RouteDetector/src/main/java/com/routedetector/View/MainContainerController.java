package com.routedetector.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.routedetector.Client.SocketContainer;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.Client.ConnectionStateHolder;
import com.routedetector.Client.RmiContainer;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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

	/**Reference to MapPaneController to set vehicle position on new message received from server.*/
	private MapCanvas mapCanvas;
	/**Reference to Gprs devices overview container to reload observed gprs devices list.
	 * 
	 */
	private GprsDevicesOverviewController gprsDevicesController;

	/**Button object which is added in status bar on connection failure.*/
    private Button buttonAlert = new Button();

	/**Reference to Main class, used to enable/disable menu item for connection management.*/
	private ConnectionStateHolder stateHolder;
	
	private RmiContainer rmiContainer;
	/**
	 * References to other controllers
	 */
	private VehicleOverviewController vehicleOverviewController;
	private DriverOverviewController driverOverviewController;
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
	public MapCanvas getMapCanvas() {
		return mapCanvas;
	}

	public void setMapCanvas(MapCanvas mapCanvas) {
		this.mapCanvas = mapCanvas;
	}

	public GprsDevicesOverviewController getGprsDevicesController() {
		return gprsDevicesController;
	}

	public void setGprsDevicesController(GprsDevicesOverviewController gprsDevicesController) {
		this.gprsDevicesController = gprsDevicesController;
	}

	public ConnectionStateHolder getStateHolder() {
		return stateHolder;
	}

	public void setStateHolder(ConnectionStateHolder stateHolder) {
		this.stateHolder = stateHolder;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}
    public Tab getVehicleTab() {
		return vehicleTab;
	}

	public void setVehicleTab(Tab vehicleTab) {
		this.vehicleTab = vehicleTab;
	}
	
    public MainContainerController(){
    }

	public RmiContainer getRmiContainer() {
		return rmiContainer;
	}

	public void setRmiContainer(RmiContainer rmiContainer) {
		this.rmiContainer = rmiContainer;
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
	@FXML
    private void initialize() {
		buttonAlert.setTooltip(new Tooltip("Socket connection closed!"));
		buttonAlert.getStyleClass().add("picture-button");
		buttonAlert.getStyleClass().add("transparent");
		buttonAlert.getStyleClass().add("warning");
		buttonAlert.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	socketContainer.changeSocketConnection();
	        }
	    });
	}
	/**
	 * Initializes and starts service object.
	 */
	@FXML
    private void openSite() {
		final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load("http://www.routedetector.com");

		Tab help=new Tab();
        HBox h=new HBox();
        h.setSpacing(10);
        Label label=new Label("Help");
        Button cancel=new Button();
        h.getChildren().add(label);
        h.getChildren().add(cancel);
		cancel.getStyleClass().add("picture-button");
		cancel.getStyleClass().add("transparent");
		cancel.getStyleClass().add("cancel");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	tabs.getTabs().remove(help);
	        }
	    });
        
		help.setGraphic(h);
		help.setContent(webView);
		tabs.getTabs().add(help);
		statusBar.setProgressText("Opened help tab.");
		/*WebPageService service=new WebPageService();
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
		service.start();*/
	}
	@FXML
    private void connect() {
		socketContainer.changeSocketConnection();
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
	 * Enables/disables menu item and changes its text.
	 * 
	 * @param isConnected true if item should be enabled
	 */
	public void setConnected(boolean isConnected) {
		connectedItem.setDisable(isConnected);
			
    	// If socket is connected, warning button is removed, if not, it is added to status bar.
   	 //Also adds on click functionality to button.
   	
	   	if(isConnected){
	   		connectedItem.setText("Connected");
	   		if(statusBar.isOnLeft(buttonAlert)){
	   			statusBar.removeLeft(buttonAlert);
	   			statusBar.setProgressText("Socket connected.");
	   		}
	   	}else{
	   		connectedItem.setText("Connect...");
				if(mapCanvas!=null) mapCanvas.disableAll();
				if(gprsDevicesController!=null) gprsDevicesController.reloadTable();
				stateHolder.getObservedGprsDevicesImeiList().clear();
				
	   		if(!statusBar.isOnLeft(buttonAlert)){
		    	statusBar.addLeft(buttonAlert);
	   			statusBar.setProgressText("Socket disconnected.");
	   			
		    		StaticJobs.addBlinkingAnimation(buttonAlert);
		    	}	    	    		
	   	}
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
            			
            			Stage stage=new Stage();
            	        /*final WebView webView = new WebView();
            	        final WebEngine webEngine = webView.getEngine();
            	        webEngine.load("http://www.google.com");
            	        */
            	        stage.setTitle("Simple browser");
            	        Scene scene = new Scene(new BorderPane()
            	        		
            	        		,1000,700, Color.web("#666970"));
            	        stage.setScene(scene);

            	        stage.show();
            			/*
            			String os = System.getProperty("os.name").toLowerCase();
            			
            			if(os.indexOf( "win" ) >= 0){
            				Runtime rt = Runtime.getRuntime();
            				rt.exec( "rundll32 url.dll,FileProtocolHandler " + webSite);
            			}else if(os.indexOf( "mac" ) >= 0){
            				Runtime rt = Runtime.getRuntime();
            				rt.exec( "open" + webSite);
            			}else if(os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0){
            				Runtime rt = Runtime.getRuntime();
            				String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
            				                                 "netscape","opera","links","lynx"};

            				StringBuffer cmd = new StringBuffer();
            				for (int i=0; i<browsers.length; i++)
            				     cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + webSite + "\" ");

            				rt.exec(new String[] { "sh", "-c", cmd.toString() });
            			}*/
            			/*
            			if(Desktop.isDesktopSupported())
            			{
            			  Desktop.getDesktop().browse(new URI(webSite));
            			}
            			*/
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
