package com.ks.management.recruitment.application.bulkupload;

import com.ks.management.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicationBulkUpload {
    void bulkUpload(MultipartFile file, UserPrincipal userPrincipal);
}
