package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the driver_emergency database table.
 * 
 */

@Entity
@Table(name="driver_emergency")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class DriverEmergency implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="driver_emergency_id_seq",
                sequenceName="driver_emergency_id_seq",
                allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
             generator="driver_emergency_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;



	@Column(name="contact_name")
	private String contactName;

	@Column(name="contact_phone_number")
	private String contactPhoneNumber;

	@Version
	private Long version;

	//bi-directional one-to-one association to Driver
	@OneToOne(mappedBy="driverEmergency")
	private Driver driver;

	public DriverEmergency() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhoneNumber() {
		return this.contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Driver getDriver() {
		return this.driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getContactNameProperty() {
		if(this.contactName==null) this.contactName=new String();
		StringProperty s=new SimpleStringProperty(this.contactName);
		return s;
	}

	public void setContactNameProperty(StringProperty contactNameProperty) {
		this.contactName = contactNameProperty.get();
	}

	public StringProperty getContactPhoneNumberProperty() {
		if(this.contactPhoneNumber==null) this.contactPhoneNumber=new String();
		StringProperty s=new SimpleStringProperty(this.contactPhoneNumber);
		return s;
	}

	public void setContactPhoneNumberProperty(StringProperty contactPhoneNumberProperty) {
		this.contactPhoneNumber = contactPhoneNumberProperty.get();
	}

}