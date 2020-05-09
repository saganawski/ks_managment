package com.ks.management.location;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="location")
public class Location {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="state_name")
	private String stateName;

	@Column(name="city_name")
	private String cityName;
	
	@Column(name="address_1")
	private String address1;
	
	@Column(name="address_2")
	private String address2;
	
	@Column(name="zip")
	private String zip;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	protected Location() {}
	
	public Location(String stateName, String cityName, String address1, String address2, String zip, Integer updatedBy,
			Integer createdBy) {
		this.stateName = stateName;
		this.cityName = cityName;
		this.address1 = address1;
		this.address2 = address2;
		this.zip = zip;
		this.updatedBy = updatedBy;
		this.createdBy = createdBy;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getId() {
		return id;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	
	
	
}
