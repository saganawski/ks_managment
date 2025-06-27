package com.ks.management.office;

import com.ks.management.location.Location;
import com.ks.management.office.service.OfficeService;
import com.ks.management.security.User;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeControllerUnitTest {

    @Mock
    private OfficeService officeService;

    @InjectMocks
    private OfficeController officeController;

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
    void testGetOffices_CallsServiceAndReturnsResult() {
        List<Office> expectedOffices = Arrays.asList(testOffice);
        when(officeService.getOffices()).thenReturn(expectedOffices);

        List<Office> result = officeController.getOffices();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Office", result.get(0).getName());
        verify(officeService).getOffices();
    }

    @Test
    void testGetOffices_EmptyList() {
        when(officeService.getOffices()).thenReturn(Collections.emptyList());

        List<Office> result = officeController.getOffices();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(officeService).getOffices();
    }

    @Test
    void testGetAllOffices_CallsServiceAndReturnsResult() {
        Office completedOffice = new Office();
        completedOffice.setName("Completed Office");
        completedOffice.setCompleted(true);

        List<Office> expectedOffices = Arrays.asList(testOffice, completedOffice);
        when(officeService.getAllOffices()).thenReturn(expectedOffices);

        List<Office> result = officeController.getAllOffices();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Office", result.get(0).getName());
        assertEquals("Completed Office", result.get(1).getName());
        verify(officeService).getAllOffices();
    }

    @Test
    void testGetOfficeById_CallsServiceWithCorrectId() {
        when(officeService.getOfficeById(1)).thenReturn(testOffice);

        Office result = officeController.getOfficeById(1);

        assertNotNull(result);
        assertEquals("Test Office", result.getName());
        verify(officeService).getOfficeById(1);
    }

    @Test
    void testGetOfficeById_DifferentIds() {
        when(officeService.getOfficeById(anyInt())).thenReturn(testOffice);

        officeController.getOfficeById(42);
        officeController.getOfficeById(0);
        officeController.getOfficeById(-1);

        verify(officeService).getOfficeById(42);
        verify(officeService).getOfficeById(0);
        verify(officeService).getOfficeById(-1);
    }

    @Test
    void testCreateOffice_CallsServiceWithOfficeAndUserPrincipal() {
        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        when(officeService.createOffice(any(Office.class), any(UserPrincipal.class)))
                .thenReturn(testOffice);

        Office result = officeController.createOffice(newOffice, testUserPrincipal);

        assertNotNull(result);
        assertEquals("Test Office", result.getName());
        verify(officeService).createOffice(newOffice, testUserPrincipal);
    }

    @Test
    void testCreateOffice_PassesCorrectParameters() {
        Office newOffice = new Office();
        newOffice.setName("Parameter Test Office");
        newOffice.setCompleted(true);

        when(officeService.createOffice(eq(newOffice), eq(testUserPrincipal)))
                .thenReturn(newOffice);

        Office result = officeController.createOffice(newOffice, testUserPrincipal);

        assertEquals(newOffice, result);
        verify(officeService).createOffice(newOffice, testUserPrincipal);
    }

    @Test
    void testUpdateOffice_CallsServiceWithOfficeAndUserPrincipal() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office");
        updateOffice.setCompleted(true);

        when(officeService.updateOffice(any(Office.class), any(UserPrincipal.class)))
                .thenReturn(updateOffice);

        Office result = officeController.updateOffice(updateOffice, testUserPrincipal);

        assertNotNull(result);
        assertEquals("Updated Office", result.getName());
        assertTrue(result.getCompleted());
        verify(officeService).updateOffice(updateOffice, testUserPrincipal);
    }

    @Test
    void testUpdateOfficeWithId_CallsServiceWithCorrectParameters() {
        Office updateOffice = new Office();
        updateOffice.setName("Updated Office with ID");

        when(officeService.updateOffice(any(Office.class), eq(42), any(UserPrincipal.class)))
                .thenReturn(updateOffice);

        Office result = officeController.updateOffice(updateOffice, 42, testUserPrincipal);

        assertNotNull(result);
        assertEquals("Updated Office with ID", result.getName());
        verify(officeService).updateOffice(updateOffice, 42, testUserPrincipal);
    }

    @Test
    void testUpdateOfficeWithId_DifferentIds() {
        Office updateOffice = new Office();
        updateOffice.setName("Test Office");

        when(officeService.updateOffice(any(Office.class), anyInt(), any(UserPrincipal.class)))
                .thenReturn(updateOffice);

        officeController.updateOffice(updateOffice, 1, testUserPrincipal);
        officeController.updateOffice(updateOffice, 999, testUserPrincipal);
        officeController.updateOffice(updateOffice, 0, testUserPrincipal);

        verify(officeService).updateOffice(updateOffice, 1, testUserPrincipal);
        verify(officeService).updateOffice(updateOffice, 999, testUserPrincipal);
        verify(officeService).updateOffice(updateOffice, 0, testUserPrincipal);
    }

    @Test
    void testServiceMethodDelegation() {
        // Arrange - setup all service method mocks
        when(officeService.getOffices()).thenReturn(Collections.emptyList());
        when(officeService.getAllOffices()).thenReturn(Collections.emptyList());
        when(officeService.getOfficeById(anyInt())).thenReturn(testOffice);
        when(officeService.createOffice(any(), any())).thenReturn(testOffice);
        when(officeService.updateOffice(any(), any())).thenReturn(testOffice);
        when(officeService.updateOffice(any(), anyInt(), any())).thenReturn(testOffice);

        // Act - call all controller methods
        officeController.getOffices();
        officeController.getAllOffices();
        officeController.getOfficeById(1);
        officeController.createOffice(testOffice, testUserPrincipal);
        officeController.updateOffice(testOffice, testUserPrincipal);
        officeController.updateOffice(testOffice, 1, testUserPrincipal);

        // Assert - verify all service methods were called
        verify(officeService).getOffices();
        verify(officeService).getAllOffices();
        verify(officeService).getOfficeById(1);
        verify(officeService).createOffice(testOffice, testUserPrincipal);
        verify(officeService).updateOffice(testOffice, testUserPrincipal);
        verify(officeService).updateOffice(testOffice, 1, testUserPrincipal);
    }

    @Test
    void testControllerReturnsExactServiceResults() {
        // Test that controller doesn't modify service results
        Office serviceResult = new Office();
        serviceResult.setName("Service Result");
        serviceResult.setCompleted(true);
        serviceResult.setCreatedBy(123);

        when(officeService.getOfficeById(1)).thenReturn(serviceResult);

        Office controllerResult = officeController.getOfficeById(1);

        assertSame(serviceResult, controllerResult);
        assertEquals("Service Result", controllerResult.getName());
        assertTrue(controllerResult.getCompleted());
        assertEquals(Integer.valueOf(123), controllerResult.getCreatedBy());
    }

    @Test
    void testNullHandling() {
        // Test controller behavior with null inputs
        when(officeService.createOffice(isNull(), eq(testUserPrincipal))).thenReturn(testOffice);
        when(officeService.updateOffice(isNull(), eq(testUserPrincipal))).thenReturn(testOffice);

        Office result1 = officeController.createOffice(null, testUserPrincipal);
        Office result2 = officeController.updateOffice(null, testUserPrincipal);

        assertNotNull(result1);
        assertNotNull(result2);
        verify(officeService).createOffice(null, testUserPrincipal);
        verify(officeService).updateOffice(null, testUserPrincipal);
    }

    @Test
    void testControllerAutowiring() {
        // Verify the controller has the service injected
        assertNotNull(officeController);
        
        // Test that service methods are accessible
        when(officeService.getOffices()).thenReturn(Collections.emptyList());
        
        List<Office> result = officeController.getOffices();
        
        assertNotNull(result);
        verify(officeService).getOffices();
    }

    @Test
    void testMultipleMethodOverloads() {
        // Test that both update methods work correctly
        Office office1 = new Office();
        office1.setName("Office 1");
        
        Office office2 = new Office();
        office2.setName("Office 2");

        when(officeService.updateOffice(eq(office1), eq(testUserPrincipal))).thenReturn(office1);
        when(officeService.updateOffice(eq(office2), eq(5), eq(testUserPrincipal))).thenReturn(office2);

        // Call both overloaded methods
        Office result1 = officeController.updateOffice(office1, testUserPrincipal);
        Office result2 = officeController.updateOffice(office2, 5, testUserPrincipal);

        assertEquals("Office 1", result1.getName());
        assertEquals("Office 2", result2.getName());
        
        verify(officeService).updateOffice(office1, testUserPrincipal);
        verify(officeService).updateOffice(office2, 5, testUserPrincipal);
    }

    @Test
    void testPathVariableIdHandling() {
        // Test various ID values including edge cases
        when(officeService.getOfficeById(anyInt())).thenReturn(testOffice);

        officeController.getOfficeById(Integer.MAX_VALUE);
        officeController.getOfficeById(Integer.MIN_VALUE);
        officeController.getOfficeById(1);

        verify(officeService).getOfficeById(Integer.MAX_VALUE);
        verify(officeService).getOfficeById(Integer.MIN_VALUE);
        verify(officeService).getOfficeById(1);
    }
}