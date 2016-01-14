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
 * The persistent class for the other_vehicle_info database table.
 * 
 */
@Entity
@Table(name="other_vehicle_info")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class OtherVehicleInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="other_vehicle_info_id_seq",
                   sequenceName="other_vehicle_info_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="other_vehicle_info_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;
	private String continent;

	private String country;

	@Column(name="field_1")
	private String field1;

	@Column(name="field_2")
	private String field2;

	private String ownership;
	@Version
	private Long version;

	//bi-directional one-to-one association to Vehicle
	@OneToOne(mappedBy="otherVehicleInfo")
	private Vehicle vehicle;

	public OtherVehicleInfo() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContinent() {
		return this.continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getField1() {
		return this.field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return this.field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getOwnership() {
		return this.ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
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

	public StringProperty getContinentProperty() {
		if(this.continent==null) this.continent=new String();
		StringProperty s=new SimpleStringProperty(this.continent);
		return s;
	}

	public void setContinentProperty(StringProperty continentProperty) {
		this.continent = continentProperty.get();
	}

	public StringProperty getCountryProperty() {
		if(this.country==null) this.country=new String();
		StringProperty s=new SimpleStringProperty(this.country);
		return s;
	}

	public void setCountryProperty(StringProperty countryProperty) {
		this.country = countryProperty.get();
	}

	public StringProperty getField1Property() {
		if(this.field1==null) this.field1=new String();
		StringProperty s=new SimpleStringProperty(this.field1);
		return s;
	}

	public void setField1Property(StringProperty field1Property) {
		this.field1 = field1Property.get();
	}

	public StringProperty getField2Property() {
		if(this.field2==null) this.field2=new String();
		StringProperty s=new SimpleStringProperty(this.field2);
		return s;
	}

	public void setField2Property(StringProperty field2Property) {
		this.field2 = field2Property.get();
	}

	public StringProperty getOwnershipProperty() {
		if(this.ownership==null) this.ownership=new String();
		StringProperty s=new SimpleStringProperty(this.ownership);
		return s;
	}

	public void setOwnershipProperty(StringProperty ownershipProperty) {
		this.ownership = ownershipProperty.get();
	}
}