package com.routedetector.Client;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
/**
* Class that extends BorderPane, consists of Label on left side and ProgessBar on right.
* It also has containers on far left and far right for adding buttons or similar.
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class StatusBar extends BorderPane{
	/**
	* Components placed inside
	*/	
	private ProgressBar progress=new ProgressBar();
	private Label progressText=new Label();
	private BorderPane container =new BorderPane();
	private HBox leftContainer=new HBox(5);
	private HBox rightContainer=new HBox(5);
	public ProgressBar getProgress() {
		return progress;
	}

	public Label getProgressLabel() {
		return progressText;
	}

	public void setProgressText(String progressText) {
		this.progressText.setText(progressText);
	}
	/**
	* Initialization of components in constructor
	*/	
	public StatusBar(){
		progressText.setPadding(new Insets(0,5,0,5));
		progress.setPadding(new Insets(5,5,5,5));
		container.setLeft(progressText);
		leftContainer.setAlignment(Pos.CENTER_LEFT);
		leftContainer.setPadding(new Insets(0,5,0,5));
		container.setRight(progress);
		this.setCenter(container);
		this.setLeft(leftContainer);
		this.setRight(rightContainer);
	}
	/**
	* Setting button style added to the left
	* 
	* @param button Button added to the left
	*/	
	public void addLeft(Button button){
		button.getStyleClass().add("picture-button");
		button.setTooltip(new Tooltip("Socket connection closed!"));
		button.getStyleClass().add("transparent");
		button.getStyleClass().add("warning");
		leftContainer.getChildren().add(button);
	}
	public void addRight(Node n){
		rightContainer.getChildren().add(n);
	}
	public void removeLeft(Node n){
		leftContainer.getChildren().remove(n);
	}
	public void removeRight(Node n){
		rightContainer.getChildren().remove(n);
	}
	public boolean isOnRight(Node n){
		return rightContainer.getChildren().contains(n);
	}
	public boolean isOnLeft(Node n){
		return leftContainer.getChildren().contains(n);
	}
}
