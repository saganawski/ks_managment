package com.ks.management.office;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ks.management.location.Location;


@Entity
@Table(name="office")
public class Office {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;

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
	
	public Office() {}

	public Office(String name, Location location, Integer updatedBy, Integer createdBy) {
		this.name = name;
		this.location = location;
		this.updatedBy = updatedBy;
		this.createdBy = createdBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

