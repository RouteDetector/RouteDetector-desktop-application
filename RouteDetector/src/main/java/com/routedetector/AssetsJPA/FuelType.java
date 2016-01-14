package com.routedetector.AssetsJPA;

import java.io.Serializable;
import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the fuel_type database table.
 * 
 */
@Entity
@Table(name="fuel_type")
@NamedQuery(name="FuelType.findAllTypes", query="SELECT f.type FROM FuelType f")
public class FuelType implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
	   @SequenceGenerator(name="fuel_type_id_seq",
	                      sequenceName="fuel_type_id_seq",
	                      allocationSize=1)
	   @GeneratedValue(strategy = GenerationType.SEQUENCE,
	                   generator="fuel_type_id_seq")
	   @Column(name = "id", updatable=false)	
	   private Integer id;

	private String type;

	public FuelType() {
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