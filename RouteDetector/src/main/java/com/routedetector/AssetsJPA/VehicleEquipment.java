package com.routedetector.AssetsJPA;

import java.io.Serializable;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the vehicle_equipment database table.
 * 
 */
@Entity
@Table(name="vehicle_equipment")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class VehicleEquipment implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="vehicle_equipment_id_seq",
                   sequenceName="vehicle_equipment_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="vehicle_equipment_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	@Column(name="meet_rail")
	private Boolean meetRail;

	@Column(name="other_equipment")
	private String otherEquipment;

	@Column(name="pallet_carrier")
	private Boolean palletCarrier;

	private Boolean refrigerated;

	@Column(name="securing_straps")
	private Integer securingStraps;
	@Version
	private Long version;

	//bi-directional one-to-one association to Vehicle
	@OneToOne(mappedBy="vehicleEquipment")
	private Vehicle vehicle;

	public VehicleEquipment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getMeetRail() {
		return this.meetRail;
	}

	public void setMeetRail(Boolean meetRail) {
		this.meetRail = meetRail;
	}

	public String getOtherEquipment() {
		return this.otherEquipment;
	}

	public void setOtherEquipment(String otherEquipment) {
		this.otherEquipment = otherEquipment;
	}

	public Boolean getPalletCarrier() {
		return this.palletCarrier;
	}

	public void setPalletCarrier(Boolean palletCarrier) {
		this.palletCarrier = palletCarrier;
	}

	public Boolean getRefrigerated() {
		return this.refrigerated;
	}

	public void setRefrigerated(Boolean refrigerated) {
		this.refrigerated = refrigerated;
	}

	public Integer getSecuringStraps() {
		return this.securingStraps;
	}

	public void setSecuringStraps(Integer securingStraps) {
		this.securingStraps = securingStraps;
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

	public BooleanProperty getMeetRailProperty() {
		if(this.meetRail==null) this.meetRail=false;
		BooleanProperty s=new SimpleBooleanProperty(this.meetRail);
		return s;
	}

	public void setMeetRailProperty(BooleanProperty meetRailProperty) {
		this.meetRail = meetRailProperty.get();
	}

	public StringProperty getOtherEquipmentProperty() {
		if(this.otherEquipment==null) this.otherEquipment=new String();
		StringProperty s=new SimpleStringProperty(this.otherEquipment);
		return s;
	}

	public void setOtherEquipmentProperty(StringProperty otherEquipmentProperty) {
		this.otherEquipment = otherEquipmentProperty.get();
	}

	public BooleanProperty getPalletCarrierProperty() {
		if(this.palletCarrier==null) this.palletCarrier=false;
		BooleanProperty s=new SimpleBooleanProperty(this.palletCarrier);
		return s;
	}

	public void setPalletCarrierProperty(BooleanProperty palletCarrierProperty) {
		this.palletCarrier = palletCarrierProperty.get();
	}

	public BooleanProperty getRefrigeratedProperty() {
		if(this.refrigerated==null) this.refrigerated=false;
		BooleanProperty s=new SimpleBooleanProperty(this.refrigerated);
		return s;
	}

	public void setRefrigeratedProperty(BooleanProperty refrigeratedProperty) {
		this.refrigerated = refrigeratedProperty.get();
	}

	public IntegerProperty getSecuringStrapsProperty() {
		if(this.securingStraps==null)this.securingStraps=0;
		IntegerProperty s=new SimpleIntegerProperty(this.securingStraps);
		return s;
	}

	public void setSecuringStrapsProperty(IntegerProperty securingStrapsProperty) {
		this.securingStraps = securingStrapsProperty.get();
	}
}