package com.routedetector.Client;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import com.routedetector.Basic.SecurityLoginInterface;
import com.routedetector.View.DriverOverviewController;
import com.routedetector.View.GprsDevicesOverviewController;
import com.routedetector.View.LoginController;
import com.routedetector.View.MainContainerController;
import com.routedetector.View.MapPaneController;
import com.routedetector.View.StatisticsOverviewController;
import com.routedetector.View.VehicleOverviewController;

/**
 * This class is the Main class of the application.
 * It opens splash screen, opens Service which handles loading properties, and makes secure
 * connection to Rmi registry located on RouteDetector server. After that, it initializes 
 * initStage, and opens login stage. If login attempt succeeds, it opens socket connection 
 * and shows initStage.
 * 
 * @author  Danijel Sudimac
*/
public class Main extends Application implements ConnectionStateHolder{
	/** References to stages.*/
	private Stage initStage;
	private Stage splashStage;
	
	/** Holds information of socket connection status.*/
	private boolean isConnected;
	
	/** Last signed in user's information saved in properties*/
	private LoginInfo loginInfo;
	
	/**Reference to controller which holds menu functions
	 * 
	 */
	private MainContainerController mainContainerController;
	
	/** Reference to instance of class which is in charge for Rmi calls
	 * and socket communication.
	*/
	private RmiContainer rmiContainer;
	
	/**Reference to instance of class in charge for Socket connection
	 * 
	 */
	private SocketContainer socketContainer;

	/** Reference to input stream of property file so it can be properly closed on service canceling. */
	private InputStream in;

	/** Fields that hold connection parameters*/
	private String serverAddress, loginObjectBindingName;
	private int portRmi,portSocket;
	private SecurityLoginInterface loginObject;

	private BorderPane mainContainerPane;
	
	private DriverOverviewController driverOverviewController;
	private VehicleOverviewController vehicleOverviewController;
	private GprsDevicesOverviewController gprsDevicesController;
	
	/** List of Gprs device imeis which positions are added on map pane*/
	private ObservableList<String> observedGprsDevicesImeiList= FXCollections.observableArrayList();
	
	private Task<Void> navigationTask;

	public static void main(String[] args) throws Exception { launch(args); }
	
