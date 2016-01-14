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
 * The persistent class for the driver_group database table.
 * 
 */
@Entity
@Table(name="driver_group")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
@NamedQuery(name="DriverGroup.findAll", query="SELECT d FROM DriverGroup d")
public class DriverGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="driver_group_id_seq",
                   sequenceName="driver_group_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="driver_group_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	private String description;

	private String name;
	
	private Long version;
	
	@Column(name = "contact_person")
	private String contactPerson;

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEMail() {
		return contactEMail;
	}

	public void setContactEMail(String contactEMail) {
		this.contactEMail = contactEMail;
	}

	@Column(name = "contact_phone")
	private String contactPhone;

	@Column(name = "contact_e_mail")
	private String contactEMail;

	public DriverGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}
	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getContactPersonProperty() {
		if(this.contactPerson==null) this.contactPerson=new String();
		StringProperty s=new SimpleStringProperty(this.contactPerson);
		return s;
	}

	public StringProperty getContactPhoneProperty() {
		StringProperty s=new SimpleStringProperty(this.contactPhone);
		return s;
	}
	
	public StringProperty getContactEMailProperty() {
		if(this.contactEMail==null) this.contactEMail=new String();
		StringProperty s=new SimpleStringProperty(this.contactEMail);
		return s;
	}
	
	public StringProperty getDescriptionProperty() {
		if(this.description==null) this.description=new String();
		StringProperty s=new SimpleStringProperty(this.description);
		return s;
	}
	
	public void setContactEMailProperty(StringProperty contactEMailProperty) {
		this.contactEMail = contactEMailProperty.get();
	}
	
	public void setContactPhoneProperty(StringProperty contactPhoneProperty) {
		this.contactPhone = contactPhoneProperty.get();
	}
	
	public void setContactPersonProperty(StringProperty contactPersonProperty) {
		this.contactPerson = contactPersonProperty.get();
	}
	
	public void setDescriptionProperty(StringProperty descriptionProperty) {
		this.description = descriptionProperty.get();
	}
	public StringProperty getNameProperty() {
		if(this.name==null) this.name=new String();
		StringProperty s=new SimpleStringProperty(this.name);
		return s;
	}

	public void setNameProperty(StringProperty nameProperty) {
		this.name = nameProperty.get();
	}
}