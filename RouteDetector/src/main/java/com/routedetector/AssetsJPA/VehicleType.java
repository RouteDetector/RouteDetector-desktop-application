package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the vehicle_type database table.
 * 
 */
@Entity
@Table(name="vehicle_type")
@NamedQuery(name="VehicleType.findAllTypes", query="SELECT v.type FROM VehicleType v")
public class VehicleType implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="vehicle_type_id_seq",
	                      sequenceName="vehicle_type_id_seq",
	                      allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
	                   generator="vehicle_type_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;
	private String type;

	public VehicleType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}

	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getTypeProperty() {
		if(this.type==null) this.type=new String();
		StringProperty s=new SimpleStringProperty(this.type);
		return s;
	}

	public void setTypeProperty(StringProperty typeProperty) {
		this.type = typeProperty.get();
	}
}