package com.routedetector.JPA;

import java.io.Serializable;
import javax.persistence.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The persistent class for the gprs_devices database table.
 * 
 */
@Entity
@Table(name="gprs_devices")
@NamedQueries(value = { 
		@NamedQuery(name="findGprsDevices",  query="SELECT g FROM GprsDevice g"),
		@NamedQuery(name="findPublicOrGprsDevicesByEMail",
				query="SELECT g " +
				"FROM GprsDevice g " +
				"WHERE g.companyEmail = :email OR g.isPublic=true"),
		@NamedQuery(name="findGprsDeviceByEMail",
				query="SELECT g " +
				"FROM GprsDevice g " +
				"WHERE g.companyEmail = :email "),
		@NamedQuery(name="findGprsDeviceByImei",
				query="SELECT g " +
				"FROM GprsDevice g " +
				"WHERE g.imei = :imei ")})

public class GprsDevice implements Serializable {
	private static final long serialVersionUID = 1L;

   @Id
   @SequenceGenerator(name="gprs_devices_id_seq",
                      sequenceName="gprs_devices_id_seq",
                      allocationSize=1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE,
                   generator="gprs_devices_id_seq")
   @Column(name = "id", updatable=false)	
   private Integer id;

	@Column(name="company_email")
	private String companyEmail;

	private String imei;

	@Column(name="is_public")
	private Boolean isPublic;

	private Boolean isconnected;

	public GprsDevice() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Boolean getIsconnected() {
		return this.isconnected;
	}

	public void setIsconnected(Boolean isconnected) {
		this.isconnected = isconnected;
	}

	public BooleanProperty getIsconnectedProperty() {
		if(this.isconnected==null) this.isconnected=false;
		BooleanProperty s=new SimpleBooleanProperty(this.isconnected);
		return s;
	}
	public void setIsconnectedProperty(BooleanProperty isconnectedProperty) {
		this.isconnected = isconnectedProperty.get();
	}
	public BooleanProperty getIsPublicProperty() {
		if(this.isPublic==null) this.isPublic=false;
		BooleanProperty s=new SimpleBooleanProperty(this.isPublic);
		return s;
	}
	public void setIsPublicProperty(BooleanProperty isPublicProperty) {
		this.isPublic = isPublicProperty.get();
	}
	public StringProperty getImeiProperty() {
		if(this.imei==null) this.imei=new String();
		StringProperty s=new SimpleStringProperty(this.imei);
		return s;
	}

	public void setImeiProperty(StringProperty imeiProperty) {
		this.imei = imeiProperty.get();
	}

}