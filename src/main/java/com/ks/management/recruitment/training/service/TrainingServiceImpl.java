package com.ks.management.recruitment.training.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.recruitment.interview.ui.InterviewDtoByOffice;
import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.TrainingConfirmationType;
import com.ks.management.recruitment.training.TrainingNote;
import com.ks.management.recruitment.training.dao.JpaTraining;
import com.ks.management.recruitment.training.dao.JpaTrainingConfirmationType;
import com.ks.management.recruitment.training.dao.JpaTrainingNote;
import com.ks.management.recruitment.training.ui.TrainingDto;
import com.ks.management.recruitment.training.ui.TrainingDtoByOffice;
import com.ks.management.security.UserEmployee;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.dao.UserEmployeeJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService{
    @Autowired
    private JpaTraining jpaTraining;
    @Autowired
    private JpaEmployeeRepo employeeRepo;
    @Autowired
    private JpaTrainingConfirmationType jpaTrainingConfirmationType;
    @Autowired
    private JpaTrainingNote jpaTrainingNote;
    @Autowired
    private UserEmployeeJpa userEmployeeJpa;

    @Override
    public List<Training> getAllTrainings() {
        return jpaTraining.findAll();
    }

    @Override
    public Training createTraining(Training training, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        training.setUpdatedBy(activeUserId);
        training.setCreatedBy(activeUserId);
        return jpaTraining.save(training);
    }

    @Override
    public Training getTrainingById(int trainingId) {
        final Optional<Training> training = jpaTraining.findById(trainingId);
        if(!training.isPresent()){
            throw new RuntimeException("Cant find training with ID:" + trainingId);
        }
        return training.get();
    }

    @Override
    public TrainingDto getTrainingDtoById(int trainingId) {
        final Optional<Training> training = jpaTraining.findById(trainingId);
        if(!training.isPresent()){
            throw new RuntimeException("Cant find training with ID:" + trainingId);
        }
        final Set<Employee> trainerOptions = employeeRepo.findAllNonCanvassers();
        final Set<TrainingConfirmationType> confirmationTypes = new HashSet<>(jpaTrainingConfirmationType.findAll());
        final TrainingDto trainingDto = TrainingDto.builder()
                .id(training.map(t -> t.getId()).orElse(-1))
                .application(training.map(t -> t.getApplication()).orElse(null))
                .interview(training.map(t -> t.getInterview()).orElse(null))
                .scheduledTime(training.map(t -> t.getScheduledTime()).orElse(null))
                .trainer(training.map(t -> t.getTrainer()).orElse(null))
                .trainingConfirmationType(training.map(t -> t.getTrainingConfirmationType()).orElse(null))
                .hasShow(training.map(t -> t.getHasShow()).orElse(null))
                .updatedBy(training.map(t -> t.getUpdatedBy()).orElse(null))
                .updatedDate(training.map(t -> t.getUpdatedDate()).orElse(null))
                .createdBy(training.map(t -> t.getCreatedBy()).orElse(null))
                .createdDate(training.map(t -> t.getCreatedDate()).orElse(null))
                .trainingNotes(training.map(t -> t.getTrainingNotes()).orElse(Collections.emptyList()))
                .trainerOptions(trainerOptions)
                .trainingConfirmationTypeOptions(confirmationTypes)
                .build();

        return trainingDto;
    }

    @Override
    public Training updateTraining(Training training, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        training.setUpdatedBy(activeUserId);
        if(training.getTrainingNotes() != null){
            final TrainingNote note = training.getTrainingNotes().get(0);
            note.setCreatedBy(activeUserId);
            note.setUpdatedBy(activeUserId);
            note.setTraining(training);
            training.addTrainingNote(note);
        }
        return jpaTraining.save(training);
    }

    @Override
    public void deleteNote(int noteId) {
        jpaTrainingNote.deleteById(noteId);
    }

    @Override
    public void deleteTraining(int trainingId) {
        jpaTraining.deleteById(trainingId);
    }

    @Override
    public Boolean checkForTraining(Integer applicationId, Integer interviewId) {
        Boolean hasTraining = false;
        final List<Training> trainings = jpaTraining.findAllByApplicationIdAndInterviewId(applicationId,interviewId);
        if(!trainings.isEmpty()){
            hasTraining = true;
        }

        return hasTraining;
    }

    @Override
    public List<Training> getTodaysTrainings(UserPrincipal userPrincipal) {
        final Integer activeUserId = userPrincipal.getUserId();
        final UserEmployee userEmployee = userEmployeeJpa.findByUserId(activeUserId);
        if(userEmployee == null){
            return Collections.emptyList();
        }
        final Integer employeeId = userEmployee.getEmployee().getId();

        final List<Training> trainings = jpaTraining.getTodaysTrainigs(employeeId);

        return trainings;
    }

    @Override
    public List<TrainingDtoByOffice> getAllTrainingsByOfficeId(Integer officeId) {
        final List<Object[]> allTrainingsByOfficeId = jpaTraining.getAllTrainingsByOfficeId(officeId);

        final List<TrainingDtoByOffice> dtos = allTrainingsByOfficeId.stream()
                .map(t -> {
                    final Integer id = (Integer) Optional.ofNullable(t[0]).orElse(-1);
                    final String firstName = (String) Optional.ofNullable(t[1]).orElse("");
                    final String lastName = (String) Optional.ofNullable(t[2]).orElse("");
                    final String phoneNumber = (String) Optional.ofNullable(t[3]).orElse("");
                    final Date scheduledTime = (Date) Optional.ofNullable(t[4]).orElse(null);
                    final String trainerFirstName = (String) Optional.ofNullable(t[5]).orElse("");
                    final String trainerLastName = (String) Optional.ofNullable(t[6]).orElse("");
                    final String officeName = (String) Optional.ofNullable(t[7]).orElse("");

                    String trainerFullName = "";
                    if(!trainerLastName.isEmpty()){
                        trainerFullName = String.format("%s , %s", trainerLastName, trainerFirstName);
                    }
                    final TrainingDtoByOffice dto = TrainingDtoByOffice.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phoneNumber(phoneNumber)
                            .scheduledTime(scheduledTime)
                            .trainer(trainerFullName)
                            .officeName(officeName)
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return dtos;
    }
}
