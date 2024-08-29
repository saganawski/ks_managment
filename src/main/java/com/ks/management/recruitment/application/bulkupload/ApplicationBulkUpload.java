package com.ks.management.recruitment.application.bulkupload;

import com.ks.management.office.Office;
import com.ks.management.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface ApplicationBulkUpload {
    HashMap<String, Object> bulkUpload(MultipartFile file, UserPrincipal userPrincipal, Office office);
}
