package com.ks.management.recruitment.application.bulkupload;

import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.recruitment.application.dao.ApplicationJpa;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import com.ks.management.recruitment.application.dao.JpaApplicationNote;

public class BulkUploadFactory {
    private ApplicationJpa applicationJpa;
    private JpaOfficeRepo jpaOfficeRepo;
    private JpaApplicationNote jpaApplicationNote;
    private ApplicationSourceJpaDao applicationSourceJpaDao;

    public BulkUploadFactory(ApplicationJpa applicationJpa, JpaOfficeRepo jpaOfficeRepo, JpaApplicationNote jpaApplicationNote, ApplicationSourceJpaDao applicationSourceJpaDao) {
        this.applicationJpa = applicationJpa;
        this.jpaOfficeRepo = jpaOfficeRepo;
        this.jpaApplicationNote = jpaApplicationNote;
        this.applicationSourceJpaDao = applicationSourceJpaDao;
    }

    public ApplicationBulkUpload createBulkUploadType(String type){
        if(type.equalsIgnoreCase("PetitionCirculator") || type.equalsIgnoreCase("arizona")) {
            return new PetitionCirculator(applicationJpa, jpaOfficeRepo, jpaApplicationNote, applicationSourceJpaDao);
        }
        /*} else if(type.equalsIgnoreCase("PoliticalCanvasser")){
            return  new PoliticalCanvasser(applicationJpa,jpaOfficeRepo,jpaApplicationNote,applicationSourceJpaDao);
        } else if(type.equalsIgnoreCase("DoorDoor")){
            return new DoorDoorCanvasser(applicationJpa,jpaOfficeRepo,jpaApplicationNote,applicationSourceJpaDao);
        }*/
        else {
            throw new RuntimeException(String.format("Unable to find bulkUpload type or: '%s ', Select a different type or contact tech support",type));
        }
    }
}
