package com.ks.management.recruitment.application.service;

import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ApplicationDto;
import com.ks.management.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {

    Application createApplication(Application application, UserPrincipal userPrincipal);

    List<ApplicationDto> findAll();

    ApplicationDto findBydId(Integer applicationId);

    Application updateApplication(Application application, UserPrincipal userPrincipal);

    void deleteApplicationById(Integer applicationId);

    void deleteNoteForAppId(int applicationId, int noteId);

    void bulkUpload(MultipartFile file, UserPrincipal userPrincipal, String type);
}
