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
 * The persistent class for the vehicle_group database table.
 * 
 */
@Entity
@Table(name="vehicle_group")
@Multitenant(MultitenantType.TABLE_PER_TENANT)
@TenantTableDiscriminator(type=TenantTableDiscriminatorType.SCHEMA)
@NamedQuery(name="VehicleGroup.findAll", query="SELECT v FROM VehicleGroup v")
public class VehicleGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	   @Id
@SequenceGenerator(name="vehicle_group_id_seq",
                   sequenceName="vehicle_group_id_seq",
                   allocationSize=1)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator="vehicle_group_id_seq")
@Column(name = "id", updatable=false)	
private Integer id;

	private String description;

	private String name;
	
	private Long version;

	//bi-directional one-to-one association to Vehicle

	public VehicleGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	public IntegerProperty getIdProperty() {
		IntegerProperty s=new SimpleIntegerProperty(this.id);
		return s;
	}
	public void setIdProperty(IntegerProperty idProperty) {
		this.id = idProperty.get();
	}

	public StringProperty getDescriptionProperty() {
		if(this.description==null) this.description=new String();
		StringProperty s=new SimpleStringProperty(this.description);
		return s;
	}

	public void setDescriptionProperty(StringProperty descriptionProperty) {
		this.description = descriptionProperty.get();
	}

	public StringProperty getNameProperty() {
		if(this.name==null) this.name=new String();
		StringProperty s=new SimpleStringProperty(this.name);
		return s;
	}

	public void setNameProperty(StringProperty nameProperty) {
		this.name = nameProperty.get();
	}
}