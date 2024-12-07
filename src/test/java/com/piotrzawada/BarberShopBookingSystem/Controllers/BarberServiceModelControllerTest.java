package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServiceModel_Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = BarberServiceModelController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class BarberServiceModelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    BarberServiceModel_Service service;
    private BarberServiceModel standard, beardTrim;

    @BeforeEach
    public void init() {

        standard = BarberServiceModel.builder()
                .name("Standard Haircut")
                .price(20.00)
                .build();
        beardTrim = BarberServiceModel.builder()
                .name("Beard Trim")
                .price(15.99)
                .build();
    }

    @Test
    @WithMockUser
    void allServices_isOk() throws Exception {
        given(service.getAllServices()).willReturn(List.of(standard,beardTrim));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/services/all"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addService_isCreated() throws Exception {
        given(service.save(ArgumentMatchers.any())).willReturn(standard);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/services/add")
                .param("name", "Standard Haircut")
                .param("price", "20.00")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(standard, service.save(standard));
    }

    @Test
    @WithMockUser
    void addService_byRoleUser_isForbidden() throws Exception {
        given(service.save(ArgumentMatchers.any())).willReturn(standard);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/services/add")
                .param("name", "Standard Haircut")
                .param("price", "20.00")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(standard, service.save(standard));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addService_serviceWithTheSameName_isConflict() throws Exception {
        given(service.getByName(ArgumentMatchers.anyString())).willReturn(standard);
        given(service.save(ArgumentMatchers.any())).willReturn(standard);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/services/add")
                .param("name", "Standard Haircut")
                .param("price", "20.00")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editPrice_isCreated() throws Exception {
        given(service.getByName("standard")).willReturn(standard);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/services/update")
                .param("name", "standard")
                .param("newPrice", "25.50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertEquals(25.50, standard.getPrice());

    }

    @Test
    @WithMockUser
    void editPrice_byRoleUser_isForbidden() throws Exception {
        given(service.getByName("standard")).willReturn(standard);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/services/update")
                .param("name", "standard")
                .param("newPrice", "25.50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertEquals(20.00, standard.getPrice());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editPrice_byRoleUser_isNotFound() throws Exception {
        given(service.getByName(ArgumentMatchers.anyString())).willReturn(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/services/update")
                .param("name", "standard")
                .param("newPrice", "25.50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_isNoContent() throws Exception {
        given(service.getByName(ArgumentMatchers.anyString())).willReturn(standard);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/services/delete")
                .param("name", "standard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_isNotFound() throws Exception {
        given(service.getByName(ArgumentMatchers.anyString())).willReturn(null);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/services/delete")
                .param("name", "standard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_byRoleUser_isForbidden() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/services/delete")
                .param("name", "standard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}