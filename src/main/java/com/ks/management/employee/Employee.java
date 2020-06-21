package com.ks.management.employee;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ks.management.position.Position;

@Entity
@Table(name="employee")
public class Employee {

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
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "position_id")
	private Position position;
	
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
	
	
	@OneToMany(mappedBy = "employee")
	Set<EmployeeOffice> empolyeeOffices;
	
	
	protected Employee() {}


	public Employee(String firstName, String lastName, String alias, String email, String phoneNumber,
			Position position, Integer updatedBy, Integer createdBy, Set<EmployeeOffice> empolyeeOffices) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.alias = alias;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.position = position;
		this.updatedBy = updatedBy;
		this.createdBy = createdBy;
		this.empolyeeOffices = empolyeeOffices;
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


	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
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


	public Set<EmployeeOffice> getEmpolyeeOffices() {
		return empolyeeOffices;
	}


	public void setEmpolyeeOffices(Set<EmployeeOffice> empolyeeOffices) {
		this.empolyeeOffices = empolyeeOffices;
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
