package com.ks.management.project.service;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.office.Office;
import com.ks.management.project.Project;
import com.ks.management.project.ProjectWeek;
import com.ks.management.project.dao.JpaProjectRepo;
import com.ks.management.project.dao.JpaProjectWeekRepo;
import com.ks.management.project.ui.ProjectDTO;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private JpaProjectRepo jpaProjectRepo;
    @Autowired
    private JpaProjectWeekRepo jpaProjectWeekRepo;
    @Autowired
    private JpaEmployeeScheduleRepo jpaEmployeeScheduleRepo;

    @Override
    public Set<Project> getProjects() {
        return jpaProjectRepo.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Project createProject(Project project, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();
        project.setCreatedBy(userId);
        project.setUpdatedBy(userId);

        return jpaProjectRepo.save(project);
    }

    @Override
    public ProjectDTO getProjectDtoById(Integer projectId) {
        final Optional<Project> project = jpaProjectRepo.findById(projectId);
        if(!project.isPresent()){
            throw new RuntimeException("No project found with id " + projectId);
        }

        final Integer id = project.map(p -> p.getId()).orElse(-1);
        final String name = project.map(p -> p.getName()).orElse("");
        final Office office = project.map(p -> p.getOffice()).orElse(null);

        final Integer updatedBy = project.map(p -> p.getUpdatedBy()).orElse(-1);
        final Date updateDate = project.map(p -> p.getUpdatedDate()).orElse(null);
        final Integer createdBy = project.map(p -> p.getCreatedBy()).orElse(-1);
        final Date createdDate = project.map(p -> p.getCreatedDate()).orElse(null);

        final Set<ProjectWeek> weeks = project.map(p -> p.getProjectWeeks()).orElse(Collections.EMPTY_SET);


        final ProjectDTO projectDTO = ProjectDTO.builder()
                .id(id)
                .name(name)
                .shiftGoal(-1)
                .shiftsScheduled(-1)
                .shiftCompleted(-1)
                .remainingWorkingDays(-1)
                .shiftsNeeded(-1)
                .office(office)
                .updatedBy(updatedBy)
                .updatedDate(updateDate)
                .createdBy(createdBy)
                .createdDate(createdDate)
                .projectWeeks(weeks)
                .build();

        return projectDTO;
    }

    @Override
    public ProjectDTO createNewWeekForProject(Integer projectId, UserPrincipal userPrincipal) {
        final Optional<Project> project = jpaProjectRepo.findById(projectId);
        if(!project.isPresent()){
            throw new RuntimeException("No project found with id " + projectId);
        }
        final Integer userId = userPrincipal.getUserId();

        final ProjectWeek projectWeek = new ProjectWeek();
        projectWeek.setCreatedBy(userId);
        projectWeek.setUpdatedBy(userId);
        projectWeek.setProject(project.get());

        project.map(p -> p.getProjectWeeks().add(projectWeek));

        jpaProjectRepo.save(project.get());

        final Integer id = project.map(p -> p.getId()).orElse(-1);
        final String name = project.map(p -> p.getName()).orElse("");
        final Office office = project.map(p -> p.getOffice()).orElse(null);

        final Integer updatedBy = project.map(p -> p.getUpdatedBy()).orElse(-1);
        final Date updateDate = project.map(p -> p.getUpdatedDate()).orElse(null);
        final Integer createdBy = project.map(p -> p.getCreatedBy()).orElse(-1);
        final Date createdDate = project.map(p -> p.getCreatedDate()).orElse(null);

        final Set<ProjectWeek> weeks = project.map(p -> p.getProjectWeeks()).orElse(Collections.EMPTY_SET);


        final ProjectDTO projectDTO = ProjectDTO.builder()
                .id(id)
                .name(name)
                .shiftGoal(-1)
                .shiftsScheduled(-1)
                .shiftCompleted(-1)
                .remainingWorkingDays(-1)
                .shiftsNeeded(-1)
                .office(office)
                .updatedBy(updatedBy)
                .updatedDate(updateDate)
                .createdBy(createdBy)
                .createdDate(createdDate)
                .projectWeeks(weeks)
                .build();

        return projectDTO;
    }

    @Override
    public ProjectWeek updateProjectWeek(Integer projectId, UserPrincipal userPrincipal, ProjectWeek projectWeek) {
        final Optional<ProjectWeek> databaseProjectWeek = jpaProjectWeekRepo.findById(projectWeek.getId());
        if(!databaseProjectWeek.isPresent()){
            throw new RuntimeException("No project week found with id " + projectWeek.getId());
        }

        final Integer userId = userPrincipal.getUserId();

        final Integer originalShiftGoal = Optional.ofNullable(projectWeek).map(pw -> pw.getOriginalShiftGoal()).orElse(null);
        final LocalDate startDate = Optional.ofNullable(projectWeek).map(pw -> pw.getStartDate()).orElse(null);
        final LocalDate endDate = Optional.ofNullable(projectWeek).map(pw -> pw.getEndDate()).orElse(null);
        final Integer currentShiftGoal = Optional.ofNullable(projectWeek).map(pw -> pw.getCurrentShiftGoal()).orElse(null);
        final Integer remainingWorkDays = Optional.ofNullable(projectWeek).map(pw -> pw.getRemainingWorkingDays()).orElse(null);

        final ProjectWeek updatedProjectWeek = databaseProjectWeek.get();
        updatedProjectWeek.setUpdatedBy(userId);
        updatedProjectWeek.setOriginalShiftGoal(originalShiftGoal);
        updatedProjectWeek.setStartDate(startDate);
        updatedProjectWeek.setEndDate(endDate);
        updatedProjectWeek.setCurrentShiftGoal(currentShiftGoal);
        updatedProjectWeek.setRemainingWorkingDays(remainingWorkDays);

        final Integer officeId = databaseProjectWeek.map(pw -> pw.getProject()).map(p -> p.getOffice()).map(o -> o.getId()).orElse(-1);
        if(officeId != -1){
            final List<EmployeeSchedule> employeeSchedules = jpaEmployeeScheduleRepo.findAllByOfficeForTimePeriod(officeId,startDate.atStartOfDay(),endDate.atStartOfDay());

            final Integer shiftCount = employeeSchedules.size();
            final Integer doubleShiftCount = (int) employeeSchedules.stream()
                    .filter(es -> es.getEmployeeScheduleStatus() != null)
                    .filter(es -> es.getEmployeeScheduleStatus().getCode().equalsIgnoreCase("DOUBLE_SHIFT_WORKED"))
                    .count();

            updatedProjectWeek.setShiftsScheduled(shiftCount + doubleShiftCount);

            final Integer shiftsWorked = (int) employeeSchedules.stream()
                    .filter(es -> es.getEmployeeScheduleStatus() != null)
                    .filter(es -> !es.getEmployeeScheduleStatus().getCode().equalsIgnoreCase("UNEXCUSED_ABSENCE"))
                    .filter(es -> !es.getEmployeeScheduleStatus().getCode().equalsIgnoreCase("EXCUSED_ABSENCE"))
                    .count();
            final Integer shiftsCompleted = shiftsWorked + doubleShiftCount;

            updatedProjectWeek.setShiftsCompleted(shiftsCompleted);

            final Integer shiftGoalRemainder = currentShiftGoal - shiftsCompleted;
            final Double shiftsNeededPerDay =  (shiftGoalRemainder.doubleValue()/ remainingWorkDays.doubleValue());
            updatedProjectWeek.setShiftsNeeded(shiftsNeededPerDay);
        }


        jpaProjectWeekRepo.save(updatedProjectWeek);

        return updatedProjectWeek;
    }
}