	/**
	 * Starts ConnectService and opens splash stage.
	 * 
	 *  {@inheritDoc}
	 */
	@Override 
	public void start(final Stage initStage) throws Exception {
		//Saves reference to initStage
		this.initStage=initStage;
		
		//Creates instances of classes that handles Rmi and Socket connections
		this.rmiContainer= new RmiContainer();
		this.socketContainer=new SocketContainer();
 
		
		//Creates service that loads properties and establish connection to Rmi server
		ConnectService service=new ConnectService();
		service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent workerStateEvent) {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}	
			}
		});
		service.start();
		
		//Initialize initStage
		try{
			this.initMainWindow();
		} catch (IOException e) {
			StaticJobs.showExceptionAlert(e, splashStage);
		}
		
		//Shows splash
		showSplash();

	   //Stops service if splash screen gets closed.
		splashStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				service.cancel();
			}
		});
	 }
	/** Method for calling LoginController which opens login stage.
	 * showLogin method returns true if login attempt succeeds. In that case application proceed to 
	 * another stage. If login stage is closed with no user signed in, initStage is closed and
	 * application ends.*/
	private void showLoginStage() {
	    socketContainer.setConnectionStateHolder(this);
		if(new LoginController().showLogin(rmiContainer, socketContainer, loginObject, loginInfo)){

			 showMainWindow();
		}else{
			initStage.close();
		}	
	}
	private void showMainWindow(){
		this.driverOverviewController.reloadTableOnRunLater();
		this.vehicleOverviewController.reloadTableOnRunLater();
		this.gprsDevicesController.reloadTableOnRunLater();
		initStage.show();
	}
	/** Method for initializing initStage after successful sign in.
	 * The scene that is set to initStage holds MainContainer (BorderPane) in it.
	 * MainContainer holds TabPane and DriverOverview, VehicleOverview, GprsOverview, Statistics and MapPane are placed in separate tabs.
	 */
	private void initMainWindow() throws IOException{
		//Adds icon to stage
		initStage.getIcons().add(new Image("/resources/logo_image.png"));   
		
		//Handling socket closing on application end.
		initStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				socketContainer.disconnectSocket();
			}
		});
	    initStage.setTitle("RouteDetector");
	    initStage.getIcons().add(new Image("/resources/logo_image.png"));
	    
	    //Create status bar
	    StatusBar statusBar=new StatusBar();
	    statusBar.setProgressText("");
	    statusBar.getProgress().setProgress(0);
	    
	    // Load Vehicle overview.
	  	FXMLLoader newLoader = new FXMLLoader();  
	    newLoader.setLocation(LoginController.class.getResource("VehiclesOverview.fxml"));
	    BorderPane vehiclePane = (BorderPane) newLoader.load();       
	    vehicleOverviewController = newLoader.getController();
	    vehicleOverviewController.setRmiContainer(rmiContainer);
	    vehicleOverviewController.setMotherWindow(initStage);
	    vehicleOverviewController.setStatusBar(statusBar);
	    
	    //Loading Driver overview
	  	FXMLLoader secondLoader = new FXMLLoader();  
	    secondLoader.setLocation(LoginController.class.getResource("DriversOverview.fxml"));	    
	    BorderPane driverPane = (BorderPane) secondLoader.load();
	    driverOverviewController = secondLoader.getController();
	    driverOverviewController.setRmiContainer(rmiContainer);
	    driverOverviewController.setMotherWindow(initStage);
	    driverOverviewController.setStatusBar(statusBar);
	    
	    //Loading Map Overview
	  	FXMLLoader thirdLoader = new FXMLLoader();  
	    thirdLoader.setLocation(LoginController.class.getResource("MapPane.fxml"));
	    BorderPane mapPane = (BorderPane) thirdLoader.load();
	    MapPaneController mapPaneController = thirdLoader.getController();
	    mapPaneController.setRmiContainer(rmiContainer);    
	    mapPaneController.setMotherWindow(initStage);
	    mapPaneController.setStatusBar(statusBar);
	    
	    //Loading GprsDevices Overview
	  	FXMLLoader fourthLoader = new FXMLLoader();  
	    fourthLoader.setLocation(LoginController.class.getResource("GprsDevicesOverview.fxml"));
	    BorderPane gprsPane = (BorderPane) fourthLoader.load();

	    gprsDevicesController = fourthLoader.getController();
	    gprsDevicesController.setRmiContainer(rmiContainer);
	    gprsDevicesController.setMotherWindow(initStage);   
	    gprsDevicesController.setStatusBar(statusBar);
	    gprsDevicesController.setSocketContainer(socketContainer);
	    gprsDevicesController.setConnectionStateHolder(this);

	    //Loading GprsDevices Overview
	  	FXMLLoader fifthLoader = new FXMLLoader();  
	  	fifthLoader.setLocation(LoginController.class.getResource("StatisticsOverview.fxml"));
	    BorderPane statisticsPane = (BorderPane) fifthLoader.load();
	    StatisticsOverviewController statisticsController = fifthLoader.getController();
	    statisticsController.setRmiContainer(rmiContainer);
	    statisticsController.setMotherWindow(initStage);   
	    statisticsController.setStatusBar(statusBar);
	    statisticsController.loadStatisticsView();
	    
		// Load main container overview.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LoginController.class.getResource("MainContainer.fxml"));
		mainContainerPane = (BorderPane) loader.load();
		
		mainContainerController = loader.getController();
	    mainContainerController.getVehicleTab().setContent(vehiclePane);
	    mainContainerController.getDriverTab().setContent(driverPane);
	    mainContainerController.getMapTab().setContent(mapPane);
	    mainContainerController.getGprsTab().setContent(gprsPane);
	    mainContainerController.getStatisticsTab().setContent(statisticsPane);
		mainContainerController.setRmiContainer(rmiContainer);
		mainContainerController.setMotherStage(initStage);
	    mainContainerController.setSocketContainer(socketContainer);
	    //Add overviews to MainContainerController for menu-view connection
	    mainContainerController.setDriverOverviewController(driverOverviewController);
	    mainContainerController.setVehicleOverviewController(vehicleOverviewController);
	    mainContainerController.setStatusBar(statusBar);
	    
	    //Setting tab selection listener
	    mainContainerController.getTabs().getSelectionModel().selectedItemProperty().addListener(
		    new ChangeListener<Tab>() {
				@Override
				public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
					if(oldValue==mainContainerController.getGprsTab())
						if(gprsDevicesController.areChangesMade())
							if(StaticJobs.showApplyConfirmationDialog(initStage))
								gprsDevicesController.applyChanges();
							
					if(newValue==mainContainerController.getVehicleTab()){
						if(navigationTask!=null && navigationTask.isRunning())
							navigationTask.cancel();
						
						navigationTask= new Task<Void>()  {
							@Override
							protected Void call() throws Exception {
								vehicleOverviewController.reloadTableOnRunLater();
								return null;							
							}
						};
				        new Thread(navigationTask).start();
				        
					}else if(newValue==mainContainerController.getDriverTab()){
						if(navigationTask!=null && navigationTask.isRunning())
							navigationTask.cancel();
						
						navigationTask= new Task<Void>()  {
							@Override
							protected Void call() throws Exception {
								driverOverviewController.reloadTableOnRunLater();
								return null;							
							}
						};
				        new Thread(navigationTask).start();	
					}else if(newValue==mainContainerController.getGprsTab()){
						if(navigationTask!=null && navigationTask.isRunning())
							navigationTask.cancel();
						
						navigationTask= new Task<Void>()  {
							@Override
							protected Void call() throws Exception {
								gprsDevicesController.reloadTableOnRunLater();
								return null;							
							}
						};
				        new Thread(navigationTask).start();	
					}else if(newValue==mainContainerController.getStatisticsTab()){
						if(navigationTask!=null && navigationTask.isRunning())
							navigationTask.cancel();
						
						navigationTask= new Task<Void>()  {
							@Override
							protected Void call() throws Exception {
								statisticsController.loadStatisticsViewOnRunLater();
								return null;							
							}
						};
				        new Thread(navigationTask).start();		
					}
				}
		    }
		);
	    //Set containers
	    socketContainer.setMotherStage(initStage);
	    socketContainer.setStatusBar(statusBar);
	    socketContainer.setMapCanvas(mapPaneController);
	    socketContainer.setGprsDevicesController(gprsDevicesController);
	    rmiContainer.setDriverTab(mainContainerController.getDriverTab());
	    rmiContainer.setVehicleTab(mainContainerController.getVehicleTab());
	    rmiContainer.setGprsTab(mainContainerController.getGprsTab());
	
	    //show main stage
	    Scene scene = new Scene(mainContainerPane);
		mainContainerPane.getStylesheets().add("/resources/style.css");
	    initStage.setScene(scene);
	
	}
	/** Method for setting and showing splash stage.
	 * Sets on-close action to cancel the service if user closes the splash screen.
	 * 
    * @param  ConnectService instance reference to stop the service if splash screen gets closed.
	*/

	private void showSplash() {
		int SPLASH_WIDTH = 200;
		int SPLASH_HEIGHT = 80;
	  
		Label sceneTitle = new Label("RouteDetector");
		sceneTitle.getStyleClass().add("label-bright");
		StackPane titleBar= new StackPane();
		titleBar.getStyleClass().add("background");
		titleBar.getChildren().add(sceneTitle);
	  
	  //Ads status bar below
		StatusBar status=new StatusBar();
		status.setProgressText("Loading...");
	  
		VBox splashLayout = new VBox();
		splashLayout.getChildren().addAll(titleBar, status);
	  
		Scene splashScene = new Scene(splashLayout);
		splashScene.getStylesheets().add("/resources/style.css");
      
		splashStage=new Stage();
	  
		splashStage.initStyle(StageStyle.UNDECORATED);
      
	 //Sets position of splash screen
		final Rectangle2D bounds = Screen.getPrimary().getBounds();
		splashStage.setScene(splashScene);
		splashStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		splashStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);

		splashStage.getIcons().add(new Image("/resources/logo_image.png"));

		splashStage.show();
	}
	/** Method for closing splash screen.
	*/
	private void hideSplash() {
		if(splashStage!=null)
			splashStage.close(); 
	}
	/** Nested Class that extends Service class. It handles loading of properties files, setting
	*  system properties (security, trust-store and key-store linking...) and makes connection
	*  to Rmi registry.
	*/
	class ConnectService extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>()  {

				/** {@inheritDoc}*/
				@Override
				protected Void call() throws Exception {
					try{
						try {
							//Loads the properties
  			          		Properties prop = new Properties();
	  			          	in = getClass().getClassLoader().getResourceAsStream("properties/config.properties");
	  			          	prop.load(in);
	  			          	in.close();
	  			          	
	  			        	serverAddress = prop.getProperty("serverAddress");
	  			        	loginObjectBindingName = prop.getProperty("loginObjectBindingName");
	  			        	portRmi = Integer.parseInt(prop.getProperty("portRmi"));
	  			        	portSocket = Integer.parseInt(prop.getProperty("portSocket"));
	  			    		prop.clear();
	
	  			          	//in = new FileInputStream("D:/ostava/properties/login.properties");
	  			    		in = new FileInputStream("./properties/login.properties");
	  			          	prop.load(in);
	  			          	in.close();
	  			          	loginInfo=new LoginInfo(prop.getProperty("user"), prop.getProperty("comp"),null);
	  			    		prop.clear();
	  			    		
	  			    		in = getClass().getClassLoader().getResourceAsStream("properties/rmi.properties");
	  			    		
	  			    		//System properties setting
	  			    	    prop = System.getProperties();
	  			    	    prop.load(in);
	  			    	    in.close();
	  			    	    System.setProperties(prop);
	  			    	    
	  			    	    //System.setProperty("javax.net.ssl.keyStore","D:/ostava/properties/keystore.jks");
	  			      	    //System.setProperty("javax.net.ssl.trustStore","D:/ostava/properties/cacerts");
	  			    	    //System.setProperty("java.security.policy", "D:/ostava/properties/security.properties");
	
	  			    	    System.setProperty("javax.net.ssl.keyStore","./properties/keystore");
	  			      	    System.setProperty("javax.net.ssl.trustStore", "./properties/cacerts");
	  			    	    //System.setProperty("java.security.policy", "./properties/security.properties");
	  			    	    
	  			    	    //Enable RC4 algorithm (by excluding it from disabled ones)
	  			    	    java.security.Security.setProperty("jdk.tls.disabledAlgorithms","SSLv3, DH keySize < 768");
	  			    	    prop=null;
						}catch (IOException e) {
							e.printStackTrace();
							StaticJobs.showExceptionAlertOnRunLater(e, initStage);
							hideSplashRunLater();
							initStage.close();
							return null;
						}finally {
							if (in != null) {
								try {
									in.close();
								} catch (IOException e) {
									e.printStackTrace();
									StaticJobs.showExceptionAlertOnRunLater(e, initStage);
									hideSplashRunLater();
									initStage.close();
									return null;
								}
							}
						}
						Registry registry = LocateRegistry.getRegistry(serverAddress, portRmi, new SslRMIClientSocketFactory());

						loginObject = (SecurityLoginInterface) registry.lookup(loginObjectBindingName);
						showLoginStageRunLater();
	  				}catch(RemoteException e) {
						e.printStackTrace();
						StaticJobs.showExceptionAlertOnRunLater(e, initStage);
	  					hideSplashRunLater();
	  			    	initStage.close();	
	  				} catch (NotBoundException e1) {
						e1.printStackTrace();
						StaticJobs.showExceptionAlertOnRunLater(e1, initStage);
	  					hideSplashRunLater();
	  			    	initStage.close();
	  				}
	  				return null;
	  			}
			};
		}

		/** Hides splash on main thread.*/
		private void hideSplashRunLater(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {   
					hideSplash();
				}
			});
		}
		/** Initializes main window, show login stage on main thread and hides splash window. */
		private void showLoginStageRunLater(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() { 
					hideSplash(); 
					showLoginStage();
					
				}
			});
		}
	}
	@Override
	public void setConnected(boolean isConnected) {
		this.isConnected=isConnected;
		mainContainerController.setConnectedItemValue(isConnected);
	}

	@Override
	public boolean getConnected() {
		return this.isConnected;
	}

	@Override
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	
	@Override
	public void setLoginInfo(LoginInfo loginInfo){
		this.loginInfo=loginInfo;
	}

	@Override
	public int getSocketPort() {
		return portSocket;
	}

	@Override
	public String getHost() {
		return serverAddress;
	}

	@Override
	public ObservableList<String> getObservedGprsDevicesImeiList() {
		return observedGprsDevicesImeiList;
	}

	@Override
	public void setObservedGprsDevicesImeiList(ObservableList<String> listOfItems) {
		observedGprsDevicesImeiList=listOfItems;
	}

}