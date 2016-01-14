package com.routedetector.Client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Callable;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Tab;

/**
 * This class contains exclusively static methods for alert initialization and showing, 
 * and methods that handle animation.
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class StaticJobs {
	/**
	 *Shows alert with expandable content which consists of TextField with exception stack trace.
	 * 
	 * @param  exception is Exception that occurred.
	 * @param  owner is the parent Stage.
	 */
    public static void showExceptionAlert(Exception exception,Stage owner){
    	Alert alert = new Alert(AlertType.ERROR);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
    	alert.setTitle("RouteDetector Exception");
    	alert.setHeaderText("An Exception occured during application processing.");
    	alert.setContentText(exception.getMessage());

    	// Create expandable Exception.
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	exception.printStackTrace(pw);
    	String exceptionText = sw.toString();
    	
    	Label label = new Label("The exception stacktrace:");

    	TextArea textArea = new TextArea(exceptionText);
    	textArea.setEditable(false);
    	textArea.setWrapText(true);

    	textArea.setMaxWidth(Double.MAX_VALUE);
    	textArea.setMaxHeight(Double.MAX_VALUE);
    	GridPane.setVgrow(textArea, Priority.ALWAYS);
    	GridPane.setHgrow(textArea, Priority.ALWAYS);

    	GridPane expContent = new GridPane();
    	expContent.setMaxWidth(Double.MAX_VALUE);
    	expContent.add(label, 0, 0);
    	expContent.add(textArea, 0, 1);

    	// Set expandable Exception into the dialog pane.
    	alert.getDialogPane().setExpandableContent(expContent);
    	 
    	alert.showAndWait();
    }
	/**
	 *Showing alert when object could not be saved, updated or removed via Rmi.
	 * 
	 * @param  exception is String exception message.
	 * @param  owner is the parent Stage.
	 */
    public static void showProcessAlert(String exception,Stage owner){
	    Alert alertTwo = new Alert(AlertType.WARNING);
    	try{
    		alertTwo.initOwner(owner);
    	}catch(Exception e){
    	}
	    alertTwo.setTitle("Process could not be completed!");
	    alertTwo.setHeaderText(exception);
	    alertTwo.setContentText("Possibile cause: The object cannot be updated because it has changed or been deleted since it was last read. "
	    		+ "If object is public Gprs device, cause can be that your company is not owner of the device.");
	    alertTwo.showAndWait();
    }
	/**
	 *Showing alert when object is not selected.
	 * 
	 * @param  exception is String exception message.
	 * @param  owner is the parent Stage.
	 */
    public static void showNoSelectionAlert(String object,Stage owner){
        Alert alert = new Alert(AlertType.WARNING);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
        alert.setTitle("No Selection");
        alert.setHeaderText("No "+object+" Selected");
        alert.setContentText("Please select a "+object+" in the table.");

        alert.showAndWait();
    }
	/**
	 *Shows apply confirmation alert.
	 * 
	 * @param  owner is the parent Stage.
     * @return boolean value if confirmed
	 */
    public static boolean showApplyConfirmationDialog(Stage owner){    
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
    	alert.setTitle("Confirmation dialog!");
    	alert.setHeaderText("Changes are made in list of observed gprs devices.");
    	alert.setContentText("Do you want to apply changes made in list?");

        return (alert.showAndWait().get()== ButtonType.OK);
    }
    /**
	 *Shows deletion confirmation alert.
	 * 
	 * @param  object is String type or name of the object.
	 * @param  owner is the parent Stage.
     * @return boolean value if confirmed
	 */
    public static boolean showDeleteConfirmationDialog(String object,Stage owner){    
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
    	alert.setTitle("Confirmation dialog!");
    	alert.setHeaderText("Delete selected "+object+"!");
    	alert.setContentText("Are you sure you want to delete selected "+object+"?");

        return (alert.showAndWait().get()== ButtonType.OK);
    }
	/**
	 *Shows reconnection confirmation dialog.
	 * 
	 * @param  owner is the parent Stage.
     * @return boolean value if confirmed
	 */
    public static boolean showReconnectionConfirmationDialog(Stage owner){    
        Alert alert = new Alert(AlertType.CONFIRMATION);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
        alert.setTitle("Server connection");
        alert.setHeaderText("Server connection stream is closed.");
        alert.setContentText("You are not receiving position information from the server. If you want start the service, click on connect button.");

        ButtonType buttonTypeOne = new ButtonType("Connect");
        ButtonType buttonTypeTwo = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == buttonTypeOne);
    }
    public static void showApplySucceededInformation(Stage owner){
        Alert alert = new Alert(AlertType.INFORMATION);
    	try{
    		alert.initOwner(owner);
    	}catch(Exception e){
    	}
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Gprs devices list sent to server");
        alert.setContentText("For those gprs devices defined in list, server will start sending position messages!");

        alert.showAndWait();
    	
    }
	public static void showApplySucceededInformationOnRunLater(Stage stage){
		Platform.runLater(new Runnable() {
			@Override
			public void run() { 
				showApplySucceededInformation(stage);
			}
		});
	}
	public static void showExceptionAlertOnRunLater(Exception e, Stage stage){
		Platform.runLater(new Runnable() {
			@Override
			public void run() { 
				showExceptionAlert(e, stage);
			}
		});
	}
	public static void showProcessAlertOnRunLater(String e, Stage stage){
		Platform.runLater(new Runnable() {
			@Override
			public void run() { 
				showProcessAlert(e, stage);
			}
		});
	}
	 public static void addBlinkingAnimationToTabOnRunLater(Node node, Tab tab) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() { 
				addBlinkingAnimationToTab(node, tab);
			}
		});
	 }
	/**
	 *Adds blinking animation to control.
	 * 
	 * @param node is Node object which blinks.
	 */
	 public static void addBlinkingAnimation(Node node) {

	    final Timeline timeline = new Timeline();
	    timeline.setCycleCount(5);
	    timeline.setAutoReverse(true);
	    final KeyValue kv = new KeyValue(node.opacityProperty(), 0.0);
	    final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
	    timeline.getKeyFrames().add(kf);
	    timeline.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				node.setOpacity(1);
			}
	    	
	    });
	    timeline.play();
	  }
	/**
	 *Adds blinking animated control to tab.
	 * 
	 * @param node is Node object which blinks.
	 * @param tab is Tab object.
	 */
	 public static void addBlinkingAnimationToTab(Node node, Tab tab) {
	    final Timeline timeline = new Timeline();
		tab.setGraphic(node);
	    timeline.setCycleCount(5);
	    timeline.setAutoReverse(true);
	    final KeyValue kv = new KeyValue(node.opacityProperty(), 0.0);
	    final KeyFrame kf = new KeyFrame(Duration.millis(700), kv);
	    timeline.getKeyFrames().add(kf);
	    timeline.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				tab.setGraphic(null);
			}
	    	
	    });
	    timeline.play();
	  }
	 public static void changeBackgroundColor(Node node) {
		node.opacityProperty().set(1);
		final Color startColor = Color.web("#FFFACD");
		final Color endColor = Color.web("#F5F5F5");
		
		final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(startColor);

		// String that represents the color above as a JavaFX CSS function:
		// -fx-body-color: rgb(r, g, b);
		// with r, g, b integers between 0 and 255
		
		final StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.format("-fx-background-color: rgb(%d, %d, %d);", 
                        (int) (256*color.get().getRed()), 
                        (int) (256*color.get().getGreen()), 
                        (int) (256*color.get().getBlue()));
            }
        }, color);
		
		// bind the button's style property
        node.styleProperty().bind(cssColorSpec);
        
        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
                new KeyFrame(Duration.seconds(2), new KeyValue(color, endColor)));

        
        timeline.play();
	 }
	 public static void fadeOutBackground(Node node) {
		node.opacityProperty().set(0.5);		 
	 }
}