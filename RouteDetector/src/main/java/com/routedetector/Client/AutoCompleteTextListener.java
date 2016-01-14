package com.routedetector.Client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
	* This class is implementation of ChangeListener<String>.
	* It shows popup window on KeyPressed Event.
	* It should be used with TextField and ObservableList of String items which will be put into popup.
	* 
	* <p>First TextFiled and list should be created and 
	* then new AutoCompleteTextField with them as arguments.
	* 
	* @author  Danijel Sudimac
	* @version  1.0.0
*/
public class AutoCompleteTextListener implements ChangeListener<String>{
	/** The auto-complete entries. */
	private final SortedSet<String> entries;
	  
	/** The popup used to select an entry. */
	private Popup entriesPopup;
	  
	/** The base text field. */
	private TextField field;
	  
	/** The ListView placed in popup. */
	private ListView<String> listView ;
	  
	
	/** The row height and max row number in ListView. */
	private final int ROW_HEIGHT = 24;
	private final int MAX_ROW_NUM = 5;
	  
	/** Construct a new AutoCompleteTextField. 
	 * Puts reference to components, initializes ListView and adds listeners.
	 * Also sets initial height of ListView. Sets auto-hide to true.
	 * 
	 * @param  field the TextField to be typed in.
	 * @param  list is the collection of items String type.
	 */
	public AutoCompleteTextListener(TextField field, ObservableList<String> list){
	    this.field=field;
	    field.setPromptText("Click to expand");
	    entries = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	    entries.addAll(list);
	    
	    entriesPopup = new Popup();
	    listView = new ListView<String>();
	    listView.setItems(list);
	    
	    if(list.size()>MAX_ROW_NUM)
	    	listView.setPrefHeight(MAX_ROW_NUM * ROW_HEIGHT +6);
	    else if(list.size()==0)
	    	listView.setPrefHeight(0);
	    else if(list.size()==1){
	    	listView.setPrefHeight(list.size() * ROW_HEIGHT);
	    	listView.getSelectionModel().select(0);
	    }else
	    	listView.setPrefHeight(list.size() * ROW_HEIGHT +6);
	
	    entriesPopup.setAutoHide(true);
	    
	    //show popup on TextField click
	    field.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	showPopup();
	        }
	    });

	    //Insert selected item to TextField on Enter pressed on ListView
	    listView.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				
		        if(field.editableProperty().get() && event.getCode() == KeyCode.ENTER) {
		        	if(listView.getSelectionModel().getSelectedItem()!=null)
		        		field.setText( listView.getSelectionModel().getSelectedItem());
		        	entriesPopup.hide();
		        }
			}    	
	    });
	
	    //Insert selected item to TextField on Click on ListView
	    listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	field.setText( listView.getSelectionModel().getSelectedItem());
	        	entriesPopup.hide();
	        }
	    });
	    
	    //Insert ListView into popup	
	    entriesPopup.getContent().add(listView);
	    
	    //Adds listener to TextFiled
	    field.textProperty().addListener(this);
	    
	    //On focus changed hide popup
	    field.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
		    	entriesPopup.hide();
		    }
	    });
	}

	/**
	 * Get the existing set of autocomplete entries.
	 * @return The existing autocomplete entries.
	 */

	/**
	 * Populate the entry set with the given search results.  Display is limited to MAX_ROW_NUM entries.
	 * @param searchResult The List of matching strings.
	 */
	private void populatePopup(List<String> searchResult) {
		ObservableList<String> list = FXCollections.observableArrayList(searchResult);
		listView.setItems(list);
      
		if(list.size()>MAX_ROW_NUM)
			listView.setPrefHeight(MAX_ROW_NUM * ROW_HEIGHT +6);
		else if(list.size()==0)
			listView.setPrefHeight(0);
		else if(list.size()==1){
			listView.getSelectionModel().select(0);
			listView.setPrefHeight(list.size() * ROW_HEIGHT);
		}else
			listView.setPrefHeight(list.size() * ROW_HEIGHT + 6);
	}
	/**
	 * Shows popup below TextField. Sets width of component to fit the TextField width.
	 */
	private void showPopup(){
	    if(listView.getItems().isEmpty()) return;
		
	    Point2D point = field.localToScene(0.0, 0.0);
	    Window window = field.getScene().getWindow();
	    
	    entriesPopup.setX(window.getX() + point.getX()+3);
	    entriesPopup.setY(window.getY() + point.getY() + 50); 
	
	    listView.setPrefWidth(field.getWidth());
	    listView.setMinWidth(field.getWidth());
	    listView.setMaxWidth(field.getWidth());
	    
	    entriesPopup.show(window);
	}

	  /**
	   * Overrides ChangeListener interface method filling searchResult list and calling populate Popup.
	   * {@inheritDoc}
	   */
	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	    if (field.getText().length() == 0){
	    	entriesPopup.hide();
	    } else{
		    LinkedList<String> searchResult = new LinkedList<>();
		    searchResult.addAll(entries.subSet(field.getText(), field.getText() + Character.MAX_VALUE));
		    
		    if (entries.size() > 0){
		        populatePopup(searchResult);
		        if (!entriesPopup.isShowing()){
		        	showPopup();
		        }
		    } else{
			    entriesPopup.hide();
		    }
	    }
	}
}