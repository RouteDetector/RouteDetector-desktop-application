package com.routedetector.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.routedetector.AssetsJPA.VehiclePosition;
import com.routedetector.Client.GprsDeviceWrapper;
import com.routedetector.Client.RmiContainer;
import com.routedetector.Client.StatusBar;

import extfx.scene.chart.DateAxis;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/**
 * This class is in charge for adding Chart content to application. It is a controller 
 * for StatisticsOverview view.
 * 
 * @author  Danijel Sudimac
*/
public class StatisticsOverviewController implements ChartHolder{

	@FXML
    private HBox topBox;
	@FXML
    private VBox leftPanel;
	@FXML
	private BorderPane panel;
	
	private LineChart<Date, Number> chart;
	
	private Task<Void> navigationTask;
    private StatusBar statusBar;
    private String currentGprsImei;
    private List<VehiclePosition> currentChartData=new ArrayList<VehiclePosition>();
	/**
	 * References to Rmi handler class
	 */
	private RmiContainer rmiContainer;
	private Stage motherStage;
	
	/**Number of minutes for which records are shown*/
	private long range=30;
	
	private long period;
	
    private DateAxis dateAxis;
	public StatusBar getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
	
    public StatisticsOverviewController(){
    }

	/**
	 * Initializes reload button.
	 */	
	@FXML
    private void initialize() {
        Button button=new Button();
        button.setTooltip(new Tooltip("Reload map"));
        button.getStyleClass().add("picture-button");
        button.getStyleClass().add("transparent");
        button.getStyleClass().add("reload-button");
		button.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	    		loadStatisticsView();
	        }
	    });
		topBox.getChildren().add(button);
		
	}
	/**
	 * Lists all gprs devices and calls createDeviceBoxPanel
	 */
	public void loadStatisticsView(){
		leftPanel.getChildren().clear();
	    Iterator<Map.Entry<String,GprsDeviceWrapper>> it = rmiContainer.getGprsData().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,GprsDeviceWrapper> pair = (Map.Entry<String,GprsDeviceWrapper>)it.next();
	        createDeviceBoxPanel(pair.getKey(), pair.getValue().getVehicleLicensePlate(), pair.getValue().getDriverName());
	    }
		statusBar.setProgressText("Statistics overview opened.");
	}
	/**
	 * Calls loadStatisticsView on main thread.
	 */
	public void loadStatisticsViewOnRunLater(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loadStatisticsView();
			}
		});
	}
	/**
	 * Creates DeviceBoxPanel and ads it to the leftPanel
	 * 
	 * @param imei is gprs device id
	 * @param licensePlateNumber is vehicle identification number shown in box
	 * @param driverName is name of driver shown in box 
	 */
	public void createDeviceBoxPanel(String imei, String licensePlateNumber, String driverName){
        try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(DriverOverviewController.class.getResource("DeviceBox.fxml"));
			GridPane panel = (GridPane) loader.load();
			DeviceBoxController controller=loader.getController();
			
			controller.setDeviceInformation(imei, licensePlateNumber, driverName);
			controller.setHistogramHolder(this);
			controller.setMotherWindow(motherStage);
			
			leftPanel.getChildren().add(panel);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public RmiContainer getRmiContainer() {
		return rmiContainer;
	}
	public void setRmiContainer(RmiContainer rmiContainer) {
		this.rmiContainer = rmiContainer;
	}
	public Stage getMotherWindow() {
		return motherStage;
	}
	public void setMotherWindow(Stage motherStage) {
		this.motherStage = motherStage;
	}
	/**
	 * Calls RmiContainer to get list of last vehicle positions in new thread.
	 * 
	 * @param imei
	 */
	@Override
	public void showChart(String imei) {
		
		if(navigationTask!=null && navigationTask.isRunning())
			navigationTask.cancel();
		
		navigationTask= new Task<Void>()  {
			@Override
			protected Void call() throws Exception {
				currentChartData.addAll(rmiContainer.getVehiclePositionsForWholePeriod(imei, motherStage));
				currentGprsImei=imei;
				createChartOnRunLater();
				return null;							
			}
		};
        new Thread(navigationTask).start();	
		
	}
	/** Fills the chart with data*/
	private void createChartOnRunLater(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
			    ObservableList<XYChart.Data<Date, Number>> seriesData = FXCollections.observableArrayList();
			    
			    Iterator<VehiclePosition> iterator=currentChartData.iterator();
			    while(iterator.hasNext()) {
			    	VehiclePosition pos=iterator.next();
			    	if(!pos.getIsConnected()){
			    		if(!currentChartData.isEmpty()){
			    			series.add(new XYChart.Series<>("Continous gprs position data series", seriesData));
			    			seriesData=FXCollections.observableArrayList();
			    		}
			    	}else{
			    		seriesData.add(new XYChart.Data<Date, Number>(pos.getTime(), pos.getSpeed()));
			    	}
			    }
			    if(!seriesData.isEmpty())
	    			series.add(new XYChart.Series<>("Continous gprs position data series", seriesData));

			    NumberAxis numberAxis = new NumberAxis();
			    
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(currentChartData.get(0).getTime());
			    cal.add(Calendar.MINUTE, - (int)range);
			    Date dateBefore30Minutes = cal.getTime();
			    
			    long diffMs = currentChartData.get(0).getTime().getTime() - currentChartData.get(currentChartData.size()-1).getTime().getTime();
			    period = diffMs/ 1000/ 60;
			    
			    dateAxis = new DateAxis(dateBefore30Minutes,currentChartData.get(0).getTime());
			    dateAxis.setLabel("Time");
			    numberAxis.setLabel("Speed km/h");
			    chart = new LineChart<>(dateAxis, numberAxis);
			    chart.getData().addAll(series);
			    chart.setTitle("Vehicle positions for gprs device with imei:"+currentGprsImei+ " for period: "+currentChartData.get(0).getTime()+" - "+currentChartData.get(currentChartData.size()-1).getTime());
			
			    VBox box=new VBox();
			    box.setPadding(new Insets(5));
			    box.getChildren().add(chart);
			    
			    if(currentChartData.size()>10){    
				    Slider slider = new Slider();
					slider.setMin(0);
					slider.setMax(1);
					slider.setValue(1);
					slider.setPrefWidth(100);
					slider.setMaxWidth(100);
			        slider.valueProperty().addListener(new ChangeListener<Number>() {
			            public void changed(ObservableValue<? extends Number> ov,
			                Number old_val, Number new_val) {
			            	setBounds(new_val.doubleValue());
			            }
			        });
			        box.getChildren().add(new Label("Move to another period by sliding."));
				    box.getChildren().add(slider);
				    
				    if(period>30){
					    Slider sliderTwo = new Slider();
						sliderTwo.setMin(0);
						sliderTwo.setMax(1);
						sliderTwo.setValue(0);
						sliderTwo.setPrefWidth(100);
						sliderTwo.setMaxWidth(100);
				        sliderTwo.valueProperty().addListener(new ChangeListener<Number>() {
				            public void changed(ObservableValue<? extends Number> ov,
				                Number old_val, Number new_val) {
				            		range=((Double)((period-30)*new_val.doubleValue())).longValue();
				            		setBounds(slider.getValue());
				            }
				        });
				        box.getChildren().add(new Label("Change range width by sliding."));	
					    box.getChildren().add(sliderTwo);
				    }
			    }
			    panel.setCenter(box);
			}
		});

	}
	private void setBounds(double value){
    	int row=((Double)((currentChartData.size()-1)*(1-value))).intValue();

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(currentChartData.get(row).getTime());
	    int bound= (int)range/2;
	    cal.add(Calendar.MINUTE, - bound);
	    Date dateBefore = cal.getTime();

	    cal.setTime(currentChartData.get(row).getTime());
	    cal.add(Calendar.MINUTE, + bound);
	    Date dateAfter = cal.getTime();
	    
        dateAxis.setLowerBound(dateBefore);
        dateAxis.setUpperBound(dateAfter);
	}
}
