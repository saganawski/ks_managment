package com.ks.management.office;

import com.ks.management.employee.Employee;
import com.ks.management.location.Location;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name= "employee_office",
			joinColumns = @JoinColumn(name="office_id"),
			inverseJoinColumns = @JoinColumn(name="employee_id")
	)
	private List<Employee> Employees = new ArrayList<>();
	
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

