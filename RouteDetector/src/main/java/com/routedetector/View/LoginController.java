package com.routedetector.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import com.routedetector.Basic.SecurityLoginInterface;
import com.routedetector.Basic.SecurityServerInterface;
import com.routedetector.Client.SocketContainer;
import com.routedetector.Client.StageStarter;
import com.routedetector.Client.StaticJobs;
import com.routedetector.Client.StatusBar;
import com.routedetector.Client.LoginInfo;
import com.routedetector.Client.RmiContainer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * Class which creates login view stage, creates stub object for RMI communication,
 * makes initial RMI calls.
 * 
 * @author Danijel Sudimac
 *
 */
public class LoginController {
	private StageStarter stageStarter;
	
	private RmiContainer rmiContainer;
	private SocketContainer socketContainer;

    private boolean isLogedIn = false;

	private SecurityLoginInterface loginObject;
	
	private TextField organizationTextField;
	private TextField userTextField;
    private Text actiontarget;
	private PasswordField pwBox;
    private StatusBar status;
    private GridPane grid ;
    private Task<Void> task;	
    private Stage loginStage;
	private LoginInfo loginInfo;
	
    
	public LoginController(){
	}
	/**
	 * Creates task, tries to login, makes first rmi with communication stub object.
	 * Calls afterLogin on end.
	 */
    private void tryLogin(){
    	task=new Task<Void>() {
            @Override
            protected Void call() throws Exception {
    	        try {
    				updateProgress(0.1,1);
    				updateMessage("Trying to login...");
			    	try{
	    			    SecurityServerInterface theServer = (SecurityServerInterface) loginObject.login(userTextField.getText(), pwBox.getText(), organizationTextField.getText());
	    	  	    	loginInfo.setUser(userTextField.getText());
	    	  	    	loginInfo.setEmail(organizationTextField.getText());
	    	  	    	loginInfo.setPassword(pwBox.getText().toCharArray());
	    			    //saveProperties();
	    			    updateProgress(0.4,1);
	    				updateMessage("Starting socket!");
	    		        socketContainer.startSocket();
	    				updateProgress(0.5,1);
	    				updateMessage("Retrieving vehicle's data...");
	    				rmiContainer.setCommunicator(theServer);
	    				rmiContainer.setVehicleInfo(null);
	    				updateProgress(0.6,1);
	    				updateMessage("Retrieving driver's data...");
	    				rmiContainer.setDriverInfo(null);
	    				updateProgress(0.7,1);
	    				updateMessage("Retrieving gprs devices data...");
	    				rmiContainer.setGprsDevicesInfo(null);
	    				updateProgress(0.8,1);
	    				updateMessage("Retrieving context data...");
	    				rmiContainer.setTypesInfo(null);
	    				updateProgress(0.9,1);
	    				updateMessage("Initialising stage...");
	    				
	    		        afterLogin(true,null);
	    	    	}catch (RemoteException e1) {
	    	        	e1.printStackTrace();
	    				updateMessage("Login failed.");
	    				updateProgress(0,1);
	    				afterLogin(false,"Connection failure...");
	    				
	    			}catch (LoginException e2) {
	    	        	e2.printStackTrace();
	    				updateMessage("Login failed.");
	    				updateProgress(0,1);
	    				afterLogin(false,"Incorrect input data...");
	    			}
    	        }catch (Exception e) {
    	        	e.printStackTrace();
    				updateMessage("Login failed.");
    				updateProgress(0,1);
    				afterLogin(false,"Connection failure...");
		    	}
            return null;
            }
        };

    	status.getProgressLabel().textProperty().bind(task.messageProperty());
    	status.getProgress().progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    	startProgress();
    }
    private void startProgress() {
        grid.setDisable(true);
    }
    private void stopProgress() {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	if(isLogedIn){
                	closeLogin();
            	}else{
            		grid.setDisable(false);
            	}
            }
        });
    }
    private void afterLogin(boolean success,String comment){
		if(success){
			isLogedIn=true;
			showMainWindowOnRunLater();
			
		}else{
			isLogedIn=false;
			actiontarget.setFill(Color.FIREBRICK);
			actiontarget.setText(comment);
		}
		stopProgress();
    }
    private void showMainWindowOnRunLater(){
    	  Platform.runLater(new Runnable() {
              @Override
              public void run() {
          		try{
          			stageStarter.initMainWindow();
          			stageStarter.showMainWindow();
          			closeLogin();
        		} catch (IOException e) {
        			StaticJobs.showExceptionAlert(e, loginStage);
        		}
              }
          });

    }

    private void closeLogin(){
    	loginStage.close();
    }
    /**
     * Initializes login dialog.
     * 
     * @param rmiContainer reference to RmiContainer object instance which holds all RMI call methods
     * @param socketContainer reference to SocketContainer object instance
     * @param loginObject stub for RMI login object
     * @param loginInfo holds login information
     * @return returns true if login success
     */
    public boolean showLogin(RmiContainer rmiContainer, SocketContainer socketContainer, SecurityLoginInterface loginObject, LoginInfo loginInfo, StageStarter stageStarter){
    	this.stageStarter=stageStarter;
    	this.loginInfo=loginInfo;
    	this.rmiContainer=rmiContainer;
    	this.socketContainer=socketContainer;
        this.loginObject=loginObject;
        
	    BorderPane root;
		
	    loginStage=new Stage();
		
        loginStage.setResizable(false);
        loginStage.setTitle("Welcome to RouteDetector");
       // initStage.initStyle(StageStyle.UNDECORATED);
        root = new BorderPane();
        
	    grid = new GridPane();
	    grid.setAlignment(Pos.CENTER);
	    grid.setHgap(10);
	    grid.setVgap(5);
	    grid.setPadding(new Insets(15, 7, 15, 7));

	    Label sceneTitle = new Label("Login: ");
	    sceneTitle.getStyleClass().add("label-bright");
	    StackPane titleBar= new StackPane();
	    titleBar.getStyleClass().add("background");
	    titleBar.getChildren().add(sceneTitle);
	    
	    Label organizationName = new Label("Company's email:");
	    grid.add(organizationName, 0, 1);

	    organizationTextField = new TextField();
	    grid.add(organizationTextField, 1, 1);
	    
	    Label userName = new Label("User Name:");
	    grid.add(userName, 0, 2);

	    userTextField = new TextField();
	    grid.add(userTextField, 1, 2);

	    Label pw = new Label("Password:");
	    grid.add(pw, 0, 3);

	    pwBox = new PasswordField();
	    
	    pwBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent keyEvent) {
	            if (keyEvent.getCode() == KeyCode.ENTER)  {
	            	tryLogin();
	            }
	        }
	    });
	    
	    grid.add(pwBox, 1, 3);
	    Button btn = new Button("Login");
	    HBox hbBtn = new HBox(10);
	    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	    hbBtn.intersects(5, 0, 0, 0);
	    Button btnC = new Button("Cancel");
	    hbBtn.getChildren().add(btn);
	    hbBtn.getChildren().add(btnC);
	    grid.add(hbBtn, 1, 4);
	            	    
	    actiontarget = new Text();
	    grid.add(actiontarget, 1, 6);
	    
	    btn.defaultButtonProperty().bind(btn.focusedProperty());
	    btnC.defaultButtonProperty().bind(btnC.focusedProperty());
	    
	    btn.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	tryLogin();
	        }
	    });
	    btnC.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	isLogedIn=false;
	        	loginStage.close();
	        }
	    });
	    root.setTop(titleBar);
	    root.setCenter(grid);
	    status=new StatusBar();
	    status.setProgressText("Waiting for user input...");
	    status.getProgress().setProgress(0);
        root.setBottom(status);
        Scene scene=new Scene(root);           
		scene.getStylesheets().add("/resources/style.css");
		
    	userTextField.setText(loginInfo.getUser());
    	organizationTextField.setText(loginInfo.getEmail());

    	loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	    @Override
    	    public void handle(WindowEvent event) {
    	    	if(task!=null)
    	    		task.cancel();
    	    }
    	});
        loginStage.setScene(scene);
        loginStage.getIcons().add(new Image("/resources/logo_image.png"));  
        
	    loginStage.showAndWait();
	    	    
	    return isLogedIn;
    }
    /**
     * Saves login info to properties file.
     */
  /*  private void saveProperties(){
    	Properties prop;
    	try{
    		//InputStream in = new FileInputStream("D:/ostava/properties/login.properties");
    		//OutputStream os=new FileOutputStream("D:/ostava/properties/login.properties");
    		InputStream in = new FileInputStream("./properties/login.properties");
    		//OutputStream os=new FileOutputStream("./properties/login.properties");
		    	
    		String s=System.getProperty("user.dir");
		    File f=new File(s+"/properties/login.properties");
		    f.setWritable(true);
		    	f.setExecutable(true);
    		//File f=new File(getClass().getResource("/properties/login.properties").toString());
    		//InputStream in= getClass().getClassLoader().getResourceAsStream("properties/login.properties");
    		OutputStream os=new FileOutputStream(f);
			try {
			  	prop = new Properties();          		  	
	  			
	  	    	prop.load(in);
	  	    	prop.setProperty("user",userTextField.getText());
	  	    	prop.setProperty("comp",organizationTextField.getText());
	  	    	prop.store(os, null);
	  	    	prop.clear();
	  		}finally {
	  			os.close();
	  			in.close();
	  		}
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  			StaticJobs.showExceptionAlertOnRunLater(e,null);
  		} catch (IOException e) {
  			e.printStackTrace();
			StaticJobs.showExceptionAlertOnRunLater(e,null);
  		}
    }*/
}
