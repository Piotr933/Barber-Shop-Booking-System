package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServices_Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.mockito.BDDMockito.given;

@WebMvcTest(BarberServicesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class BarberServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BarberServices_Service service;
    private BarberServices standard, beardTrim;

    @BeforeEach
    public void init() {

        standard = BarberServices.builder()
                .name("Standard Haircut")
                .price(20.00)
                .build();
        beardTrim = BarberServices.builder()
                .name("Beard Trim")
                .price(15.99)
                .build();
    }

    @Test
    void allServices_returnListOfBarberServices_statusOk() throws Exception {
        given(service.getAllServices()).willReturn(List.of(standard,beardTrim));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/services/all"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addService_statusCreated() throws Exception {
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
    void editPrice_statusCreated() throws Exception {
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
    void delete_statusNoContent() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/services/delete")
                .param("name", "standard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(standard)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}