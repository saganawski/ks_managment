package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.position.Position;
import org.springframework.beans.factory.annotation.Autowired;
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
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }
        return repo.save(employee);
    }
}
