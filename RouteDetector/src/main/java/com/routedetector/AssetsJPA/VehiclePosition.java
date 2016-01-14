package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.annotations.TenantTableDiscriminatorType;

import java.sql.Timestamp;


/**
 * The persistent class for the vehicle_positions database table.
 * 
 */
@Entity
@Table(name="vehicle_positions")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
@NamedQueries(value = { 
		@NamedQuery(name="findAllVehiclePositions", query="SELECT v FROM VehiclePosition v"),
		@NamedQuery(name="findVehiclePositions",
				query="SELECT v " +
				"FROM VehiclePosition v " +
				"WHERE v.imei = :imei ")})
public class VehiclePosition implements Serializable {
	private static final long serialVersionUID = 1L;

   @Id
   @SequenceGenerator(name="vehicle_positions_id_seq",
                      sequenceName="vehicle_positions_id_seq",
                      allocationSize=1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE,
                   generator="vehicle_positions_id_seq")
   @Column(name = "id", updatable=false)	
   private Integer id;

	@Column(name="device_time")
	private Timestamp deviceTime;

	private String imei;

	@Column(name="is_connected")
	private Boolean isConnected;

	private float lat;

	private float lon;

	private float mileage;

	private float orientation;

	private float speed;

	private Timestamp time;

	public VehiclePosition() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDeviceTime() {
		return this.deviceTime;
	}

	public void setDeviceTime(Timestamp deviceTime) {
		this.deviceTime = deviceTime;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Boolean getIsConnected() {
		return this.isConnected;
	}

	public void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	public float getLat() {
		return this.lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return this.lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public float getMileage() {
		return this.mileage;
	}

	public void setMileage(float mileage) {
		this.mileage = mileage;
	}

	public float getOrientation() {
		return this.orientation;
	}

	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}