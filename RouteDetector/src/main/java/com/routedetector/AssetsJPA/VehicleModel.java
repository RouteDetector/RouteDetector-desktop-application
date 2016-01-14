package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the vehicle_model database table.
 * 
 */
@Entity
@Table(name="vehicle_model")
@NamedQueries(value = { 
		@NamedQuery(name="VehicleModel.findAllModels", query="SELECT DISTINCT v.model FROM VehicleModel v  ORDER BY v.model"),
		@NamedQuery(name="VehicleModel.findAllManufacturers", query="SELECT DISTINCT v.manufacturer FROM VehicleModel v ORDER BY v.manufacturer")})
public class VehicleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="vehicle_model_id_seq",
	                      sequenceName="vehicle_model_id_seq",
	                      allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
	                   generator="vehicle_model_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	private String manufacturer;

	private String model;

	private Integer year;

	public VehicleModel() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getManufacturerProperty() {
		if(this.manufacturer==null) this.manufacturer=new String();
		StringProperty s=new SimpleStringProperty(this.manufacturer);
		return s;
	}

	public void setManufacturerProperty(StringProperty manufacturerProperty) {
		this.manufacturer = manufacturerProperty.get();
	}

	public StringProperty getModelProperty() {
		if(this.model==null) this.model=new String();
		StringProperty s=new SimpleStringProperty(this.model);
		return s;
	}

	public void setModelProperty(StringProperty modelProperty) {
		this.model = modelProperty.get();
	}

	public IntegerProperty getYearProperty() {
		if(this.year==null)this.year=0;
		IntegerProperty s=new SimpleIntegerProperty(this.year);
		return s;
	}

	public void setYearProperty(IntegerProperty yearProperty) {
		this.year = yearProperty.get();
	}
}