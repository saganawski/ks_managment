package com.ks.management.office.service;

import java.util.List;

import com.ks.management.office.Office;
import com.ks.management.security.UserPrincipal;

public interface OfficeService {

	List<Office> getOffices();

    Office createOffice(Office office, UserPrincipal userPrincipal);

    Office getOfficeById(Integer officeId);

    Office updateOffice(Office office, UserPrincipal userPrincipal);

    Office updateOffice(Office office, Integer officeId, UserPrincipal userPrincipal);
}
