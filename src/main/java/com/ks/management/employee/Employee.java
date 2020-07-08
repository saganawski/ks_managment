package com.ks.management.employee;

import com.ks.management.office.Office;
import com.ks.management.position.Position;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="employee")
@Data
@Builder
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
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
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
	
	@ManyToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
	@JoinTable(
			name= "employee_office",
			joinColumns = @JoinColumn(name="employee_id"),
			inverseJoinColumns = @JoinColumn(name="office_id")
	)
	private List<Office> offices = new ArrayList<>();

	public void addOffice(Office office){
		if(offices == null){
			offices = new ArrayList<>();
		}
		offices.add(office);
	}
}
