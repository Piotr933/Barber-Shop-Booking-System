package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingService;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser admin, admin2, admin3;
    private AppUser appUser;
    private Booking booking, booking2;
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
        booking =  Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .appUser(appUser)
                .build();
        booking2 =  Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10,0))
                .appUser(appUser)
                .build();
    }

    @Test
    void adminController_register_returnOK() throws Exception {
        given(userService.registerUser(ArgumentMatchers.any())).willReturn(admin);

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Admin successfully registered\"}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void adminController_register_returnUNAUTHORIZED() throws Exception {
        given(userService.usersByRole(ArgumentMatchers.any())).willReturn(List.of(admin, admin2, admin3));

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: Limit of admins has been reached\"}"));
    }

    @Test
    void adminController_register_returnUNAUTHORIZED2() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appUser)));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: Wrong credentials\"}"));
    }

    @Test
    void adminController_register_returnBAD_REQUEST() throws Exception {
        given(userService.userExist(ArgumentMatchers.any())).willReturn(true);

        ResultActions resultActions = mockMvc.perform(post("/api/admin/register/3{}343d863reg--s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Register Admin failed: The email address is registered already\"}"));
    }

    @Test
    void adminController_addEmptyBookingSlots_returnOK() throws Exception {
        given(bookingService.latestDateTime()).willReturn(LocalDateTime.of(2024, 8, 2, 18,0));

        ResultActions resultActions = mockMvc.perform(post("/api/admin/add")
                .param("days", "1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Booking Slots has been updated: 19\"}"));
    }

    @Test
    void adminController_addEmptyBookingSlots_returnBadRequest() throws Exception {
        given(bookingService.latestDateTime()).willReturn(LocalDateTime.of(2024, 8, 2, 18,0));

        ResultActions resultActions = mockMvc.perform(post("/api/admin/add")
                .param("days", "33")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Adding new slots failed: Invalid entry. Please enter the value between 1 and 30 \"}"));
    }

    @Test
    void adminController_userBookings_returnOK() throws Exception {
        given(bookingService.allBooked()).willReturn(List.of(booking, booking2));

        ResultActions resultActions = mockMvc.perform(get("/api/admin/usersBookings")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"2026-09-20T12:30\":\"adam443243433@gmail.com\",\"2026-11-04T10:00\":\"adam443243433@gmail.com\"}"));
    }

    @Test
    void adminController_cancelBookingByDataTime_returnOK() throws Exception {
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking);

        ResultActions resultActions = mockMvc.perform(put("/api/admin/cancelBooking")
                .param("ldt", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Booking Cancelled\"}"));
    }

    @Test
    void adminController_cancelBookingByDataTime_returnBad_Request() throws Exception {
        booking.setAppUser(null);

        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking);

        ResultActions resultActions = mockMvc.perform(put("/api/admin/cancelBooking")
                .param("ldt", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There is not appointment booked on this date and time\"}"));
    }
}