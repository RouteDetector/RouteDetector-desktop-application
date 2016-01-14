package com.routedetector.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Class that acts as controller for DeviceBox view. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class DeviceBoxController {

    @FXML
    private Label licensePlate;
    @FXML
    private Label driver;
    @FXML
    private Label imei;
    @FXML
    private Button showButton;
        
    /**Reference to StatisticsOverviewController for triggering changes in view content.*/
    private ChartHolder chartHolder;
    
	private Stage motherStage;
	@FXML
	private void initialize(){
		showButton.defaultButtonProperty().bind(showButton.focusedProperty());
	}
    /**Calls ChartHolder method for chart showing.*/
	@FXML
	public void showChart(){
		chartHolder.showChart(imei.getText());
    }
	public Stage getMotherWindow() {
		return motherStage;
	}
	public void setMotherWindow(Stage motherStage) {
		this.motherStage = motherStage;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate.setText(licensePlate);
	}
	public void setDeviceInformation( String imei, String licensePlateNumber, String driver){
		this.licensePlate.setText(licensePlateNumber);
		this.imei.setText(imei);
		this.driver.setText(driver);
	} 
    public ChartHolder getChartHolder() {
		return chartHolder;
	}
	public void setHistogramHolder(ChartHolder chartHolder) {
		this.chartHolder = chartHolder;
	}
}
