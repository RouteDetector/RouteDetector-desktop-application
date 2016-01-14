package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * The persistent class for the vehicle_loadspace database table.
 * 
 */
@Entity
@Table(name="vehicle_loadspace")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class VehicleLoadspace implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="vehicle_loadspace_id_seq",
                   sequenceName="vehicle_loadspace_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="vehicle_loadspace_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	@Column(name="curb_weight_kg")
	private Float curbWeightKg;

	@Column(name="double_deck")
	private Boolean doubleDeck;

	@Column(name="gross_weight_kg")
	private Float grossWeightKg;

	@Column(name="height_cm")
	private Integer heightCm;

	@Column(name="lenght_cm")
	private Integer lenghtCm;

	@Column(name="pallet_number_capacity")
	private Integer palletNumberCapacity;

	@Column(name="payload_weight_kg")
	private Float payloadWeightKg;
	@Version
	private Long version;

	@Column(name="width_cm")
	private Integer widthCm;

	//bi-directional one-to-one association to Vehicle
	@OneToOne(mappedBy="vehicleLoadspace")
	private Vehicle vehicle;

	public VehicleLoadspace() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getCurbWeightKg() {
		return this.curbWeightKg;
	}

	public void setCurbWeightKg(Float curbWeightKg) {
		this.curbWeightKg = curbWeightKg;
	}

	public Boolean getDoubleDeck() {
		return this.doubleDeck;
	}

	public void setDoubleDeck(Boolean doubleDeck) {
		this.doubleDeck = doubleDeck;
	}

	public Float getGrossWeightKg() {
		return this.grossWeightKg;
	}

	public void setGrossWeightKg(Float grossWeightKg) {
		this.grossWeightKg = grossWeightKg;
	}

	public Integer getHeightCm() {
		return this.heightCm;
	}

	public void setHeightCm(Integer heightCm) {
		this.heightCm = heightCm;
	}

	public Integer getLenghtCm() {
		return this.lenghtCm;
	}

	public void setLenghtCm(Integer lenghtCm) {
		this.lenghtCm = lenghtCm;
	}

	public Integer getPalletNumberCapacity() {
		return this.palletNumberCapacity;
	}

	public void setPalletNumberCapacity(Integer palletNumberCapacity) {
		this.palletNumberCapacity = palletNumberCapacity;
	}

	public Float getPayloadWeightKg() {
		return this.payloadWeightKg;
	}

	public void setPayloadWeightKg(Float payloadWeightKg) {
		this.payloadWeightKg = payloadWeightKg;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getWidthCm() {
		return this.widthCm;
	}

	public void setWidthCm(Integer widthCm) {
		this.widthCm = widthCm;
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

	public FloatProperty getCurbWeightKgProperty() {
		if(this.curbWeightKg==null)this.curbWeightKg=0f;
		FloatProperty s=new SimpleFloatProperty(this.curbWeightKg);
		return s;
	}

	public void setCurbWeightKgProperty(FloatProperty curbWeightKgProperty) {
		this.curbWeightKg = curbWeightKgProperty.get();
	}

	public BooleanProperty getDoubleDeckProperty() {
		if(this.doubleDeck==null)this.doubleDeck=false;
		BooleanProperty s=new SimpleBooleanProperty(this.doubleDeck);
		return s;
	}

	public void setDoubleDeckProperty(BooleanProperty doubleDeckProperty) {
		this.doubleDeck = doubleDeckProperty.get();
	}

	public FloatProperty getGrossWeightKgProperty() {
		if(this.grossWeightKg==null)this.grossWeightKg=0f;
		FloatProperty s=new SimpleFloatProperty(this.grossWeightKg);
		return s;
	}

	public void setGrossWeightKgProperty(FloatProperty grossWeightKgProperty) {
		this.grossWeightKg = grossWeightKgProperty.get();
	}

	public IntegerProperty getHeightCmProperty() {
		if(this.heightCm==null)this.heightCm=0;
		IntegerProperty s=new SimpleIntegerProperty(this.heightCm);
		return s;
	}

	public void setHeightCmProperty(IntegerProperty heightCmProperty) {
		this.heightCm = heightCmProperty.get();
	}

	public IntegerProperty getLenghtCmProperty() {
		if(this.lenghtCm==null) this.lenghtCm=0;
		IntegerProperty s=new SimpleIntegerProperty(this.lenghtCm);
		return s;
	}

	public void setLenghtCmProperty(IntegerProperty lenghtCmProperty) {
		this.lenghtCm = lenghtCmProperty.get();
	}

	public IntegerProperty getPalletNumberCapacityProperty() {
		if(this.palletNumberCapacity==null) this.palletNumberCapacity=0;
		IntegerProperty s=new SimpleIntegerProperty(this.palletNumberCapacity);
		return s;
	}

	public void setPalletNumberCapacityProperty(IntegerProperty palletNumberCapacityProperty) {
		this.palletNumberCapacity = palletNumberCapacityProperty.get();
	}

	public FloatProperty getPayloadWeightKgProperty() {
		if(this.payloadWeightKg==null)this.payloadWeightKg=0f;
		FloatProperty s=new SimpleFloatProperty(this.payloadWeightKg);
		return s;
	}

	public void setPayloadWeightKgProperty(FloatProperty payloadWeightKgProperty) {
		this.payloadWeightKg = payloadWeightKgProperty.get();
	}

	public IntegerProperty getWidthCmProperty() {
		if(this.widthCm==null)this.widthCm=0;
		IntegerProperty s=new SimpleIntegerProperty(this.widthCm);
		return s;
	}

	public void setWidthCmProperty(IntegerProperty widthCmProperty) {
		this.widthCm = widthCmProperty.get();
	}
}