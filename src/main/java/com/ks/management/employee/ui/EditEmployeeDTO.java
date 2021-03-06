package com.ks.management.employee.ui;

import com.ks.management.employee.EmployeeNote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditEmployeeDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String alias;
    private String email;
    private String phoneNumber;
    private String position;
    private Boolean deleted;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean voluntary;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private List<Integer> officeSelection = new ArrayList<>();
    private List<EmployeeNote> employeeNotes = new ArrayList<>();
    private Boolean isActive;

    public EditEmployeeDTO() {
    }

    public EditEmployeeDTO(Integer id, String firstName, String lastName, String alias, String email, String phoneNumber, String position, List<Integer> officeSelection) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.officeSelection = officeSelection;
    }

    public EditEmployeeDTO(Integer id, String firstName, String lastName, String alias, String email, String phoneNumber, String position, Boolean deleted, List<Integer> officeSelection) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.deleted = deleted;
        this.officeSelection = officeSelection;
    }

    public EditEmployeeDTO(Integer id, String firstName, String lastName, String alias, String email, String phoneNumber, String position, Boolean deleted, LocalDate startDate, LocalDate endDate, Boolean voluntary,  Integer updatedBy, Date updatedDate, Integer createdBy, Date createdDate, List<Integer> officeSelection, List<EmployeeNote> employeeNotes, Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.deleted = deleted;
        this.startDate = startDate;
        this.endDate = endDate;
        this.voluntary =voluntary;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.officeSelection = officeSelection;
        this.employeeNotes = employeeNotes;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Integer> getOfficeSelection() {
        return officeSelection;
    }

    public void setOfficeSelection(List<Integer> officeSelection) {
        this.officeSelection = officeSelection;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getVoluntary() {
        return voluntary;
    }

    public void setVoluntary(Boolean voluntary) {
        this.voluntary = voluntary;
    }

    public List<EmployeeNote> getEmployeeNotes() {
        return employeeNotes;
    }

    public void setEmployeeNotes(List<EmployeeNote> employeeNotes) {
        this.employeeNotes = employeeNotes;
    }

    public  Boolean getIsActive() {return isActive;}

    public void setIsActive(Boolean isActive) {this.isActive = isActive;}
}
