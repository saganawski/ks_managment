
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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceImplTest {

    @Mock
    private JpaOfficeRepo jpaOfficeRepo;
    @Mock
    private JpaLocationDao jpaLocationDao;
    @Mock
    private UserPrincipal userPrincipal;

    @InjectMocks
    private OfficeServiceImpl officeService;

    private Office office;
    private Location location;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        location = new Location("TS", "Test City", "123 Test St", null, "12345", 1, 1);
        Field locationIdField = location.getClass().getDeclaredField("id");
        locationIdField.setAccessible(true);
        locationIdField.set(location, 1);

        office = new Office();
        Field officeIdField = office.getClass().getDeclaredField("id");
        officeIdField.setAccessible(true);
        officeIdField.set(office, 1);
        office.setName("Test Office");
        office.setLocation(location);
        office.setCompleted(false);
    }

    @Test
    void createOffice() {
        when(userPrincipal.getUserId()).thenReturn(1);

        officeService.createOffice(office, userPrincipal);

        verify(jpaLocationDao).save(location);
        verify(jpaOfficeRepo).save(office);
    }

    @Test
    void getOfficeById() {
        when(jpaOfficeRepo.getOne(1)).thenReturn(office);

        Office result = officeService.getOfficeById(1);

        assertEquals(office, result);
    }

    @Test
    void getAllOffices() {
        List<Office> offices = Arrays.asList(office, new Office());
        when(jpaOfficeRepo.findAll()).thenReturn(offices);

        List<Office> result = officeService.getAllOffices();

        assertEquals(2, result.size());
    }

    @Test
    void updateOffice() {
        when(userPrincipal.getUserId()).thenReturn(1);

        officeService.updateOffice(office, userPrincipal);

        verify(jpaLocationDao).save(location);
        verify(jpaOfficeRepo).save(office);
    }

    @Test
    void getOffices_shouldReturnNonCompletedOffices() {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);

        Office nullCompletedOffice = new Office();
        nullCompletedOffice.setCompleted(null);

        List<Office> offices = Arrays.asList(office, completedOffice, nullCompletedOffice);
        when(jpaOfficeRepo.findAll()).thenReturn(offices);

        List<Office> result = officeService.getOffices();

        assertEquals(1, result.size());
        assertEquals(false, result.get(0).getCompleted());
    }
}
