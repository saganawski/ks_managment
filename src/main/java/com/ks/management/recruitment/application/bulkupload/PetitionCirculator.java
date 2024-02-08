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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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
    public HashMap<String, Object> bulkUpload(MultipartFile file, UserPrincipal userPrincipal, Office office) {
        final HashMap<String, Object> responseBody = new HashMap<>();

        final Integer userId = userPrincipal.getUserId();
        final List<String> applications = new ArrayList<>();

        try {
             applications.addAll(readFileReturnListOfStringApplications(file));
        }catch (IOException e){
            log.error("Exception Unable to read file: \n" + e.getMessage());
            // add to response file failed to read and return;
            responseBody.put("error", "Unable to read file");
            return responseBody;
        };

        responseBody.put("lineCount:", "Number of applications in file : " + (applications.size() - 1));
        final AtomicInteger applicationsCreated = new AtomicInteger(0);

        applications.stream().skip(1).filter(a -> a.length() > 1).forEach(a -> {
            final List<String> sourceApplication = PetitionCirculatorApplicationBulkUploadCSV.cleanInput(a);
            // if sourceApplication is size is not 18 log error, add line number to response body, skip iteration
            if(sourceApplication.size() != 18){

                final int lineNumber = applications.indexOf(a) +1;

                log.error("Error: BAD DATA. line size is not 18 columns when split , line: {} Line in file skipped is number {}", sourceApplication, lineNumber);
                responseBody.put( String.format( "error line number %s", lineNumber), String.format( "BAD DATA. line number skipped %s size is not 18 columns when split",  lineNumber));

                return;
            }

            try{
                final PetitionCirculatorApplicationBulkUploadCSV applicationBulkUpload = new PetitionCirculatorApplicationBulkUploadCSV(sourceApplication);

                final Application savedApplication = createApplication(applicationBulkUpload, userId, office);

                addNoteToApplication(applicationBulkUpload, userId, savedApplication);

            }catch (Exception e){

                final int lineNumber = applications.indexOf(a) +1;

                log.error("Error: BAD DATA. line size is not 18 columns when split , line: {} Line in file skipped is number {}", sourceApplication, lineNumber);
                log.error("Error: Unable to make Application \n" + e.getMessage());
                responseBody.put( String.format( "error line number %s", lineNumber), String.format( "BAD DATA. line number skipped %s",  lineNumber));
                return;
            };

            applicationsCreated.getAndIncrement();
        });

        responseBody.put("TotalApplicantsCreated", "Number of applications created : " + applicationsCreated.get());

        return responseBody;
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


    private List<String> readFileReturnListOfStringApplications(MultipartFile file) throws IOException {
        final List<String> applications = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String line;
        while((line = bufferedReader.readLine()) != null){
            applications.add(line);
        }
        return applications;
    }
}
