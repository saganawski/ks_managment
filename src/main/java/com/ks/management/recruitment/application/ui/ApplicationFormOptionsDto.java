package com.ks.management.recruitment.application.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.recruitment.application.ApplicationContactType;
import com.ks.management.recruitment.application.ApplicationResult;
import com.ks.management.recruitment.application.ApplicationSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicationFormOptionsDto {
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<ApplicationContactType> applicationContactTypes = new HashSet<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<ApplicationSource> applicationSources = new HashSet<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<ApplicationResult> applicationResults = new HashSet<>();

}
