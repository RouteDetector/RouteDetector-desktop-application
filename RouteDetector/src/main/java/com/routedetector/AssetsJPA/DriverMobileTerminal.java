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
 * The persistent class for the driver_mobile_terminal database table.
 * 
 */
@Entity
@Table(name="driver_mobile_terminal")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class DriverMobileTerminal implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="driver_mobile_terminal_id_seq",
          sequenceName="driver_mobile_terminal_id_seq",
          allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
       generator="driver_mobile_terminal_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	private String password;

	private String username;

	@Version
	private Long version;

	//bi-directional one-to-one association to Driver
	@OneToOne(mappedBy="driverMobileTerminal")
	private Driver driver;

	public DriverMobileTerminal() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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