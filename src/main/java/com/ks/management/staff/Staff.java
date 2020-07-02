package com.ks.management.staff;

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
@Table(name="staff")
public class Staff {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@Column(name="alias")
	private String alias;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="position")
	private String position;
	
	@Column(name="driver")
	private Boolean driver;
	
	@Column(name="fm")
	private Boolean fm;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="voluntary")
	private Boolean voluntary;
	
	@Column(name="referred_by")
	private String referredBy;
	
	@Column(name="training")
	private Boolean training;

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
	
	
	protected Staff() {}
	
	public Staff(String firstName, String lastName, Integer upatedBy, Integer createdBy) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.updatedBy = updatedBy;
		this.createdBy = createdBy;
		
	}

	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public Boolean getDriver() {
		return driver;
	}


	public void setDriver(Boolean driver) {
		this.driver = driver;
	}

	public Boolean getFm() {
		return fm;
	}

	public void setFm(Boolean fm) {
		this.fm = fm;
	}

	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Boolean getVoluntary() {
		return voluntary;
	}


	public void setVoluntary(Boolean voluntary) {
		this.voluntary = voluntary;
	}


	public String getReferredBy() {
		return referredBy;
	}


	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}


	public Boolean getTraining() {
		return training;
	}


	public void setTraining(Boolean training) {
		this.training = training;
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
