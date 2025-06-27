
package com.ks.management.office;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ks.management.office.service.OfficeService;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.service.UserPrincipalDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfficeController.class)
class OfficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeService officeService;
    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private Office office;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        office = new Office();
        Field officeIdField = office.getClass().getDeclaredField("id");
        officeIdField.setAccessible(true);
        officeIdField.set(office, 1);
        office.setName("Test Office");
    }

    @Test
    @WithMockUser
    void getOffices() throws Exception {
        List<Office> offices = Arrays.asList(office, new Office());
        given(officeService.getOffices()).willReturn(offices);

        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllOffices() throws Exception {
        List<Office> offices = Arrays.asList(office, new Office());
        given(officeService.getAllOffices()).willReturn(offices);

        mockMvc.perform(get("/offices/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createOffice() throws Exception {
        given(officeService.createOffice(any(Office.class), any(UserPrincipal.class))).willReturn(office);

        mockMvc.perform(post("/offices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(office)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getOfficeById() throws Exception {
        given(officeService.getOfficeById(1)).willReturn(office);

        mockMvc.perform(get("/offices/{officeId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateOffice() throws Exception {
        given(officeService.updateOffice(any(Office.class), any(UserPrincipal.class))).willReturn(office);

        mockMvc.perform(put("/offices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(office)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateOfficeWithId() throws Exception {
        given(officeService.updateOffice(any(Office.class), eq(1), any(UserPrincipal.class))).willReturn(office);

        mockMvc.perform(put("/offices/{officeId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(office)))
                .andExpect(status().isOk());
    }
}
