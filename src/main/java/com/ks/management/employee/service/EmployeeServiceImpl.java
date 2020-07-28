package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.position.Position;
import com.ks.management.position.dao.JpaPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

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
    public Employee createEmployee(NewEmployeeDTO newEmployeeDTO) {
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
        final Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .alias(aliasName)
                .email(email)
                .phoneNumber(phoneNumber)
                .position(position)
//                TODO get active userId
                .updatedBy(-1)
                .createdBy(-1)
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }
        return repo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repo.findAll();
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
    public Employee updateEmployee(EditEmployeeDTO employeeDTO) {
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

        final Employee employee = Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .alias(aliasName)
                .email(email)
                .phoneNumber(phoneNumber)
                .position(position)
//                TODO get active userId
                .updatedBy(-1)
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }
        
        return repo.save(employee);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        repo.deleteById(employeeId);
    }

    @Override
    public Employee createNewEmployee(Employee employee) {
        final List<Office> offices = new ArrayList<>();

        employee.getOffices().stream()
                .map(Office::getId)
                .filter(officeId -> officeRepo.findById(officeId).isPresent())
                .map(id -> officeRepo.findById(id).get())
                .forEach(offices::add);

        employee.removeALlOffices();
        offices.forEach(employee::addOffice);
        //TODO: set by auth id
        employee.setUpdatedBy(-1);
        employee.setCreatedBy(-1);

        return repo.save(employee);
    }

}
