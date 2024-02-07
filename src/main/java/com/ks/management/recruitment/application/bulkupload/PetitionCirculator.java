package com.ks.management.recruitment.application.bulkupload;

import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ApplicationNote;
import com.ks.management.recruitment.application.ApplicationSource;
import com.ks.management.recruitment.application.dao.ApplicationJpa;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import com.ks.management.recruitment.application.dao.JpaApplicationNote;
import com.ks.management.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PetitionCirculator implements ApplicationBulkUpload {
    private ApplicationJpa applicationJpa;
    private JpaOfficeRepo jpaOfficeRepo;
    private JpaApplicationNote jpaApplicationNote;
    private ApplicationSourceJpaDao applicationSourceJpaDao;

    public PetitionCirculator(){}

    public PetitionCirculator(ApplicationJpa applicationJpa, JpaOfficeRepo jpaOfficeRepo, JpaApplicationNote jpaApplicationNote, ApplicationSourceJpaDao applicationSourceJpaDao) {
        this.applicationJpa = applicationJpa;
        this.jpaOfficeRepo = jpaOfficeRepo;
        this.jpaApplicationNote = jpaApplicationNote;
        this.applicationSourceJpaDao = applicationSourceJpaDao;
    }

    @Override
    public void bulkUpload(MultipartFile file, UserPrincipal userPrincipal, Office office) {
        final Integer userId = userPrincipal.getUserId();

        List<String> applications = readFileReturnListOfStringApplications(file);
        //TODO: add metadata to response entity, number of applications it count of applications size minus 1 for header in csv file

        //TODO: throw error if wrong file / parser selected

        applications.stream().skip(1).filter(a -> a.length() > 1).forEach(a -> {
            final List<String> sourceApplication = PetitionCirculatorApplicationBulkUploadCSV.cleanInput(a);
            //TODO: problem here
            final PetitionCirculatorApplicationBulkUploadCSV applicationBulkUpload = new PetitionCirculatorApplicationBulkUploadCSV(sourceApplication);

            final Application savedApplication = createApplication(applicationBulkUpload, userId, office);

            addNoteToApplication(applicationBulkUpload, userId, savedApplication);
        });

    }

    private void addNoteToApplication(PetitionCirculatorApplicationBulkUploadCSV applicationBulkUpload, Integer userId, Application savedApplication) {
        final ApplicationNote note = new ApplicationNote();
        note.setCreatedBy(userId);
        note.setUpdatedBy(userId);
        note.setNote("Added FROM Bulk Upload: \n" + applicationBulkUpload.toString());
        note.setApplication(savedApplication);
        jpaApplicationNote.save(note);
    }

    private Application createApplication(PetitionCirculatorApplicationBulkUploadCSV applicationBulkUpload, Integer userId, Office office) {
        final ApplicationSource indeed = applicationSourceJpaDao.findByCode("INDEED");
        final String firstName = Optional.ofNullable(applicationBulkUpload.getName()).map(n -> n.split(" ")).map(s -> s[0]).orElse(null);
        final String lastName = Optional.ofNullable(applicationBulkUpload.getName()).map(n -> n.split(" ")).map(s -> s[s.length -1]).orElse(null);

        final Application application = Application.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(applicationBulkUpload.getPhone())
                .email(applicationBulkUpload.getEmail())
                .dateReceived(applicationBulkUpload.getDate())
                .applicationSource(indeed)
                .office(office)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        final Application savedApplication = applicationJpa.save(application);
        return savedApplication;
    }


    private List<String> readFileReturnListOfStringApplications(MultipartFile file) {
        try{
            //TODO test this method without reader
            Reader reader = new InputStreamReader(file.getInputStream());

            List<String> applications = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null){
                applications.add(line);
            }
            return applications;

        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Exception: \n" + e.getMessage());
        }
    }
}
