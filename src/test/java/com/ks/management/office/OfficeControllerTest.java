package com.ks.management.office;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ks.management.location.Location;
import com.ks.management.office.service.OfficeService;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.User;
import com.ks.management.security.service.UserPrincipalDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfficeController.class)
@ActiveProfiles("test")
class OfficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeService officeService;

    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private Office testOffice;
    private Location testLocation;
    private UserPrincipal testUserPrincipal;

    @BeforeEach
    void setUp() {
        testLocation = new Location("TestState", "TestCity", "123 Test St", "", "12345", 1, 1);

        testOffice = new Office();
        testOffice.setName("Test Office");
        testOffice.setLocation(testLocation);
        testOffice.setCompleted(false);

        User testUser = new User();
        testUser.setId(123);
        testUserPrincipal = new UserPrincipal(testUser);
    }

    @Test
    @WithMockUser
    void getOffices_ShouldReturnOfficesList() throws Exception {
        List<Office> offices = Arrays.asList(testOffice);
        when(officeService.getOffices()).thenReturn(offices);

        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Office"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    @WithMockUser
    void getOffices_ShouldReturnEmptyListWhenNoOffices() throws Exception {
        when(officeService.getOffices()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void getAllOffices_ShouldReturnAllOffices() throws Exception {
        Office completedOffice = new Office();
        completedOffice.setCompleted(true);
        
        List<Office> allOffices = Arrays.asList(testOffice, completedOffice);
        when(officeService.getAllOffices()).thenReturn(allOffices);

        mockMvc.perform(get("/offices/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void createOffice_ShouldCreateAndReturnOffice() throws Exception {
        when(officeService.createOffice(any(Office.class), any(UserPrincipal.class)))
                .thenReturn(testOffice);

        Office newOffice = new Office();
        newOffice.setName("New Office");
        newOffice.setLocation(testLocation);

        mockMvc.perform(post("/offices")
                .with(user(testUserPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOffice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Office"));
    }

    @Test
    @WithMockUser
    void getOfficeById_ShouldReturnOffice() throws Exception {
        when(officeService.getOfficeById(1)).thenReturn(testOffice);

        mockMvc.perform(get("/offices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Office"));
    }

    @Test
    @WithMockUser
    void getOfficeById_ShouldHandleInvalidId() throws Exception {
        when(officeService.getOfficeById(999)).thenReturn(null);

        mockMvc.perform(get("/offices/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    void updateOffice_WithoutId_ShouldUpdateOffice() throws Exception {
        Office updatedOffice = new Office();
        updatedOffice.setName("Updated Office");
        
        when(officeService.updateOffice(any(Office.class), any(UserPrincipal.class)))
                .thenReturn(updatedOffice);

        mockMvc.perform(put("/offices")
                .with(user(testUserPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOffice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Office"));
    }

    @Test
    @WithMockUser
    void updateOffice_WithId_ShouldUpdateOffice() throws Exception {
        Office updatedOffice = new Office();
        updatedOffice.setName("Updated Office");
        
        when(officeService.updateOffice(any(Office.class), eq(1), any(UserPrincipal.class)))
                .thenReturn(updatedOffice);

        mockMvc.perform(put("/offices/1")
                .with(user(testUserPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOffice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Office"));
    }

    @Test
    void getOffices_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/offices"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void createOffice_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/offices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOffice)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void createOffice_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/offices")
                .with(user(testUserPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getOfficeById_WithInvalidPathVariable_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/offices/invalid"))
                .andExpect(status().isBadRequest());
    }
}