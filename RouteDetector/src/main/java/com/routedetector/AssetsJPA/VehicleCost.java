package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;


/**
 * The persistent class for the vehicle_cost database table.
 * 
 */
@Entity
@Table(name="vehicle_cost")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
public class VehicleCost implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="vehicle_cost_id_seq",
                   sequenceName="vehicle_cost_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="vehicle_cost_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	@Column(name="cost_per_h")
	private BigDecimal costPerH;

	@Column(name="cost_per_km")
	private BigDecimal costPerKm;

	private BigDecimal debt;

	private Boolean paid;

	@Column(name="value_of_vehicle")
	private BigDecimal valueOfVehicle;
	@Version
	private Long version;

	//bi-directional one-to-one association to Vehicle
	@OneToOne(mappedBy="vehicleCost")
	private Vehicle vehicle;

	public VehicleCost() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCostPerH() {
		return this.costPerH;
	}

	public void setCostPerH(BigDecimal costPerH) {
		this.costPerH = costPerH;
	}

	public BigDecimal getCostPerKm() {
		return this.costPerKm;
	}

	public void setCostPerKm(BigDecimal costPerKm) {
		this.costPerKm = costPerKm;
	}

	public BigDecimal getDebt() {
		return this.debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	public Boolean getPaid() {
		return this.paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public BigDecimal getValueOfVehicle() {
		return this.valueOfVehicle;
	}

	public void setValueOfVehicle(BigDecimal valueOfVehicle) {
		this.valueOfVehicle = valueOfVehicle;
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

	public ObjectProperty<BigDecimal> getCostPerHProperty() {
		if(this.costPerH==null) this.costPerH=new BigDecimal(0);
		ObjectProperty<BigDecimal> s=new SimpleObjectProperty<BigDecimal>(this.costPerH);
		return s;
	}

	public void setCostPerHProperty(ObjectProperty<BigDecimal> costPerHProperty) {
		this.costPerH = costPerHProperty.get();
	}

	public ObjectProperty<BigDecimal> getCostPerKmProperty() {
		if(this.costPerKm==null) this.costPerKm=new BigDecimal(0);
		ObjectProperty<BigDecimal> s=new SimpleObjectProperty<BigDecimal>(this.costPerKm);
		return s;
	}

	public void setCostPerKmProperty(ObjectProperty<BigDecimal> costPerKmProperty) {
		this.costPerKm = costPerKmProperty.get();
	}

	public ObjectProperty<BigDecimal> getDebtProperty() {
		if(this.debt==null) this.debt=new BigDecimal(0);
		ObjectProperty<BigDecimal> s=new SimpleObjectProperty<BigDecimal>(this.debt);
		return s;
	}

	public void setDebtProperty(ObjectProperty<BigDecimal> debtProperty) {
		this.debt = debtProperty.get();
	}

	public BooleanProperty getPaidProperty() {
		if(this.paid==null) this.paid=false;
		BooleanProperty s=new SimpleBooleanProperty(this.paid);
		return s;
	}

	public void setPaidProperty(BooleanProperty paidProperty) {
		this.paid = paidProperty.get();
	}

	public ObjectProperty<BigDecimal> getValueOfVehicleProperty() {
		if(this.valueOfVehicle==null) this.valueOfVehicle=new BigDecimal(0);
		ObjectProperty<BigDecimal> s=new SimpleObjectProperty<BigDecimal>(this.valueOfVehicle);
		return s;
	}

	public void setValueOfVehicleProperty(ObjectProperty<BigDecimal> valueOfVehicleProperty) {
		this.valueOfVehicle = valueOfVehicleProperty.get();
	}
}