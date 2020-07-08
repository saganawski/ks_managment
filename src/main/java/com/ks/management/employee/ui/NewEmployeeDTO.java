package com.ks.management.employee.ui;

import com.ks.management.office.Office;
import com.ks.management.position.Position;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEmployeeDTO {

    private String firstName;
    private String lastName;
    private String alias;
    private String email;
    private String phoneNumber;
    private Position position;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private List<Integer> officeSelections = new ArrayList<>();

    public NewEmployeeDTO(String firstName, String lastName, String alias, String email, String phoneNumber, Position position, Integer updatedBy, Date updatedDate, Integer createdBy, Date createdDate, List<Integer> officeSelections) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.officeSelections = officeSelections;
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

    public List<Integer> getOfficeSelections() {
        return officeSelections;
    }

    public void setOfficeSelections(List<Integer> officeSelections) {
        this.officeSelections = officeSelections;
    }
}
