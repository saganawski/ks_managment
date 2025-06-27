package com.ks.management.office.service;

import com.ks.management.location.Location;
import com.ks.management.location.dao.JpaLocationDao;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.security.User;
import com.ks.management.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        testLocation = new Location("CA", "Los Angeles", "123 Main St", "Suite 100", "90210", 1, 1);

        testOffice = new Office();
        testOffice.setName("Test Office");
        testOffice.setLocation(testLocation);
        testOffice.setCompleted(false);
        testOffice.setCreatedBy(1);
        testOffice.setUpdatedBy(1);

        User mockUser = User.builder()
                .id(1)
                .username("testuser")
                .password("password")
                .isActive(true)
                .roles("USER")
                .permissions("READ,WRITE")
                .build();
        testUserPrincipal = new UserPrincipal(mockUser);
    }

    @Test
    void testGetOffices_FiltersCompletedOffices() {
        Office completedOffice = new Office();
        completedOffice.setName("Completed Office");
        completedOffice.setCompleted(true);

        Office activeOffice = new Office();
        activeOffice.setName("Active Office");
        activeOffice.setCompleted(false);

        Office nullCompletedOffice = new Office();
        nullCompletedOffice.setName("Null Completed Office");
        nullCompletedOffice.setCompleted(null);

        List<Office> allOffices = Arrays.asList(testOffice, completedOffice, activeOffice, nullCompletedOffice);
        when(jpaOfficeRepo.findAll()).thenReturn(allOffices);

        List<Office> result = officeService.getOffices();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(o -> "Test Office".equals(o.getName())));
        assertTrue(result.stream().anyMatch(o -> "Active Office".equals(o.getName())));
        assertTrue(result.stream().allMatch(o -> !o.getCompleted()));
    }

    @Test
    void testGetAllOffices_ReturnsAllIncludingCompleted() {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);
        
        List<Office> allOffices = Arrays.asList(testOffice, completedOffice);
        when(jpaOfficeRepo.findAll()).thenReturn(allOffices);

        List<Office> result = officeService.getAllOffices();

        assertEquals(2, result.size());
        verify(jpaOfficeRepo).findAll();
    }

    @Test
    void testCreateOffice_ValidInput_Success() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        Office result = officeService.createOffice(newOffice, testUserPrincipal);

        assertNotNull(result);
        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(newOffice);
        assertEquals(Integer.valueOf(1), newOffice.getCreatedBy());
        assertEquals(Integer.valueOf(1), newOffice.getUpdatedBy());
        assertFalse(newOffice.getCompleted());
    }

    @Test
    void testCreateOffice_NullCompleted_DefaultsToFalse() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);
        newOffice.setCompleted(null);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        Office result = officeService.createOffice(newOffice, testUserPrincipal);

        assertFalse(newOffice.getCompleted());
        verify(jpaOfficeRepo).save(newOffice);
    }

    @Test
    void testCreateOffice_CompletedTrue_RemainsTrue() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);
        newOffice.setCompleted(true);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        Office result = officeService.createOffice(newOffice, testUserPrincipal);

        assertTrue(newOffice.getCompleted());
        verify(jpaOfficeRepo).save(newOffice);
    }

    @Test
    void testCreateOffice_SetsAuditFields() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(testOffice);

        officeService.createOffice(newOffice, testUserPrincipal);

        assertEquals(Integer.valueOf(1), newOffice.getCreatedBy());
        assertEquals(Integer.valueOf(1), newOffice.getUpdatedBy());
        assertEquals(Integer.valueOf(1), testLocation.getCreatedBy());
        assertEquals(Integer.valueOf(1), testLocation.getUpdatedBy());
    }

    @Test
    void testGetOfficeById_ValidId_ReturnsOffice() {
        when(jpaOfficeRepo.getOne(1)).thenReturn(testOffice);

        Office result = officeService.getOfficeById(1);

        assertNotNull(result);
        assertEquals(testOffice, result);
        verify(jpaOfficeRepo).getOne(1);
    }

    @Test
    void testUpdateOffice_ValidInput_UpdatesAuditFields() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office");
        updateOffice.setLocation(testLocation);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(updateOffice);

        Office result = officeService.updateOffice(updateOffice, testUserPrincipal);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), updateOffice.getUpdatedBy());
        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(updateOffice);
    }

    @Test
    void testUpdateOfficeWithId_ValidInput_UpdatesAuditFields() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office");
        updateOffice.setLocation(testLocation);

        when(jpaLocationDao.save(any(Location.class))).thenReturn(testLocation);
        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(updateOffice);

        Office result = officeService.updateOffice(updateOffice, 1, testUserPrincipal);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), updateOffice.getUpdatedBy());
        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(updateOffice);
    }

    @Test
    void testLocationPersistence_SavesLocationFirst() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        when(jpaLocationDao.save(testLocation)).thenReturn(testLocation);
        when(jpaOfficeRepo.save(newOffice)).thenReturn(testOffice);

        officeService.createOffice(newOffice, testUserPrincipal);

        verify(jpaLocationDao).save(testLocation);
        verify(jpaOfficeRepo).save(newOffice);
        assertEquals(testLocation, newOffice.getLocation());
    }

    @Test
    void testCreateOffice_NullUserPrincipal_ThrowsException() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        assertThrows(NullPointerException.class, () -> {
            officeService.createOffice(newOffice, null);
        });
    }

    @Test
    void testUpdateOffice_NullUserPrincipal_ThrowsException() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office");
        updateOffice.setLocation(testLocation);

        assertThrows(NullPointerException.class, () -> {
            officeService.updateOffice(updateOffice, null);
        });
    }

    @Test
    void testCreateOffice_NullLocation_ThrowsException() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(null);

        assertThrows(NullPointerException.class, () -> {
            officeService.createOffice(newOffice, testUserPrincipal);
        });
    }

    @Test
    void testUpdateOffice_NullLocation_HandlesGracefully() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office");
        updateOffice.setLocation(null);

        when(jpaOfficeRepo.save(any(Office.class))).thenReturn(updateOffice);

        Office result = officeService.updateOffice(updateOffice, testUserPrincipal);
        
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getUpdatedBy());
        verify(jpaOfficeRepo).save(updateOffice);
    }

    @Test
    void testGetOffices_EmptyList_ReturnsEmptyList() {
        when(jpaOfficeRepo.findAll()).thenReturn(Arrays.asList());

        List<Office> result = officeService.getOffices();

        assertTrue(result.isEmpty());
        verify(jpaOfficeRepo).findAll();
    }

    @Test
    void testGetOffices_AllCompletedOffices_ReturnsEmptyList() {
        Office completedOffice1 = new Office();
        completedOffice1.setCompleted(true);
        
        Office completedOffice2 = new Office();
        completedOffice2.setCompleted(true);

        when(jpaOfficeRepo.findAll()).thenReturn(Arrays.asList(completedOffice1, completedOffice2));

        List<Office> result = officeService.getOffices();

        assertTrue(result.isEmpty());
    }
}