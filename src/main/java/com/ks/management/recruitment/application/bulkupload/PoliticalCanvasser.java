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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class PoliticalCanvasser implements ApplicationBulkUpload {
    private ApplicationJpa applicationJpa;
    private JpaOfficeRepo jpaOfficeRepo;
    private JpaApplicationNote jpaApplicationNote;
    private ApplicationSourceJpaDao applicationSourceJpaDao;

    public PoliticalCanvasser(ApplicationJpa applicationJpa, JpaOfficeRepo jpaOfficeRepo, JpaApplicationNote jpaApplicationNote, ApplicationSourceJpaDao applicationSourceJpaDao) {
        this.applicationJpa = applicationJpa;
        this.jpaOfficeRepo = jpaOfficeRepo;
        this.jpaApplicationNote = jpaApplicationNote;
        this.applicationSourceJpaDao = applicationSourceJpaDao;
    }

    @Override
    public HashMap<String, Object> bulkUpload(MultipartFile file, UserPrincipal userPrincipal, Office office) {
        final HashMap<String, Object> responseBody = new HashMap<>();

        final Integer userId = userPrincipal.getUserId();

        List<String> applications = readFileReturnListOfStringApplications(file);
        //TODO: throw error if wrong file / parser selected

        applications.stream().skip(1).filter(a -> a.length() > 1).forEach(a -> {
            final List<String> sourceApplication = PoliticalCanvasserApplicationBulkUploadCSV.cleanInput(a);

            final PoliticalCanvasserApplicationBulkUploadCSV applicationBulkUpload = new PoliticalCanvasserApplicationBulkUploadCSV(sourceApplication);

            final Application savedApplication = createApplication(applicationBulkUpload, userId, office);

            addNoteToApplication(applicationBulkUpload, userId, savedApplication);
        });
        return responseBody;
    }

    private void addNoteToApplication(PoliticalCanvasserApplicationBulkUploadCSV applicationBulkUpload, Integer userId, Application savedApplication) {
        final ApplicationNote note = new ApplicationNote();
        note.setCreatedBy(userId);
        note.setUpdatedBy(userId);
        note.setNote("Added FROM Bulk Upload: \n" + applicationBulkUpload.toString());
        note.setApplication(savedApplication);
        jpaApplicationNote.save(note);
    }

    private Application createApplication(PoliticalCanvasserApplicationBulkUploadCSV applicationBulkUpload, Integer userId, Office office) {
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

    private Office getOfficeFromJobLocation(String sourceJobLocation) {
        Office office = null;

        if(!sourceJobLocation.isEmpty()){
            office = jpaOfficeRepo.findByName(sourceJobLocation).orElse(null);
        }
        return office;
    }

    private List<String> readFileReturnListOfStringApplications(MultipartFile file) {
        try{
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
