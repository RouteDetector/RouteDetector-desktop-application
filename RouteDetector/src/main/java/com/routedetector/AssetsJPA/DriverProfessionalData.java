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
 * The persistent class for the driver_professional_data database table.
 * 
 */
@Entity
@Table(name="driver_professional_data")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class DriverProfessionalData implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="driver_professional_data_id_seq",
          sequenceName="driver_professional_data_id_seq",
          allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
       generator="driver_professional_data_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;


	@Column(name="contract_type")
	private String contractType;

	private String department;

	@Temporal(TemporalType.DATE)
	@Column(name="employment_date")
	private Date employmentDate;

	private String experience;

	@Version
	private Long version;

	//bi-directional one-to-one association to Driver
	@OneToOne(mappedBy="driverProfessionalData")
	private Driver driver;

	public DriverProfessionalData() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getEmploymentDate() {
		return this.employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getExperience() {
		return this.experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
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

	public StringProperty getContractTypeProperty() {
		if(this.contractType==null) this.contractType=new String();
		StringProperty s=new SimpleStringProperty(this.contractType);
		return s;
	}

	public void setContractTypeProperty(StringProperty contractTypeProperty) {
		this.contractType = contractTypeProperty.get();
	}

	public StringProperty getDepartmentProperty() {
		if(this.department==null) this.department=new String();
		StringProperty s=new SimpleStringProperty(this.department);
		return s;
	}

	public void setDepartmentProperty(StringProperty departmentProperty) {
		this.department = departmentProperty.get();
	}

	public ObjectProperty<LocalDate> getEmploymentDateProperty() {
		if(this.employmentDate==null)employmentDate=new Date();
		ObjectProperty<LocalDate> date=new SimpleObjectProperty<LocalDate>();
		date.set(this.employmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return date;
	}

	public void setEmploymentDateProperty(ObjectProperty<LocalDate> employmentDateProperty) {
		this.employmentDate = Date.from(employmentDateProperty.get().atStartOfDay(ZoneId.systemDefault()).toInstant());

	}

	public StringProperty getExperienceProperty() {
		if(this.experience==null) this.experience=new String();
		StringProperty s=new SimpleStringProperty(this.experience);
		return s;
	}

	public void setExperienceProperty(StringProperty experienceProperty) {
		this.experience = experienceProperty.get();
	}


}