package com.routedetector.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Optional;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.View.GprsDevicesOverviewController;
import com.routedetector.View.MapCanvas;

import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
* This class is the place where Socket connection establish. 
* Socket connection is located in nested class which extends Service class and reference is maintained in
* one of the field, so the connection can be easily managed.
 * 
 * @author  Danijel Sudimac
*/
public class SocketContainer {
	/**Date format message sent by server.*/
	private static String datePattern ="yyyy-MM-dd HH:mm:ss.SSSSSS";
	
	/**Reference to MapPaneController to set vehicle position on new message received from server.*/
	private MapCanvas map;
	/**Reference to Gprs devices overview container to reload observed gprs devices list.
	 * 
	 */
	private GprsDevicesOverviewController gprsDevicesController;
	
	/** Reference to service nested class instance.*/
	private SocketService service;

	/**Reference to Main class, used to enable/disable menu item for connection management.*/
	private ConnectionStateHolder stateHolder;
	
	/** References to stream objects.*/
	private ObjectInputStream ois;
    private ObjectOutputStream  oos;

	/** Reference to SSLSocket.*/
	private SSLSocket sslSocket;

	/** 
	 * Reference to status bar object. It is used to show warning button in status bar if 
	 * connection breaks.
	 */
    private StatusBar statusBar;

	/** Reference to initStage for setting Alert parent.*/
	private Stage motherStage;
	
	/**Button object which is added in status bar on connection failure.*/
    private Button buttonAlert = new Button();

    /**Method for setting list of gprs device imeis for which the positions should be received*/
    public void sendMessageToServer(){
		map.removeAll();    
       	Thread thread=new Thread() {
            public void run() {
        		try {
        			ClientRequest request=new ClientRequest();
        			Iterator<String> iterator=stateHolder.getObservedGprsDevicesImeiList().iterator();
        			while(iterator.hasNext()){
        				String imei=iterator.next();
        				request.getImeiList().add(imei);
        			}
        			if(oos!=null){
        				oos.writeObject(request);
        				setStatusOnRunLater("Updated list of gprs devices for which positions should be received.");
        				StaticJobs.showApplySucceededInformationOnRunLater(motherStage);
        			}else{
        				setStatusOnRunLater("Socket output stream closed.");       				
        			}
        		} catch (IOException e) {
        			setStatusOnRunLater("Failed to update list of gprs devices for which positions should be received.");
        			e.printStackTrace();
        		}
            }
        };
        thread.setDaemon(true);
        thread.start();
        	
    }
	/**Method for showing confirmation dialog for socket reconnection.*/
	public void changeSocketConnection() {
        if (StaticJobs.showReconnectionConfirmationDialog(motherStage)){
        	connectSocketDialog();
        }
	}

	/**Method for socket closing.*/
    public void disconnectSocket(){
		try {
			if(sslSocket!=null)sslSocket.close();
			service.cancel();
		} catch (IOException e) {
			StaticJobs.showExceptionAlert(e,motherStage);
		}

		setSocketConnection(false);
    }

