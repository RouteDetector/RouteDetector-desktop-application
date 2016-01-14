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
 * The persistent class for the mobile_terminal database table.
 * 
 */
@Entity
@Table(name="mobile_terminal")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class MobileTerminal implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="mobile_terminal_id_seq",
                   sequenceName="mobile_terminal_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="mobile_terminal_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	private String description;

	@Column(name="mobile_terminal_id")
	private String mobileTerminalId;

	private String password;

	private String username;
	@Version
	private Long version;

	//bi-directional one-to-one association to Vehicle
	@OneToOne(mappedBy="mobileTerminal")
	private Vehicle vehicle;

	public MobileTerminal() {
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

	public String getMobileTerminalId() {
		return this.mobileTerminalId;
	}

	public void setMobileTerminalId(String mobileTerminalId) {
		this.mobileTerminalId = mobileTerminalId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getDescriptionProperty() {
		if(this.description==null) this.description=new String();
		StringProperty s=new SimpleStringProperty(this.description);
		return s;
	}

	public void setDescriptionProperty(StringProperty descriptionProperty) {
		this.description = descriptionProperty.get();
	}

	public StringProperty getMobileTerminalIdProperty() {
		if(this.mobileTerminalId==null) this.mobileTerminalId=new String();
		StringProperty s=new SimpleStringProperty(this.mobileTerminalId);
		return s;
	}

	public void setMobileTerminalIdProperty(StringProperty mobileTerminalIdProperty) {
		
		this.mobileTerminalId = mobileTerminalIdProperty.get();
	}

	public StringProperty getPasswordProperty() {
		if(this.password==null) this.password=new String();
		StringProperty s=new SimpleStringProperty(this.password);
		return s;
	}

	public void setPasswordProperty(StringProperty passwordProperty) {
		this.password = passwordProperty.get();
	}

	public StringProperty getUsernameProperty() {
		if(this.username==null) this.username=new String();
		StringProperty s=new SimpleStringProperty(this.username);
		return s;
	}

	public void setUsernameProperty(StringProperty usernameProperty) {
		this.username = usernameProperty.get();
	}

}