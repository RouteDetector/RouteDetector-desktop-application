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


/**
 * The persistent class for the driver_license database table.
 * 
 */
@Entity
@Table(name="driver_license")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class DriverLicense implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="driver_license_id_seq",
             sequenceName="driver_license_id_seq",
             allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
          generator="driver_license_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	private String classes;

	@Temporal(TemporalType.DATE)
	@Column(name="expiry_date")
	private Date expiryDate;

	private String number;

	@Version
	private Long version;

	//bi-directional one-to-one association to Driver
	@OneToOne(mappedBy="driverLicense")
	private Driver driver;

	public DriverLicense() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClasses() {
		return this.classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public StringProperty getClassesProperty() {
		if(this.classes==null) this.classes=new String();
		StringProperty s=new SimpleStringProperty(this.classes);		
		return s;
	}

	public void setClassesProperty(StringProperty classesProperty) {
		this.classes = classesProperty.get();
	}

	public ObjectProperty<LocalDate> getExpiryDateProperty() {
		if(this.expiryDate==null)expiryDate=new Date();
		ObjectProperty<LocalDate> date=new SimpleObjectProperty<LocalDate>();
		date.set(this.expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return date;
	}

	public void setExpiryDateProperty(ObjectProperty<LocalDate> expiryDateProperty) {
		this.expiryDate = Date.from(expiryDateProperty.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public StringProperty getNumberProperty() {
		if(this.number==null) this.number=new String();
		StringProperty s=new SimpleStringProperty(this.number);
		return s;
	}

	public void setNumberProperty(StringProperty numberProperty) {
		this.number = numberProperty.get();
	}

}