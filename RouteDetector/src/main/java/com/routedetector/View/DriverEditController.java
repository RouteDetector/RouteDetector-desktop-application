package com.routedetector.View;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.routedetector.AssetsJPA.Driver;
import com.routedetector.AssetsJPA.DriverGroup;
import com.routedetector.Client.AutoCompleteTextListener;
import com.routedetector.Client.CollectionManipulation;
import com.routedetector.Client.DriverWrapper;
import com.routedetector.Client.StaticJobs;

import javafx.stage.FileChooser;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

/**
 * Class that acts as controller for driver editing view. 
 * 
 * @author  Danijel Sudimac
 * @version  1.0.0
*/
public class DriverEditController {

    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField classes;
    @FXML
    private Label currentVehicle;
    @FXML
    private TextField mobilePhone;
    @FXML
    private TextField tachographNumber;
    @FXML
    private DatePicker employmentDate;
    @FXML
    private TextField contractType;
    @FXML
    private TextField department;
    @FXML
    private TextArea experience;
    @FXML
    private TextField driverLicense;
    @FXML
    private DatePicker expiryDate;
    @FXML
    private TextField contactName;
    @FXML
    private TextField contactPhoneNumber;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField idNumber;
    @FXML
    private TextField postCode;
    @FXML
    private TextField fullName;
    @FXML
    private TextField country;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private TextField city;	    
    @FXML
    private TextField maritalStatus;
    @FXML
    private TextArea address;
    @FXML
    private RadioButton editableButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ImageView imageView;
    
    /**
     * Reference of stage that view is placed into for alert owner setting.
     */
    private Stage dialogStage;
    /**
     * Driver object which fields are edited.
     */
    private Driver driver;
    
    /**Lists and maps references for isInputValid method checking.
     * Driver name must be unique, and driver group must be existing one.
     */    
    private Map<String, DriverWrapper> driverWrappers;
    private ObservableList<String> driverGroupNames;
    
    private boolean saveClicked;
    private boolean isNew;
    
