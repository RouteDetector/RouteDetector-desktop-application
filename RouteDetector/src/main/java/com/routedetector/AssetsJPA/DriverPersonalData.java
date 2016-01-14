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
 * The persistent class for the driver_personal_data database table.
 * 
 */
@Entity
@Table(name="driver_personal_data")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class DriverPersonalData implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="driver_personal_data_id_seq",
          sequenceName="driver_personal_data_id_seq",
          allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
       generator="driver_personal_data_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;


	private String address;

	@Temporal(TemporalType.DATE)
	@Column(name="birth_date")
	private Date birthDate;

	private String city;

	private String country;

	@Column(name="full_name")
	private String fullName;

	@Column(name="id_number")
	private String idNumber;

	@Column(name="marital_status")
	private String maritalStatus;

	@Column(name="post_code")
	private String postCode;

	@Version
	private Long version;

	//bi-directional one-to-one association to Driver
	@OneToOne(mappedBy="driverPersonalData")
	private Driver driver;

	public DriverPersonalData() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getMaritalStatus() {
		return this.maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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

	public StringProperty getAddressProperty() {
		if(this.address==null) this.address=new String();
		StringProperty s=new SimpleStringProperty(this.address);
		return s;
	}

	public void setAddressProperty(StringProperty addressProperty) {
		this.address = addressProperty.get();
	}

	public ObjectProperty<LocalDate> getBirthDateProperty() {
		if(this.birthDate==null) birthDate=new Date();
		ObjectProperty<LocalDate> date=new SimpleObjectProperty<LocalDate>();
		date.set(this.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return date;
	}

	public void setBirthDateProperty(ObjectProperty<LocalDate> birthDateProperty) {
		this.birthDate = Date.from(birthDateProperty.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public StringProperty getCityProperty() {
		if(this.city==null) this.city=new String();
		StringProperty s=new SimpleStringProperty(this.city);
		return s;
	}

	public void setCityProperty(StringProperty cityProperty) {
		this.city = cityProperty.get();
	}

	public StringProperty getCountryProperty() {
		if(this.country==null) this.country=new String();
		StringProperty s=new SimpleStringProperty(this.country);
		return s;
	}

	public void setCountryProperty(StringProperty countryProperty) {
		this.country = countryProperty.get();
	}

	public StringProperty getFullNameProperty() {
		if(this.fullName==null) this.fullName=new String();
		StringProperty s=new SimpleStringProperty(this.fullName);
		return s;
	}

	public void setFullNameProperty(StringProperty fullNameProperty) {
		this.fullName = fullNameProperty.get();
	}

	public StringProperty getIdNumberProperty() {
		if(this.idNumber==null) this.idNumber=new String();
		StringProperty s=new SimpleStringProperty(this.idNumber);
		return s;
	}

	public void setIdNumberProperty(StringProperty idNumberProperty) {
		this.idNumber = idNumberProperty.get();
	}

	public StringProperty getMaritalStatusProperty() {
		if(this.maritalStatus==null) this.maritalStatus=new String();
		StringProperty s=new SimpleStringProperty(this.maritalStatus);
		return s;
	}

	public void setMaritalStatusProperty(StringProperty maritalStatusProperty) {
		this.maritalStatus = maritalStatusProperty.get();
	}

	public StringProperty getPostCodeProperty() {
		if(this.postCode==null) this.postCode=new String();
		StringProperty s=new SimpleStringProperty(this.postCode);
		return s;
	}

	public void setPostCodeProperty(StringProperty postCodeProperty) {
		this.postCode = postCodeProperty.get();
	}


}