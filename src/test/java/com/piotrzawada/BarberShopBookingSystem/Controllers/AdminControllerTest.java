package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingSlotsService;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc()
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BookingSlotsService bookingSlotsService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser admin, admin2, admin3;
    private AppUser appUser;
    private BookingSlot bookingSlot, bookingSlot2;
    @Autowired
    private PasswordEncoder encoder;


    @BeforeEach
    public void init() {
        admin = AppUser.builder()
                .nickname("AdminBooking1")
                .email("adamAdmin@gmail.com")
                .password("ABCdasddsgsa#2452")
                .build();
        admin2 = AppUser.builder()
                .nickname("AdminBooking2")
                .email("aoifeAdmin@gmail.com")
                .password("ABCdasdgsa#24533f")
                .build();
        admin3 = AppUser.builder()
                .nickname("AdminBooking3")
                .email("tomAdmin@gmail.com")
                .password("ABCdasdgsa#24532")
                .build();

        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();
        bookingSlot =  BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .appUser(appUser)
                .build();
        bookingSlot2 =  BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10,0))
                .appUser(appUser)
                .build();
    }

    @Test
    void registerAdmin_isCreated() throws Exception {
        given(userService.registerUser(ArgumentMatchers.any())).willReturn(admin);

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Admin successfully " +
                        "registered\"}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithAnonymousUser
    void registerAdmin_extraADMIN_isUnauthorized() throws Exception {
        given(userService.usersByRole(ArgumentMatchers.any())).willReturn(List.of(admin, admin2, admin3));

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: Limit " +
                        "of admins has been reached\"}"));
    }

    @Test
    @WithAnonymousUser
    void registerAdmin_wrongCredentials_isUnauthorized() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appUser)));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: Wrong" +
                        " credentials\"}"));
    }

    @Test
    @WithAnonymousUser
    void registerAdmin_sameCredentials_statusConflict() throws Exception {
        given(userService.userExist(ArgumentMatchers.any())).willReturn(true);

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: The" +
                        " email address is registered already\"}"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addEmptyBookingSlotsByDay_isCreated() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(null);
        ResultActions resultActions = mockMvc.perform(post("/api/admin/addSlots")
                .param("localDate", "2024-03-20")
                .param("open", "08:00")
                .param("close", "20:00")
                .param("duration", "30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"25 booking slots added on " +
                        "2024-03-20\"}"));
    }

    @Test
    @WithMockUser()
    void addEmptyBookingSlotsByDay_byRoleUser_isForbidden() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(null);
        ResultActions resultActions = mockMvc.perform(post("/api/admin/addSlots")
                .param("localDate", "2024-03-20")
                .param("open", "08:00")
                .param("close", "20:00")
                .param("duration", "30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void addEmptyBookingSlots_byAnonymousUser_isUnauthorized() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(null);
        ResultActions resultActions = mockMvc.perform(post("/api/admin/addSlots")
                .param("localDate", "2024-03-20")
                .param("open", "08:00")
                .param("close", "20:00")
                .param("duration", "30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeSlotsByDay_isOk() throws Exception {
        given(bookingSlotsService.availableByDate(ArgumentMatchers.any())).willReturn(List.of(bookingSlot, bookingSlot2));

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeSlots")
                .param("localDate", "2024-03-20")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"2 removed  slots on  " +
                        "2024-03-20\"}"));

    }

    @Test
    @WithMockUser
    void removeSlotsByDay_byRoleUser_isForbidden() throws Exception {
        given(bookingSlotsService.availableByDate(ArgumentMatchers.any())).willReturn(List.of(bookingSlot, bookingSlot2));

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeSlots")
                .param("localDate", "2024-03-20")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithAnonymousUser
    void removeSlotsByDay_byAnonymousUser_isUnauthorized() throws Exception {
        given(bookingSlotsService.availableByDate(ArgumentMatchers.any())).willReturn(List.of(bookingSlot, bookingSlot2));

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeSlots")
                .param("localDate", "2024-03-20")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeOneSlotsByDateAndTime_isOk() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeOneSlotBy")
                .param("localDateTime", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"The booking slot has been " +
                        "removed\"}"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removeOneSlotsByDateAndTime_removeNotExistingBookingSlot_isConflict() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(null);

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeOneSlotBy")
                .param("localDateTime", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There is no Booking Slot " +
                        "at this data and time\"}"));
    }

    @Test
    @WithMockUser
    void removeOneSlotsByDateAndTime_byRoleUser_isForbidden() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeOneSlotBy")
                .param("localDateTime", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void removeOneSlotsByDateAndTime_byAnonymousUser_isUnauthorized() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(delete("/api/admin/removeOneSlotBy")
                .param("localDateTime", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void userBookings_isOk() throws Exception {
        given(bookingSlotsService.allBooked()).willReturn(List.of(bookingSlot, bookingSlot2));

        ResultActions resultActions = mockMvc.perform(get("/api/admin/usersBookings")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"2026-09-20T12:30\":" +
                        "\"adam443243433@gmail.com\",\"2026-11-04T10:00\":\"adam443243433@gmail.com\"}"));
    }

    @Test
    @WithMockUser
    void userBookings_byRoleUser_isForbidden() throws Exception {
        given(bookingSlotsService.allBooked()).willReturn(List.of(bookingSlot, bookingSlot2));

        ResultActions resultActions = mockMvc.perform(get("/api/admin/usersBookings")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cancelBookingByDataTime_isOk() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(put("/api/admin/cancelBooking")
                .param("ldt", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Booking successfully cancelled\"}"));

        Assertions.assertNull(bookingSlot.getAppUser());
        Assertions.assertNull(bookingSlot.getName());
        Assertions.assertEquals(0.0, bookingSlot.getPrice());
    }

    @Test
    @WithMockUser
    void cancelBookingByDataTime_byRoleUser_isForbidden() throws Exception {
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(put("/api/admin/cancelBooking")
                .param("ldt", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cancelBookingByDataTime_noExitingBooking_isBadRequest() throws Exception {
        bookingSlot.setAppUser(null);

        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);

        ResultActions resultActions = mockMvc.perform(put("/api/admin/cancelBooking")
                .param("ldt", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"No appointment is booked for this date and time\"}"));
    }
}