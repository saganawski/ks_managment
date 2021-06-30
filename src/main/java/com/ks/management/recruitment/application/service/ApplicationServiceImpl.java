package com.ks.management.recruitment.application.service;

import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.recruitment.application.*;
import com.ks.management.recruitment.application.bulkupload.PetitionCirculator;
import com.ks.management.recruitment.application.dao.ApplicationJpa;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import com.ks.management.recruitment.application.dao.JpaApplicationNote;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationJpa applicationJpa;
    @Autowired
    private JpaOfficeRepo jpaOfficeRepo;
    @Autowired
    private JpaApplicationNote jpaApplicationNote;
    @Autowired
    private ApplicationSourceJpaDao applicationSourceJpaDao;

    @Override
    public Application createApplication(Application application, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        Boolean hasNote = application.getApplicationNotes() != null;
        ApplicationNote note = null;
        if(hasNote){
            note = ApplicationNote.builder()
                    .note(application.getApplicationNotes().get(0).getNote())
                    .createdBy(activeUserId)
                    .updatedBy(activeUserId)
                    .build();
            application.removeNote(application.getApplicationNotes().get(0));
        }
        application.setCreatedBy(activeUserId);
        application.setUpdatedBy(activeUserId);
        final Application savedApplication = applicationJpa.save(application);

        if(hasNote){
            note.setApplication(savedApplication);
            savedApplication.addNote(note);
            applicationJpa.save(savedApplication);
        }

        return savedApplication;
    }

    @Override
    public List<ApplicationDto> findAll() {
        List<Application> applications = applicationJpa.findAll();
        List<ApplicationDto> applicationDtos = applications.stream()
                .map(a ->{
                    final Integer id = Optional.ofNullable(a.getId()).orElse(null);
                    final String firstName = Optional.ofNullable(a.getFirstName()).orElse("");
                    final String lastName = Optional.ofNullable(a.getLastName()).orElse("");
                    final String phoneNumber =Optional.ofNullable(a.getPhoneNumber()).orElse("");
                    final String email = Optional.ofNullable(a.getEmail()).orElse("");
                    final Date dateReceived = Optional.ofNullable(a.getDateReceived()).orElse(null);
                    final Date callBackDate = Optional.ofNullable(a.getCallBackDate()).orElse(null);
                    final Integer updatedBy = Optional.ofNullable(a.getUpdatedBy()).orElse(-1);
                    final Date updatedDate = Optional.ofNullable(a.getUpdatedDate()).orElse(null);
                    final Integer createdBy = Optional.ofNullable(a.getCreatedBy()).orElse(null);
                    final Date createdDate = Optional.ofNullable(a.getCreatedDate()).orElse(null);
                    final ApplicationContactType applicationContactType = Optional.ofNullable(a.getApplicationContactType()).orElse(null);
                    final ApplicationSource applicationSource = Optional.ofNullable(a.getApplicationSource()).orElse(null);
                    final ApplicationResult applicationResult = Optional.ofNullable(a.getApplicationResult()).orElse(null);
                    final Office office = Optional.ofNullable(a.getOffice()).orElse(null);
                    final List<ApplicationNote> applicationNotes = Optional.ofNullable(a.getApplicationNotes()).orElse(Collections.emptyList());

                    return ApplicationDto.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phoneNumber(phoneNumber)
                            .email(email)
                            .dateReceived(dateReceived)
                            .callBackDate(callBackDate)
                            .updatedBy(updatedBy)
                            .updatedDate(updatedDate)
                            .createdBy(createdBy)
                            .createdDate(createdDate)
                            .applicationContactType(applicationContactType)
                            .applicationSource(applicationSource)
                            .applicationResult(applicationResult)
                            .office(office)
                            .applicationNotes(applicationNotes)
                            .build();
                }).collect(Collectors.toList());
        return applicationDtos;
    }

    @Override
    public ApplicationDto findBydId(Integer applicationId) {
        final Application application = applicationJpa.findById(applicationId).orElse(new Application());
        final Integer id = Optional.ofNullable(application.getId()).orElse(null);
        final String firstName = Optional.ofNullable(application.getFirstName()).orElse("");
        final String lastName = Optional.ofNullable(application.getLastName()).orElse("");
        final String phoneNumber =Optional.ofNullable(application.getPhoneNumber()).orElse("");
        final String email = Optional.ofNullable(application.getEmail()).orElse("");
        final Date dateReceived = Optional.ofNullable(application.getDateReceived()).orElse(null);
        final Date callBackDate = Optional.ofNullable(application.getCallBackDate()).orElse(null);
        final Integer updatedBy = Optional.ofNullable(application.getUpdatedBy()).orElse(-1);
        final Date updatedDate = Optional.ofNullable(application.getUpdatedDate()).orElse(null);
        final Integer createdBy = Optional.ofNullable(application.getCreatedBy()).orElse(null);
        final Date createdDate = Optional.ofNullable(application.getCreatedDate()).orElse(null);
        final ApplicationContactType applicationContactType = Optional.ofNullable(application.getApplicationContactType()).orElse(null);
        final ApplicationSource applicationSource = Optional.ofNullable(application.getApplicationSource()).orElse(null);
        final ApplicationResult applicationResult = Optional.ofNullable(application.getApplicationResult()).orElse(null);
        final Office office = Optional.ofNullable(application.getOffice()).orElse(null);
        final List<ApplicationNote> applicationNotes = Optional.ofNullable(application.getApplicationNotes()).orElse(Collections.emptyList());

        return ApplicationDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .dateReceived(dateReceived)
                .callBackDate(callBackDate)
                .updatedBy(updatedBy)
                .updatedDate(updatedDate)
                .createdBy(createdBy)
                .createdDate(createdDate)
                .applicationContactType(applicationContactType)
                .applicationSource(applicationSource)
                .applicationResult(applicationResult)
                .office(office)
                .applicationNotes(applicationNotes)
                .build();
    }

    @Override
    public Application updateApplication(Application application, UserPrincipal userPrincipal) {
        final Integer activeUserid = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        applyNoteToAppIfPresent(application);
        application.setUpdatedBy(activeUserid);

        return applicationJpa.save(application);
    }

    private void applyNoteToAppIfPresent(Application application) {
        if(application.getApplicationNotes() != null){
            final ApplicationNote note = application.getApplicationNotes().stream()
                    .filter(an -> an.getId() == null)
                    .findFirst()
                    .orElse(null);

            if(note != null){
                final int index = application.getApplicationNotes().indexOf(note);
                application.getApplicationNotes().get(index).setApplication(application);
            }

        }
    }

    @Override
    public void deleteApplicationById(Integer applicationId) {
        applicationJpa.deleteById(applicationId);
    }

    @Override
    public void deleteNoteForAppId(int applicationId, int noteId) {
        jpaApplicationNote.deleteById(noteId);
    }

    @Override
    public void bulkUpload(MultipartFile file, UserPrincipal userPrincipal) {
        //TODO: add factory
        final PetitionCirculator petitionCirculator = new PetitionCirculator(applicationJpa,jpaOfficeRepo,jpaApplicationNote,applicationSourceJpaDao);
        petitionCirculator.bulkUpload(file,userPrincipal);
    }
}
