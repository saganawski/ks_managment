package com.ks.management.report.controller;

import com.ks.management.report.Conversion;
import com.ks.management.report.service.ConversionService;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversions")
public class ConversionController {
    @Autowired
    private ConversionService conversionService;

    @PostMapping("/office/{officeId}/start-date/{startDate}/end-date/{endDate}")
    public Conversion getConversionsForOfficeByTimePeriod(@PathVariable("officeId") Integer officeId, @PathVariable("startDate") String startDate,
                                                                @PathVariable("endDate")String endDate,@AuthenticationPrincipal UserPrincipal userPrincipal){
        return conversionService.createConversionForOfficeByTimePeriod(officeId,startDate,endDate,userPrincipal);
    }

    @GetMapping()
    public List<Conversion> getAllConversions(){
        return conversionService.getAllConversions();
    }
}
