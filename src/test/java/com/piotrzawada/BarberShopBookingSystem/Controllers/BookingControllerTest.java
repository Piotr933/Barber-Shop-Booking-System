package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrzawada.BarberShopBookingSystem.Config.TestSecurityConfig;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookingService bookingService;
    @MockBean
    UserService userService;
    @MockBean
    UserDetails userDetails;

    Booking booking, booking2, booking3;

    AppUser appUser;

    @BeforeEach
    public void init() {

        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();

        booking = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .build();
        booking2 = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10, 00))
                .build();
        booking3 = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 4, 10, 30))
                .appUser(appUser)
                .build();
        userDetails = User.withUsername("adam443243433@gmail.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void BookingController_bookVisit_ReturnOK() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking);


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-09-20T12:30")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Your visit has been booked\"}"));

    }

    @Test
    void BookingController_bookVisit_ReturnBadRequest() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-09-20T22:30")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There is not Booking available at this data time\"}"));

    }

    @Test
    void BookingController_bookVisit_ReturnBadRequest2() throws Exception {

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/book")
                .param("localDateTime", "2026-11-04T10:30")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"This time is already booked\"}"));

    }

    @Test
    void BookingController_cancelVisit_returnOk() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:30")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(booking3)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Your Booking has been cancelled\"}"));

        Assertions.assertNull(booking3.getAppUser());
    }

    @Test
    void BookingController_cancelVisit_returnNOT_FOUND() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking2);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:00")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(booking2)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"There is not existing bookings of that data and time\"}"));

    }

    @Test
    void BookingController_cancelVisit_returnBAD_REQUEST() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        booking3.setLocalDateTime(LocalDateTime.now().minusHours(2));
        booking3.setAppUser(appUser);


        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        given(bookingService.getByDataTime(ArgumentMatchers.any())).willReturn(booking3);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/cancel")
                .param("ldt", "2026-11-04T10:00")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(booking3)));


        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"You cannot cancel the booking.Please note that all cancellations needs to be requested at least 24 hours in advance.\"}"));
    }

    @Test
    void BookingController_myBookings_returnOK() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);

        appUser.setBooking(List.of(booking));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/myBookings"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"localDateTime\":\"2026-09-20T12:30:00\",\"price\":0.0}]"));

    }

    @Test
    void BookingController_myBookings_returnNO_CONTENT() throws Exception {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        given(userService.getByEmail(ArgumentMatchers.anyString())).willReturn(appUser);
        List<Booking> bookingList = new ArrayList<>();
        appUser.setBooking(bookingList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/myBookings"));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"You didn't book any visit yet\"}"));
    }

    @Test
    void BookingController_getAvailableSlots_returnOK() throws Exception {
        String date = "2027-11-22";
        List<Booking> bookingList = List.of(booking, booking2);

        given(bookingService.availableByDate(ArgumentMatchers.any())).willReturn(bookingList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/availableTimes")
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(booking)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"localDateTime\":\"2026-09-20T12:30:00\",\"price\":0.0},{\"localDateTime\":\"2026-11-04T10:00:00\",\"price\":0.0}]"));

    }

    @Test
    void BookingController_getAvailableSlots_returnBAD_REQUEST() throws Exception {
        String date = "2020-11-22";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/availableTimes")
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(booking)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Wrong date has been chosen\"}"));
    }
}