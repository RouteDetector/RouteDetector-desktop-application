package com.routedetector.AssetsJPA;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;


@Entity
@NamedQuery(name="findAllVehicles", query="SELECT v FROM Vehicle v")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="vehicle_id_seq",
                   sequenceName="vehicle_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="vehicle_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name="acquisition_date")
	private Date acquisitionDate;

	@Column(name="chronometer_h")
	private Integer chronometerH;

	@Column(name="current_co_driver")
	private String currentCoDriver ;

	@Column(name="current_driver")
	private String currentDriver;

	@Column(name="engine_type")
	private String engineType;
	
	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	@Column(name="engine_number")
	private String engineNumber;

	@Column(name="license_plate")
	private String licensePlate;

	@Column(name="odometer_km")
	private Integer odometerKm;

	@Column(name="tracking_id")
	private String trackingId;

	@Column(name="vehicle_identification_number")
	private String vehicleIdentificationNumber;

	@Column(name="vehicle_manufacturer")
	private String vehicleManufacturer;

	@Column(name="vehicle_model")
	private String vehicleModel;

	@Column(name="vehicle_group")
	private String vehicleGroup;
	
	public String getVehicleGroup() {
		return vehicleGroup;
	}

	public void setVehicleGroup(String vehicleGroup) {
		this.vehicleGroup = vehicleGroup;
	}

	@Column(name="vehicle_picture")
	private byte[] vehiclePicture;

	@Column(name="vehicle_type")
	private String vehicleType;
	@Version
	private Long version;

	@Column(name="year_of_make")
	private Integer yearOfMake;

	//bi-directional one-to-one association to FuelInfo
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fuel_info_id")
	private FuelInfo fuelInfo;

	//bi-directional one-to-one association to MobileTerminal
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="mobile_terminal_id")
	private MobileTerminal mobileTerminal;

	//bi-directional one-to-one association to OtherVehicleInfo
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="more_info_id")
	private OtherVehicleInfo otherVehicleInfo;

	//bi-directional one-to-one association to VehicleCost
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="costs_info_id")
	private VehicleCost vehicleCost;

	//bi-directional one-to-one association to VehicleEquipment
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="equipment_info_id")
	private VehicleEquipment vehicleEquipment;

	//bi-directional one-to-one association to VehicleLoadspace
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="loadspace_info_id")
	private VehicleLoadspace vehicleLoadspace;

	public Vehicle() {
	}

	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty();
		s.set(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty id) {
		this.id = id.get();
	}

	public ObjectProperty<LocalDate> getAcquisitionDateProperty() {
		if(this.acquisitionDate==null)this.acquisitionDate=new Date();
		ObjectProperty<LocalDate> date=new SimpleObjectProperty<LocalDate>();
		date.set(this.acquisitionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return date;
	}

	public void setAcquisitionDateProperty(ObjectProperty<LocalDate> acquisitionDateProperty) {
		this.acquisitionDate = Date.from(acquisitionDateProperty.get().atStartOfDay(ZoneId.systemDefault()).toInstant());

	}
	public IntegerProperty getChronometerProperty() {
		if(this.chronometerH==null) this.chronometerH=0;
		IntegerProperty s=new SimpleIntegerProperty(this.chronometerH);
		return s;
	}

	public void setChronometerHProperty(IntegerProperty chronometer) {
		this.chronometerH = chronometer.get();
	}

	public StringProperty getCurrentCoDriverProperty() {
		if(this.currentCoDriver==null) this.currentCoDriver=new String();
		StringProperty s=new SimpleStringProperty(this.currentCoDriver);
		return s;
	}
	public void setEngineTypeProperty(StringProperty type) {
		this.engineType = type.get();
	}

	public StringProperty getEngineTypeProperty() {
		if(this.engineType==null) this.engineType=new String();
		StringProperty s=new SimpleStringProperty(this.engineType);
		return s;
	}	
	public void setCurrentCoDriverProperty(StringProperty currentCoDriver) {
		this.currentCoDriver = currentCoDriver.get();
	}

	public StringProperty getCurrentDriverProperty() {
		if(this.currentDriver==null) this.currentDriver=new String();
		StringProperty s=new SimpleStringProperty(this.currentDriver);
		return s;
	}

	public void setCurrentDriverProperty(StringProperty currentDriver) {
		this.currentDriver = currentDriver.get();
	}

	public StringProperty getEngineNumberProperty() {
		if(this.engineNumber==null) this.engineNumber=new String();
		StringProperty s=new SimpleStringProperty(this.engineNumber);
		return s;
	}

	public void setEngineNumberProperty(StringProperty engine) {
		this.engineNumber = engine.get();
	}

	public StringProperty getLicensePlateProperty() {
		if(this.licensePlate==null) this.licensePlate=new String();
		StringProperty s=new SimpleStringProperty(this.licensePlate);
		return s;
	}

	public void setLicensePlateProperty(StringProperty license) {
		this.licensePlate = license.get();
	}

	public IntegerProperty getOdometerKmProperty() {
		if(this.odometerKm==null) this.odometerKm=0;
		IntegerProperty s=new SimpleIntegerProperty(this.odometerKm);
		return s;
	}

	public void setOdometerKmProperty(IntegerProperty odometer) {
		this.odometerKm = odometer.get();
	}

	public StringProperty getTrackingIdProperty() {
		if(this.trackingId==null) this.trackingId=new String();
		StringProperty s=new SimpleStringProperty(this.trackingId);
		return s;
	}

	public void setTrackingIdProperty(StringProperty tracking) {
		this.trackingId = tracking.get();
	}

	public StringProperty getVehicleIdentificationNumberProperty() {
		if(this.vehicleIdentificationNumber==null) this.vehicleIdentificationNumber=new String();
		StringProperty s=new SimpleStringProperty(this.vehicleIdentificationNumber);
		return s;
	}

	public void setVehicleIdentificationNumberProperty(StringProperty vehicleIdentification) {
		this.vehicleIdentificationNumber = vehicleIdentification.get();
	}

	public StringProperty getVehicleManufacturerProperty() {
		if(this.vehicleManufacturer==null) this.vehicleManufacturer=new String();
		StringProperty s=new SimpleStringProperty(this.vehicleManufacturer);
		return s;
	}

	public void setVehicleManufacturerProperty(StringProperty vehicleManufacturerProp) {
		this.vehicleManufacturer = vehicleManufacturerProp.get();
	}

	public StringProperty getVehicleModelProperty() {
		if(this.vehicleModel==null) this.vehicleModel=new String();
		StringProperty s=new SimpleStringProperty(this.vehicleModel);
		return s;
	}

	public void setVehicleModelProperty(StringProperty vehicleModelProp) {
		this.vehicleModel = vehicleModelProp.get();
	}

	public ObjectProperty<byte[]> getVehiclePictureProperty() {
		if(this.vehiclePicture==null) this.vehiclePicture= new byte[0];
		ObjectProperty<byte[]> s=new SimpleObjectProperty<byte[]>(this.vehiclePicture);
		return s;
	}

	public void setVehiclePictureProperty(ObjectProperty<byte[]> vehiclePictureProp) {
		this.vehiclePicture = vehiclePictureProp.get();
	}

	public StringProperty getVehicleTypeProperty() {
		if(this.vehicleType==null) this.vehicleType=new String();
		StringProperty s=new SimpleStringProperty(this.vehicleType);
		return s;
	}

	public void setVehicleTypeProperty(StringProperty vehicleTypeProperty) {
		this.vehicleType = vehicleTypeProperty.get();
	}

	public IntegerProperty getYearOfMakeProperty() {
		if(this.yearOfMake==null) this.yearOfMake=0;
		IntegerProperty date=new SimpleIntegerProperty(this.yearOfMake);
		return date;
	}

	public void setYearOfMakeProperty(IntegerProperty yearOfMakeProp) {
		this.yearOfMake = yearOfMakeProp.get();
	}

	public StringProperty getVehicleGroupProperty() {
		if(this.vehicleGroup==null) this.vehicleGroup=new String();
		StringProperty s=new SimpleStringProperty(this.vehicleGroup);
		return s;
	}

	public void setVehicleGroupProperty(StringProperty vehicleGroupProp) {
		this.vehicleGroup = vehicleGroupProp.get();
	}	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAcquisitionDate() {
		return this.acquisitionDate;
	}

	public void setAcquisitionDate(Date acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}

	public Integer getChronometerH() {
		return this.chronometerH;
	}

	public void setChronometerH(Integer chronometerH) {
		this.chronometerH = chronometerH;
	}

	public String getCurrentCoDriver() {
		return this.currentCoDriver;
	}

	public void setCurrentCoDriver(String currentCoDriver) {
		this.currentCoDriver = currentCoDriver;
	}

	public String getCurrentDriver() {
		return this.currentDriver;
	}

	public void setCurrentDriver(String currentDriver) {
		this.currentDriver = currentDriver;
	}

	public String getEngineNumber() {
		return this.engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getOdometerKm() {
		return this.odometerKm;
	}

	public void setOdometerKm(Integer odometerKm) {
		this.odometerKm = odometerKm;
	}

	public String getTrackingId() {
		return this.trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getVehicleIdentificationNumber() {
		return this.vehicleIdentificationNumber;
	}

	public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
		this.vehicleIdentificationNumber = vehicleIdentificationNumber;
	}

	public String getVehicleManufacturer() {
		return this.vehicleManufacturer;
	}

	public void setVehicleManufacturer(String vehicleManufacturer) {
		this.vehicleManufacturer = vehicleManufacturer;
	}

	public String getVehicleModel() {
		return this.vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public byte[] getVehiclePicture() {
		return this.vehiclePicture;
	}

	public void setVehiclePicture(byte[] vehiclePicture) {
		this.vehiclePicture = vehiclePicture;
	}

	public String getVehicleType() {
		return this.vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getYearOfMake() {
		return this.yearOfMake;
	}

	public void setYearOfMake(Integer yearOfMake) {
		this.yearOfMake = yearOfMake;
	}

	public FuelInfo getFuelInfo() {
		return this.fuelInfo;
	}

	public void setFuelInfo(FuelInfo fuelInfo) {
		this.fuelInfo = fuelInfo;
	}

	public MobileTerminal getMobileTerminal() {
		return this.mobileTerminal;
	}

	public void setMobileTerminal(MobileTerminal mobileTerminal) {
		this.mobileTerminal = mobileTerminal;
	}

	public OtherVehicleInfo getOtherVehicleInfo() {
		return this.otherVehicleInfo;
	}

	public void setOtherVehicleInfo(OtherVehicleInfo otherVehicleInfo) {
		this.otherVehicleInfo = otherVehicleInfo;
	}

	public VehicleCost getVehicleCost() {
		return this.vehicleCost;
	}

	public void setVehicleCost(VehicleCost vehicleCost) {
		this.vehicleCost = vehicleCost;
	}

	public VehicleEquipment getVehicleEquipment() {
		return this.vehicleEquipment;
	}

	public void setVehicleEquipment(VehicleEquipment vehicleEquipment) {
		this.vehicleEquipment = vehicleEquipment;
	}

	public VehicleLoadspace getVehicleLoadspace() {
		return this.vehicleLoadspace;
	}

	public void setVehicleLoadspace(VehicleLoadspace vehicleLoadspace) {
		this.vehicleLoadspace = vehicleLoadspace;
	}
}