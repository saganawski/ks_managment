package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.position.Position;
import com.ks.management.position.dao.JpaPositionRepo;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private JpaEmployeeRepo repo;

    @Autowired
    private JpaOfficeRepo officeRepo;

    @Autowired
    private JpaPositionRepo positionRepo;

    @Override
    @NotNull
    public Employee createEmployee(NewEmployeeDTO newEmployeeDTO, UserPrincipal userPrincipal) {
        final Set<Office> offices = new HashSet<>();
        for (Integer officeId: newEmployeeDTO.getOfficeSelections()){
            final Office office = officeRepo.findById(officeId).orElse(null);
            if(office != null){
                offices.add(office);
            }
        }

        final String firstName = Optional.ofNullable(newEmployeeDTO.getFirstName()).orElse("");
        final String lastName = Optional.ofNullable(newEmployeeDTO.getLastName()).orElse("");
        final String aliasName = Optional.ofNullable(newEmployeeDTO.getAlias()).orElse("");
        final String email = Optional.ofNullable(newEmployeeDTO.getEmail()).orElse("");
        final String phoneNumber = Optional.ofNullable(newEmployeeDTO.getPhoneNumber()).orElse("");
        final Position position = Optional.ofNullable(newEmployeeDTO.getPosition()).orElse(null);
        final Integer userId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        final Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .alias(aliasName)
                .email(email)
                .phoneNumber(phoneNumber)
                .position(position)
                .deleted(false)
                .updatedBy(userId)
                .createdBy(userId)
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }
        return repo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repo.findAll().stream()
                .filter(e -> !e.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployee(Integer employeeId) {
        try {
            return repo.findById(employeeId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee updateEmployee(EditEmployeeDTO employeeDTO, UserPrincipal userPrincipal) {
        final Set<Office> offices = new HashSet<>();
        for (Integer officeId: employeeDTO.getOfficeSelection()){
            final Office office = officeRepo.findById(officeId).orElse(null);
            if(office != null){
                offices.add(office);
            }
        }

        final Position position = positionRepo.findByCode(employeeDTO.getPosition());
        final String firstName = Optional.ofNullable(employeeDTO.getFirstName()).orElse("");
        final String lastName = Optional.ofNullable(employeeDTO.getLastName()).orElse("");
        final String aliasName = Optional.ofNullable(employeeDTO.getAlias()).orElse("");
        final String email = Optional.ofNullable(employeeDTO.getEmail()).orElse("");
        final String phoneNumber = Optional.ofNullable(employeeDTO.getPhoneNumber()).orElse("");
        final Integer id = Optional.ofNullable(employeeDTO.getId()).orElse(-1);
        final Integer updatedById = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        final Employee employee = Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .alias(aliasName)
                .email(email)
                .phoneNumber(phoneNumber)
                .position(position)
                .deleted(false)
                .updatedBy(updatedById)
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }
        
        return repo.save(employee);
    }

    @Override
    public void deleteEmployee(Integer employeeId, UserPrincipal userPrincipal) {
        final Employee employee = repo.getOne(employeeId);

        if(employee == null){
            throw new RuntimeException("Unable to find employee with id: " + employeeId);
        }

        final Integer userId = userPrincipal.getUserId();
        employee.setUpdatedBy(userId);
        employee.setDeleted(true);
        repo.save(employee);
    }

    @Override
    public Employee createNewEmployee(Employee employee, UserPrincipal userPrincipal) {
        //this intended to auto create employees from training-details.js
        if(employee.getPosition() == null){
            final Position position = positionRepo.findByCode("CANVASSER");
            employee.setPosition(position);
        }
        final List<Office> offices = new ArrayList<>();

        employee.getOffices().stream()
                .map(Office::getId)
                .filter(officeId -> officeRepo.findById(officeId).isPresent())
                .map(id -> officeRepo.findById(id).get())
                .forEach(offices::add);

        employee.removeALlOffices();
        offices.forEach(employee::addOffice);
        final Integer userId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        employee.setUpdatedBy(userId);
        employee.setCreatedBy(userId);
        employee.setDeleted(false);

        return repo.save(employee);
    }

    @Override
    public Boolean checkIfEmployeeExists(String lastName, String email) {
        final Employee employee = repo.findByLastNameAndEmail(lastName,email);
        Boolean exists = false;
        if(employee != null){
            exists = true;
        }
        return exists;
    }

    @Override
    public Set<Employee> getAllEmployeesNonCanvassers() {
        return repo.findAllNonCanvassers();
    }

}
