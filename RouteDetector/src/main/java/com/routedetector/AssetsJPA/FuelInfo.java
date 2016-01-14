package com.routedetector.AssetsJPA;

import java.io.Serializable;
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


/**
 * The persistent class for the fuel_info database table.
 * 
 */
@Entity
@Table(name="fuel_info")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
@NamedQuery(name="FuelInfo.findAll", query="SELECT f FROM FuelInfo f")
public class FuelInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="fuel_info_id_seq",
	                      sequenceName="fuel_info_id_seq",
	                      allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
	                   generator="fuel_info_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	@Column(name="consumption_l_per_hour")
	private Float consumptionLPerHour;

	@Column(name="consumption_l_per_km")
	private Float consumptionLPerKm;

	@Column(name="fuel_tank_capacity")
	private Integer fuelTankCapacity;

	@Column(name="fuel_type")
	private String fuelType;
	
	@Version
	private Long version;

	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public ObjectProperty<Float> getConsumptionLPerKmProperty() {
		if(this.consumptionLPerKm==null) this.consumptionLPerKm=0f;
		ObjectProperty<Float>  s=new SimpleObjectProperty<Float> (this.consumptionLPerKm);
		return s;
	}

	public void setConsumptionLPerKmProperty(ObjectProperty<Float> consumptionLPerKmProperty) {
		this.consumptionLPerKm = consumptionLPerKmProperty.get();
	}

	public ObjectProperty<Float> getConsumptionLPerHourProperty() {
		if(this.consumptionLPerHour==null) this.consumptionLPerHour=0f;
		ObjectProperty<Float>  s=new SimpleObjectProperty<Float> (this.consumptionLPerHour);
		return s;
	}

	public void setConsumptionLPerHourProperty(ObjectProperty<Float> consumptionLPerHourProperty) {
		this.consumptionLPerHour = consumptionLPerHourProperty.get();
	}
	
	public IntegerProperty getFuelTankCapacityProperty() {
		if(this.fuelTankCapacity==null) this.fuelTankCapacity=0;
		IntegerProperty s=new SimpleIntegerProperty(this.fuelTankCapacity);
		return s;
	}

	public void setFuelTankCapacityProperty(IntegerProperty fuelTankCapacityProperty) {
		this.fuelTankCapacity = fuelTankCapacityProperty.get();
	}

	public FuelInfo() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getConsumptionLPerHour() {
		return this.consumptionLPerHour;
	}

	public void setConsumptionLPerHour(Float consumptionLPerHour) {
		this.consumptionLPerHour = consumptionLPerHour;
	}

	public Float getConsumptionLPerKm() {
		return this.consumptionLPerKm;
	}

	public void setConsumptionLPerKm(Float consumptionLPerKm) {
		this.consumptionLPerKm = consumptionLPerKm;
	}

	public Integer getFuelTankCapacity() {
		return this.fuelTankCapacity;
	}

	public void setFuelTankCapacity(Integer fuelTankCapacity) {
		this.fuelTankCapacity = fuelTankCapacity;
	}

	public String getFuelType() {
		return this.fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public StringProperty getFuelTypeProperty() {
		if(this.fuelType==null) this.fuelType=new String();
		StringProperty s=new SimpleStringProperty(this.fuelType);
		return s;
	}

	public void setFuelTypeProperty(StringProperty fuelTypeProperty) {
		this.fuelType = fuelTypeProperty.get();
	}
	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}