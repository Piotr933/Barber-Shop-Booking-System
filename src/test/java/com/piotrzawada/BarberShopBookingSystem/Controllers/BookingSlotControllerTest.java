package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServiceModel_Service;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class BookingSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookingSlotsService bookingSlotsService;

    @MockBean
    BarberServiceModel_Service barberService;
    @MockBean
    UserService userService;
    @MockBean
    UserDetails userDetails;

    BookingSlot bookingSlot, bookingSlot2, bookingSlot3;

    AppUser appUser;

    BarberServiceModel barberServiceModel;

    @BeforeEach
    public void init() {

        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();

        bookingSlot = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .build();
        bookingSlot2 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10, 00))
                .build();
        bookingSlot3 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10, 30))
                .appUser(appUser)
                .build();
        barberServiceModel = BarberServiceModel.builder()
                .name("Standard Haircut")
                .price(25.00).build();
        userDetails = User.withUsername("adam443243433@gmail.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    @WithMockUser
    void BookingController_bookVisit_statusOK() throws Exception {

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);
        given(barberService.getByName(ArgumentMatchers.anyString())).willReturn(barberServiceModel);



        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-09-20T12:30")
                .param("name", "Standard Haircut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Your Standard Haircut has been successfully booked\"}"));

    }

    @Test
    @WithAnonymousUser
    void BookingController_bookVisit_statusUnauthorised() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot);


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-09-20T12:30")
                .param("name", "Standard Haircut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void BookingController_bookVisit_statusBadRequest() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-09-20T22:30")
                .param("name", "Standard Haircut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"No booking is available at this date and time\"}"));

    }

    @Test
    @WithMockUser
    void BookingController_bookVisit_statusConflict() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-11-04T10:30")
                .param("name", "Standard Haircut")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"This time slot is already booked\"}"));

    }

    @Test
    @WithMockUser
    void BookingController_cancelVisit_statusOk() throws Exception {

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:30")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(bookingSlot3)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Your booking has been successfully cancelled\"}"));

        Assertions.assertNull(bookingSlot3.getAppUser());
    }

    @Test
    @WithMockUser
    void BookingController_cancelVisit_statusNOT_FOUND() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot2);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:00")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(bookingSlot2)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"No existing bookings for that date and time\"}"));

    }

    @Test
    @WithMockUser
    void BookingController_cancelVisit_statusBAD_REQUEST() throws Exception {

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        bookingSlot3.setLocalDateTime(LocalDateTime.now().minusHours(2));
        bookingSlot3.setAppUser(appUser);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingSlotsService.getByDataTime(ArgumentMatchers.any())).willReturn(bookingSlot3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:00")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(bookingSlot3)));


        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"You cannot cancel the booking.Please note that all cancellations needs to be requested at least 24 hours in advance\"}"));
    }

    @Test
    @WithMockUser
    void BookingController_myBookings_statusOK() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);

        appUser.setBookingSlot(List.of(bookingSlot));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/myBookings"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"localDateTime\":\"2026-09-20T12:30:00\",\"price\":0.0}]"));

    }

    @Test
    @WithAnonymousUser
    void BookingController_myBookings_statusUnauthorized() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);

        appUser.setBookingSlot(List.of(bookingSlot));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/myBookings"));
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void BookingController_myBookings_statusNO_CONTENT() throws Exception {
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        List<BookingSlot> bookingSlotList = new ArrayList<>();
        appUser.setBookingSlot(bookingSlotList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/myBookings"));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"You haven't booked any visits yet\"}"));
    }

    @Test
    @WithAnonymousUser
    void BookingController_getAvailableSlots_statusOK() throws Exception {
        String date = "2027-11-22";
        List<BookingSlot> bookingSlotList = List.of(bookingSlot, bookingSlot2);

        given(bookingSlotsService.availableByDate(ArgumentMatchers.any())).willReturn(bookingSlotList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/availableTimes")
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[\"12:30\",\"10:00\"]"));

    }
    @Test
    @WithAnonymousUser
    void BookingController_getAvailableSlots_statusBAD_REQUEST() throws Exception {
        String date = "2020-11-22";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/availableTimes")
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingSlot)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wrong date has been chosen\"}"));
    }
}