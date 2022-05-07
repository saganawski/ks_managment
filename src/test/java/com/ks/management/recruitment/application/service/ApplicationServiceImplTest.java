package com.ks.management.recruitment.application.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationServiceImplTest {

    @Test
    void createApplication() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findBydId() {
    }

    @Test
    void updateApplication() {
    }

    @Test
    void deleteApplicationById() {
    }

    @Test
    void deleteNoteForAppId() {
    }

    @Test
    void bulkUpload() {
    }

    @Test
    void shouldReturnLocalDateTimeOverUtc(){
        final LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        final LocalDateTime minus = now.minus(4, ChronoUnit.HOURS);
        System.out.println(minus);
    }

}