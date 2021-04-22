package com.ks.management.report.service;

import com.ks.management.report.Conversion;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface ConversionService {
    Conversion createConversionForOfficeByTimePeriod(Integer officeId, String startDate, String endDate, UserPrincipal userPrincipal);

    List<Conversion> getAllConversions();
}
