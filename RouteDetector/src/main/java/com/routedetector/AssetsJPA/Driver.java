package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The persistent class for the driver database table.
 * 
 */
@Entity
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
@NamedQuery(name="findAllDrivers", query="SELECT d FROM Driver d")
public class Driver implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="driver_id_seq",
                sequenceName="driver_id_seq",
                allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
             generator="driver_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	@Column(name="e_mail")
	private String eMail;
	
	private byte[] image;

	@Column(name="mobile_phone")
	private String mobilePhone;

	private String name;
	
	@Column(name="tachograph_driver_card")
	private String tachographDriverCard;
	
	@Version
	private Long version;

	//bi-directional one-to-one association to DriverEmergency
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="driver_emergency_id")
	private DriverEmergency driverEmergency;

	//bi-directional one-to-one association to DriverLicense
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="driver_license_id")
	private DriverLicense driverLicense;

	//bi-directional one-to-one association to DriverMobileTerminal
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="mobile_terminal_id")
	private DriverMobileTerminal driverMobileTerminal;

	//bi-directional one-to-one association to DriverPersonalData
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="personal_data_id")
	private DriverPersonalData driverPersonalData;

	//bi-directional one-to-one association to DriverProfessionalData
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="driver_professional_data_id")
	private DriverProfessionalData driverProfessionalData;

	public Driver() {
	}

	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTachographDriverCard() {
		return this.tachographDriverCard;
	}

	public void setTachographDriverCard(String tachographDriverCard) {
		this.tachographDriverCard = tachographDriverCard;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public DriverEmergency getDriverEmergency() {
		return this.driverEmergency;
	}

	public void setDriverEmergency(DriverEmergency driverEmergency) {
		this.driverEmergency = driverEmergency;
	}

	public DriverLicense getDriverLicense() {
		return this.driverLicense;
	}

	public void setDriverLicense(DriverLicense driverLicense) {
		this.driverLicense = driverLicense;
	}

	public DriverMobileTerminal getDriverMobileTerminal() {
		return this.driverMobileTerminal;
	}

	public void setDriverMobileTerminal(DriverMobileTerminal driverMobileTerminal) {
		this.driverMobileTerminal = driverMobileTerminal;
	}

	public DriverPersonalData getDriverPersonalData() {
		return this.driverPersonalData;
	}

	public void setDriverPersonalData(DriverPersonalData driverPersonalData) {
		this.driverPersonalData = driverPersonalData;
	}

	public DriverProfessionalData getDriverProfessionalData() {
		return this.driverProfessionalData;
	}

	public void setDriverProfessionalData(DriverProfessionalData driverProfessionalData) {
		this.driverProfessionalData = driverProfessionalData;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}
	
	public StringProperty getEMailProperty() {
		if(this.eMail==null) this.eMail=new String();
		StringProperty s=new SimpleStringProperty(this.eMail);
		return s;
	}

	public void setEMailProperty(StringProperty eMailProperty) {
		this.eMail = eMailProperty.get();
	}

	public StringProperty getMobilePhoneProperty() {
		if(this.mobilePhone==null) this.mobilePhone=new String();
		StringProperty s=new SimpleStringProperty(this.mobilePhone);
		return s;
	}

	public void setMobilePhoneProperty(StringProperty mobilePhoneProperty) {
		this.mobilePhone = mobilePhoneProperty.get();
	}

	public StringProperty getNameProperty() {
		if(this.name==null) this.name=new String();
		StringProperty s=new SimpleStringProperty(this.name);
		return s;
	}

	public void setNameProperty(StringProperty nameProperty) {
		this.name = nameProperty.get();
	}

	public StringProperty getTachographDriverCardProperty() {
		if(this.tachographDriverCard==null) this.tachographDriverCard=new String();
		StringProperty s=new SimpleStringProperty(this.tachographDriverCard);
		return s;
	}

	public void setTachographDriverCardProperty(StringProperty tachographDriverCardProperty) {
		this.tachographDriverCard = tachographDriverCardProperty.get();
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}