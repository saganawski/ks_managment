package com.ks.management.office.service;

import com.ks.management.location.Location;
import com.ks.management.location.dao.JpaLocationDao;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceImplTest {

    @Mock
    private JpaOfficeRepo jpaOfficeRepo;

    @Mock
    private JpaLocationDao jpaLocationDao;

    @InjectMocks
    private OfficeServiceImpl officeService;

    private Office testOffice;
    private Location testLocation;
    private UserPrincipal testUserPrincipal;

    @BeforeEach
    void setUp() {
        testLocation = new Location("TestState", "TestCity", "123 Test St", "", "12345", 1, 1);

        testOffice = new Office();
        // testOffice.setId(1); // ID is auto-generated
        testOffice.setName("Test Office");
        testOffice.setLocation(testLocation);
        testOffice.setCompleted(false);

        testUserPrincipal = mock(UserPrincipal.class);
    }

    @Test
    void getOffices_ShouldReturnOnlyNonCompletedOffices() {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);

        Office nullCompletedOffice = new Office();
        nullCompletedOffice.setCompleted(null);

        when(jpaOfficeRepo.findAll()).thenReturn(Arrays.asList(testOffice, completedOffice, nullCompletedOffice));

        List<Office> result = officeService.getOffices();

        assertEquals(1, result.size());
        assertEquals(testOffice.getId(), result.get(0).getId());
        assertFalse(result.get(0).getCompleted());
    }

    @Test
    void getOffices_ShouldReturnEmptyListWhenNoNonCompletedOffices() {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);

        when(jpaOfficeRepo.findAll()).thenReturn(Collections.singletonList(completedOffice));

        List<Office> result = officeService.getOffices();

        assertTrue(result.isEmpty());
    }

    @Test
    void createOffice_ShouldSetAuditFieldsAndDefaultCompleted() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);
        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);

        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        Office result = officeService.createOffice(newOffice, testUserPrincipal);

        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(newOffice);

        assertEquals(123, newOffice.getCreatedBy());
        assertEquals(123, newOffice.getUpdatedBy());
        assertEquals(123, testLocation.getCreatedBy());
        assertEquals(123, testLocation.getUpdatedBy());
        assertFalse(newOffice.getCompleted());
    }

    @Test
    void createOffice_ShouldPreserveCompletedWhenSet() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        testOffice.setCompleted(true);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        Office result = officeService.createOffice(testOffice, testUserPrincipal);

        assertTrue(testOffice.getCompleted());
    }

    @Test
    void createOffice_ShouldHandleNullCompleted() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        testOffice.setCompleted(null);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        Office result = officeService.createOffice(testOffice, testUserPrincipal);

        assertFalse(testOffice.getCompleted());
    }

    @Test
    void getOfficeById_ShouldReturnOffice() {
        when(jpaOfficeRepo.getOne(1)).thenReturn(testOffice);

        Office result = officeService.getOfficeById(1);

        assertEquals(testOffice, result);
        verify(jpaOfficeRepo).getOne(1);
    }

    @Test
    void updateOffice_ShouldSetUpdatedByAndSaveLocation() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);
        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);

        Office result = officeService.updateOffice(testOffice, testUserPrincipal);

        assertEquals(123, testOffice.getUpdatedBy());
        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(testOffice);
        assertEquals(testOffice, result);
    }

    @Test
    void updateOfficeWithId_ShouldSetUpdatedByAndSaveLocation() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);
        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);

        Integer officeId = 1;
        Office result = officeService.updateOffice(testOffice, officeId, testUserPrincipal);

        assertEquals(123, testOffice.getUpdatedBy());
        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(testOffice);
        assertEquals(testOffice, result);
    }

    @Test
    void getAllOffices_ShouldReturnAllOfficesIncludingCompleted() {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);
        
        List<Office> allOffices = Arrays.asList(testOffice, completedOffice);
        when(jpaOfficeRepo.findAll()).thenReturn(allOffices);

        List<Office> result = officeService.getAllOffices();

        assertEquals(2, result.size());
        assertEquals(allOffices, result);
    }

    @Test
    void createOffice_ShouldHandleNullUserPrincipal() {
        assertThrows(NullPointerException.class, () -> {
            officeService.createOffice(testOffice, null);
        });
    }

    @Test
    void createOffice_ShouldHandleNullOffice() {
        assertThrows(NullPointerException.class, () -> {
            officeService.createOffice(null, testUserPrincipal);
        });
    }

    @Test
    void updateOffice_WithNullLocation_ShouldCallSaveWithNullLocation() {
        when(testUserPrincipal.getUserId()).thenReturn(123);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);
        testOffice.setLocation(null);
        
        Office result = officeService.updateOffice(testOffice, testUserPrincipal);
        
        assertEquals(123, testOffice.getUpdatedBy());
        verify(jpaLocationDao).save(null);
        verify(jpaOfficeRepo).save(testOffice);
    }
}