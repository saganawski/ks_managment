package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.EmployeeNote;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.EmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.position.Position;
import com.ks.management.position.dao.JpaPositionRepo;
import com.ks.management.recruitment.training.TrainingNote;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    @Autowired
    private JpaEmployeeNoteRepo jpaEmployeeNoteRepo;


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
//                .filter(e -> !e.getDeleted()) TODO: want all regardless of status

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
        final Integer userId = userPrincipal.getUserId();

        final Position position = positionRepo.findByCode(employeeDTO.getPosition());
        final String firstName = Optional.ofNullable(employeeDTO.getFirstName()).orElse("");
        final String lastName = Optional.ofNullable(employeeDTO.getLastName()).orElse("");
        final String aliasName = Optional.ofNullable(employeeDTO.getAlias()).orElse("");
        final String email = Optional.ofNullable(employeeDTO.getEmail()).orElse("");
        final String phoneNumber = Optional.ofNullable(employeeDTO.getPhoneNumber()).orElse("");
        final Integer id = Optional.ofNullable(employeeDTO.getId()).orElse(-1);

        final LocalDate startDate = Optional.ofNullable(employeeDTO.getStartDate()).orElse(null);
        final LocalDate endDate = Optional.ofNullable(employeeDTO.getEndDate()).orElse(null);
        final Boolean voluntary = Optional.ofNullable(employeeDTO.getVoluntary()).orElse(null);

        final Boolean activationStatus = Optional.ofNullable(employeeDTO.getIsActive()).orElse(false);

        final Employee employee = Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .alias(aliasName)
                .email(email)
                .phoneNumber(phoneNumber)
                .position(position)
                .deleted(activationStatus)
                .startDate(startDate)
                .endDate(endDate)
                .voluntary(voluntary)
                .updatedBy(userId)
                .build();

        for (Office office : offices){
            employee.addOffice(office);
        }

        if(employeeDTO.getEmployeeNotes() != null){
            final EmployeeNote note = employeeDTO.getEmployeeNotes().get(0);
            note.setCreatedBy(userId);
            note.setUpdatedBy(userId);
            note.setEmployee(employee);
            employee.addEmployeeNote(note);
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

    @Override
    public void deleteNote(int noteId) {
        jpaEmployeeNoteRepo.deleteById(noteId);
    }

    @Override
    public EmployeeDTO getEmployeeDTO(int employeeId) {
        final Optional<Employee> employee = repo.findById(employeeId);
        if(!employee.isPresent()){
            throw new RuntimeException("Cant find training with ID: " + employeeId);
        }
        final Set<Office> officeOptions = officeRepo.findAll().stream().collect(Collectors.toSet());
        final Set<Position> positionsOptions = positionRepo.findAll().stream().collect(Collectors.toSet());

        final EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .employee(employee.get())
                .officeOptions(officeOptions)
                .positionOptions(positionsOptions)
                .build();

        return employeeDTO;
    }

}