    /**
     * Sets object's fields from view before saving and closing the stage.
     */
    @FXML
	private void handleSave() {
    	
	    if (isInputValid()) {
	    	if(this.password.getText()!=null || !this.password.getText().isEmpty()){
		        try {
		            // Create MessageDigest instance for MD5
		            MessageDigest md = MessageDigest.getInstance("MD5");
		            //Add password bytes to digest
		            md.update(this.password.getText().trim().getBytes());
		            //Get the hash's bytes
		            byte[] bytes = md.digest();
		            //This bytes[] has bytes in decimal format;
		            //Convert it to hexadecimal format
		            StringBuilder sb = new StringBuilder();
		            for(int i=0; i< bytes.length ;i++)
		            {
		                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		            }
		            //Get complete hashed password in hex format
	
			        driver.getDriverMobileTerminal().setPassword(sb.toString());
		        }
		        catch (NoSuchAlgorithmException e)
		        {
		            e.printStackTrace();
		        }
	    	}
	        
	        driver.getDriverProfessionalData().setDepartment(this.department.getText().trim());        
	        driver.setName(this.name.getText().trim());
	        driver.getDriverPersonalData().setAddress(this.address.getText().trim());
	        driver.getDriverPersonalData().setCity(this.city.getText().trim());
	        driver.getDriverEmergency().setContactName(this.contactName.getText().trim());
	        driver.getDriverEmergency().setContactPhoneNumber(this.contactPhoneNumber.getText().trim());
	        driver.getDriverProfessionalData().setContractType(this.contractType.getText().trim());
	        driver.getDriverPersonalData().setCountry(this.country.getText().trim());
	        if(this.dateOfBirth.getValue()!=null) driver.getDriverPersonalData().setBirthDate(Date.from(this.dateOfBirth.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        if(this.employmentDate.getValue()!=null) driver.getDriverProfessionalData().setEmploymentDate(Date.from(this.employmentDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        if(this.expiryDate.getValue()!=null) driver.getDriverLicense().setExpiryDate(Date.from(this.expiryDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        driver.getDriverLicense().setNumber(this.driverLicense.getText().trim());
	        driver.getDriverProfessionalData().setExperience(this.experience.getText().trim());
	        driver.getDriverPersonalData().setFullName(this.fullName.getText().trim());
	        driver.getDriverLicense().setClasses(this.classes.getText().trim());
	        driver.getDriverPersonalData().setIdNumber(this.idNumber.getText().trim());
	        driver.getDriverPersonalData().setMaritalStatus(this.maritalStatus.getText().trim());
	        driver.setMobilePhone(this.mobilePhone.getText().trim());
	        driver.getDriverMobileTerminal().setUsername(this.username.getText().trim());
	        driver.getDriverPersonalData().setPostCode(this.postCode.getText().trim());
	        driver.setTachographDriverCard(this.tachographNumber.getText().trim());
	        
	        if(imageView.getImage()!=null){
	        	BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
		    	ByteArrayOutputStream s = new ByteArrayOutputStream();
		    
		    	try {
					ImageIO.write(bImage, "png", s);
			    	driver.setImage(s.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}else{
	    		driver.setImage(null);
	    	}
	        saveClicked = true;
	        dialogStage.close();
	    }
	}
    /**
     * Adds listener to Radio Button.
     */
	@FXML
    private void initialize() {
		loadButton.defaultButtonProperty().bind(loadButton.focusedProperty());
		deleteButton.defaultButtonProperty().bind(deleteButton.focusedProperty());
		saveButton.defaultButtonProperty().bind(saveButton.focusedProperty());
		cancelButton.defaultButtonProperty().bind(cancelButton.focusedProperty());
		
		editableButton.setOnAction(new EventHandler<ActionEvent>() {
			 
			public void handle(ActionEvent e) { 
				if(editableButton.isSelected()) setEditable(true);
				else setEditable(false);
			}
		});
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializing view using passed parameters.
     * 
     * @param driver is selected Driver object
     * @param vehicles is list of available vehicles
     * @param driverGroups is list of all DriverGroup objects
     * @param isNew is for setting editable button on/off
     */
    public void setDriver(DriverWrapper driverWrapper,Map<String, DriverWrapper> driverWrappers, ObservableList<DriverGroup> driverGroups, boolean isNew) {
    	this.driverWrappers=driverWrappers;
    	this.driver=driverWrapper.getDriver();
        if(driverWrapper.getCurrentVehiclePlateNumber()!=null) this.currentVehicle.setText(driverWrapper.getCurrentVehiclePlateNumber());
        if(driver.getDriverProfessionalData().getDepartment()!=null)this.department.setText(driver.getDriverProfessionalData().getDepartment());        

        driverGroupNames=CollectionManipulation.getListOfStringFromCollection(driverGroups);
        new AutoCompleteTextListener( this.department, driverGroupNames);
        
        if(driver.getName()!=null) this.name.setText(driver.getName());
        if(driver.getDriverPersonalData().getAddress()!=null)this.address.setText(driver.getDriverPersonalData().getAddress());
        if(driver.getDriverPersonalData().getCity()!=null)this.city.setText(driver.getDriverPersonalData().getCity());
        if(driver.getDriverEmergency().getContactName()!=null)this.contactName.setText(driver.getDriverEmergency().getContactName());
        if(driver.getDriverEmergency().getContactPhoneNumber()!=null)this.contactPhoneNumber.setText(driver.getDriverEmergency().getContactPhoneNumber());
        if(driver.getDriverProfessionalData().getContractType()!=null)this.contractType.setText(driver.getDriverProfessionalData().getContractType());
        if(driver.getDriverPersonalData().getCountry()!=null)this.country.setText(driver.getDriverPersonalData().getCountry());
        if(driver.getDriverPersonalData().getBirthDate()!=null)this.dateOfBirth.setValue(driver.getDriverPersonalData().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if(driver.getDriverProfessionalData().getEmploymentDate()!=null)this.employmentDate.setValue(driver.getDriverProfessionalData().getEmploymentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if(driver.getDriverLicense().getExpiryDate()!=null)this.expiryDate.setValue(driver.getDriverLicense().getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if(driver.getDriverLicense().getNumber()!=null)this.driverLicense.setText(driver.getDriverLicense().getNumber());
        if(driver.getDriverProfessionalData().getExperience()!=null)this.experience.setText(driver.getDriverProfessionalData().getExperience());
        if(driver.getDriverPersonalData().getFullName() !=null)this.fullName.setText(driver.getDriverPersonalData().getFullName());
        if(driver.getEMail() !=null)this.email.setText(driver.getEMail());
        if(driver.getDriverLicense().getClasses() !=null)this.classes.setText(driver.getDriverLicense().getClasses());
        if(driver.getDriverPersonalData().getIdNumber() !=null)this.idNumber.setText(driver.getDriverPersonalData().getIdNumber());
        if(driver.getDriverPersonalData().getMaritalStatus() !=null)this.maritalStatus.setText(driver.getDriverPersonalData().getMaritalStatus());

        if(driver.getMobilePhone() !=null)this.mobilePhone.setText(driver.getMobilePhone());
        if(driver.getDriverMobileTerminal().getPassword()!=null)this.password.setText(driver.getDriverMobileTerminal().getPassword());
        if(driver.getDriverMobileTerminal().getUsername()!=null)this.username.setText(driver.getDriverMobileTerminal().getUsername());
        if(driver.getDriverPersonalData().getPostCode() !=null)this.postCode.setText(driver.getDriverPersonalData().getPostCode());
        if(driver.getTachographDriverCard() !=null)this.tachographNumber.setText(driver.getTachographDriverCard());
        
        if(driver.getImage()!=null){
			// convert BufferedImage to byte array
        	InputStream in = new ByteArrayInputStream(driver.getImage());
			try {
				BufferedImage bImageFromConvert = ImageIO.read(in);
	        	imageView.setImage(SwingFXUtils.toFXImage(bImageFromConvert, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        this.isNew=isNew;
        editableButton.setSelected(isNew);
        setEditable(isNew);
    }
    public boolean isSaveClicked() {
        return saveClicked;
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    /**
     * Checks if view has invalid input.
     * 
     * @return true if its ok.
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if(isNew){
	        if(this.name.getText()==null || this.name.getText().isEmpty()){
	    		this.name.setStyle("-fx-border-color: red;");
	            errorMessage += "Driver's name must not be null!\n"; 	        	
	        }
	        if(driverWrappers.get(this.name.getText().trim().toLowerCase())!=null){
	    		this.name.setStyle("-fx-border-color: red;");
	            errorMessage += "Driver's name must be unique!\n"; 	        	
	        }
	        if (!driverGroupNames.contains(this.department.getText()) && (this.department.getText()!=null && !this.department.getText().trim().isEmpty())) {
	            errorMessage += "Department must be selected from the listed ones!\n"; 
	    		this.department.setStyle("-fx-border-color: red;");
	        }
	        if (this.email.getText() != null && !this.email.getText().isEmpty()) {
	        	try{
	        		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	        				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	    	        if (!pattern.matcher(this.email.getText()).matches()) {
	    	            throw new IllegalArgumentException("Invalid String");
	    	        }
	    	        driver.setEMail(this.email.getText());
	        	}catch(IllegalArgumentException e){
	        		this.email.setStyle("-fx-border-color: red;");
		            errorMessage += "No valid input for driver's email!\n"; 
	        	}
	        }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    /**
     * Enables/Disables almost all controls in view
     * 
     * @param true if its editable.
     */
    public void setEditable(boolean b) {
        this.name.setEditable(b);
        this.mobilePhone.setEditable(b);
        this.tachographNumber.setEditable(b);
        this.email.setEditable(b);
        this.driverLicense.setEditable(b);
        this.classes.setEditable(b);
        this.expiryDate.setEditable(b);
        this.username.setEditable(b);
        this.password.setEditable(b);
        this.contactName.setEditable(b);
        this.contactPhoneNumber.setEditable(b);
        this.employmentDate.setEditable(b);
        this.contractType.setEditable(b);
        this.department.setEditable(b);
        this.experience.setEditable(b);
        this.idNumber.setEditable(b);
        this.fullName.setEditable(b);
        this.dateOfBirth.setEditable(b);
        this.address.setEditable(b);
        this.postCode.setEditable(b);
        this.country.setEditable(b);
        this.city.setEditable(b);
        this.maritalStatus.setEditable(b);
        this.name.setEditable(b);
        this.saveButton.setDisable(!b);
        
        this.loadButton.setDisable(!b);
        this.deleteButton.setDisable(!b);
        
        if(!isNew) this.name.setDisable(true);
        	
        if(b){
        	editableButton.setStyle("-fx-text-fill:black;");        	
        }else{
        	editableButton.setStyle("-fx-text-fill:red;");
        	StaticJobs.addBlinkingAnimation(editableButton);
        }
    }
    /**
     * Opens FileChooser and loads image to ImageView
     * 
     */
    @FXML
    public void loadImage(){
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Load driver image from file");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
        	Image image = new Image(file.toURI().toString());
        	imageView.setImage(image);
        }    	
    }
    @FXML
    public void removeImage(){
       imageView.setImage(null);	    	
    }
}
