package com.ks.management.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.office.Office;
import com.ks.management.position.Position;
import com.ks.management.recruitment.application.ApplicationNote;
import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.training.TrainingNote;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "position_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Position position;

	@Column(name="deleted")
	private Boolean deleted;

	@Column(name="start_date")
	private LocalDate startDate;

	@Column(name="end_date")
	private LocalDate endDate;

	@Column(name="voluntary")
	private Boolean voluntary;

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
			joinColumns = @JoinColumn(name="employee_id"),
			inverseJoinColumns = @JoinColumn(name="office_id")
	)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Office> offices = new ArrayList<>();

	public void addOffice(Office office){
		if(offices == null){
			offices = new ArrayList<>();
		}
		offices.add(office);
	}

	public void removeALlOffices(){
		this.offices = new ArrayList<>();
	}

	@JsonIgnore
	@OneToMany(mappedBy = "employee",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<EmployeeSchedule> schedules = new ArrayList<>();


	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToMany(mappedBy = "employee",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<EmployeeNote> employeeNotes = new ArrayList<>();

	public void addEmployeeNote(EmployeeNote note){
		if(employeeNotes == null){
			employeeNotes = new ArrayList<>();
		}
		employeeNotes.add(note);
	}
}