	/**
	 * Shows login dialog with one password field. User's username and organization name are already
	 * saved in this stage. If password is entered it calls startSocket method.
	 */
    private void connectSocketDialog(){
    	Dialog<String> dialog = new Dialog<>();
    	dialog.setTitle("Login Dialog");
    	dialog.setHeaderText("Please login before re-starting connection");

    	// Set the icon
    	dialog.setGraphic(new ImageView("/resources/login.png"));

    	// Set the button types.
    	ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

    	// Create the container for password field and label.
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	PasswordField password = new PasswordField();
    	password.setPromptText("Password");
    	
    	grid.add(new Label("User password:"), 0, 0);
    	grid.add(password, 1, 0);

    	// Enable/Disable login button depending on whether a password was entered.
    	Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    	loginButton.setDisable(true);
    	
    	password.textProperty().addListener((observable, oldValue, newValue) -> {
    	    loginButton.setDisable(newValue.trim().isEmpty());
    	});
    	dialog.initOwner(motherStage);
    	dialog.getDialogPane().setContent(grid);
    	
    	Platform.runLater(() -> password.requestFocus());
    	
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){    	    
    		stateHolder.getLoginInfo().setPassword(password.getText().toCharArray());
        	startSocket();
    	}
    }

	/**
	 * Sets the field which represent state of connection and calls setButtonInStatusBar method.
	 *  
	 * @param isConnected boolean value which is true if socket connection is created.
	*/
    private void setSocketConnection(boolean isConnected){
    	stateHolder.setConnected(isConnected);

    	
    	// If socket is connected, warning button is removed, if not, it is added to status bar.
    	 //Also adds on click functionality to button.
    	
    	if(isConnected){
    		if(statusBar.isOnLeft(buttonAlert)){
    			statusBar.removeLeft(buttonAlert);
    			statusBar.setProgressText("Socket connected.");
    		}
    	}else{
    		if(!statusBar.isOnLeft(buttonAlert)){
    			buttonAlert.setOnAction(new EventHandler<ActionEvent>() {
	    	        public void handle(ActionEvent e) {
	    	        	changeSocketConnection();
	    	        }
	    	    });
	    		statusBar.addLeft(buttonAlert);
    			statusBar.setProgressText("Socket disconnected.");
    			
	    		StaticJobs.addBlinkingAnimation(buttonAlert);
	    	}	    	    		
    	}
    }
    private void setSocketConnectionOnRunLater(boolean isConnected){
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	setSocketConnection( isConnected);
            }
        });
    }
    
    //Getters and setters
	public SocketService getService() {
		return service;
	}
	public void setService(SocketService service) {
		this.service = service;
	}
	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
    public Stage getMotherStage() {
		return motherStage;
	}
	public void setMotherStage(Stage motherStage) {
		this.motherStage = motherStage;
	}
	public MapCanvas getMapCanvas() {
		return map;
	}
	public void setMapCanvas(MapCanvas map) {
		this.map = map;
	}
	public static String getDatePattern(){
		return datePattern;
	}
    private void setStatusOnRunLater(String message) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	statusBar.setProgressText(message);
            }
        });
    }
	/**
	 * Initializes SocketService object and starts it.
	 * 
	 * @param password char[].
	 * @param company is company's registration email address.
	*/
	public void startSocket(){
		service=new SocketService();
		//When service shut down is called, it must close all streams.
		service.setOnCancelled(new EventHandler<WorkerStateEvent>() {
		   @Override
		   public void handle(WorkerStateEvent workerStateEvent) {
		
			   try {
					if(sslSocket!=null) sslSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
		      
		  });
		service.start();
    }
	public void setConnectionStateHolder(ConnectionStateHolder stateHolder) {
		this.stateHolder = stateHolder;
	}
	public GprsDevicesOverviewController getGprsDevicesController() {
		return gprsDevicesController;
	}
	public void setGprsDevicesController(GprsDevicesOverviewController gprsDevicesController) {
		this.gprsDevicesController = gprsDevicesController;
	}
	/**
	* Nested class that extends Service java thread safe class. 
	* It opens ssl socket connection to RouteDetector server and calls setSocketConnection method to 
	* update boolean field that holds connection state.
	 * 
	*/
    public class SocketService extends Service<Void> {
		
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
    				try{
						setSocketConnectionOnRunLater(true);
						
    	        	    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    	        	    sslSocket = (SSLSocket) sslsocketfactory.createSocket(stateHolder.getHost(), stateHolder.getSocketPort());
    					
    					OutputStream os=sslSocket.getOutputStream();
    					InputStream is=sslSocket.getInputStream();
    					
    					ois = new ObjectInputStream(is);
    					oos=new ObjectOutputStream(os);
    					
    					map.removeAllOnRunLater();
    					try{
    						
    						oos.writeObject(stateHolder.getLoginInfo());
     
    						stateHolder.getLoginInfo().setPassword(null);
    						
       			            while (true) {
       			            	VehiclePosition v;
       							try {
       								v=(VehiclePosition) ois.readObject();
            						handleSocketObject(v);
       							} catch (ClassNotFoundException e) {
       								e.printStackTrace();
       							} 
       			            }
    					}finally{
    						sslSocket.close();
    					}
    				
    				} catch (UnknownHostException e) {
    					e.printStackTrace();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    				finally{
						setSocketConnectionOnRunLater(false);
						map.disableAllOnRunLater();
						gprsDevicesController.reloadTableOnRunLater();
    					stateHolder.getObservedGprsDevicesImeiList().clear();
    					try{
    						if(sslSocket.isConnected())
    							sslSocket.close();
    					}catch(IOException e){
    					}
    				}
    				return null;
                }
            };
        }
        /**
         * Calls method for handling new vehicle position information.
         * 
         * @param input is VehiclePosition serialized object
        */
        private void handleSocketObject(VehiclePosition v){
            map.initializeNewVehiclePositionOnRunLater(v);
        }
    }
}