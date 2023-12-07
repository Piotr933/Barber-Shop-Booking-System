package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingService;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rest Controller for handles REST requests related to the Admins.
 *
 * @author Piotr Zawada
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    BookingService bookingService;

    UserService userService;

    PasswordEncoder encoder;

    /**
     * This method is for register Admin.If AppUser object meet all required criteria, the new admin will be registered.
     *
     * @param user - AppUser object with Admin Details
     * @return Response Entity with response message and status.
     *
     */

    @PostMapping("/register/3{}343d863reg--s")
    public ResponseEntity<Response> register(@RequestBody AppUser user) {

        List<String> adminUsers = List.of("AdminBooking1", "AdminBooking2", "AdminBooking3");
        Response response = new Response();
        int limitOfAdmins = 3;

        if (userService.usersByRole("ROLE_ADMIN").size() >= limitOfAdmins) {
            response.setMessage("Register Admin failed: Limit of admins has been reached");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (!adminUsers.contains(user.nickname)) {
            response.setMessage("Register Admin failed: Wrong credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (userService.userExist(user.email)) {
            response.setMessage("Register Admin failed: The email address is registered already");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        user.setRole("ROLE_ADMIN");
        user.setPassword(encoder.encode(user.getPassword()));
        userService.registerUser(user);
        response.setMessage("Admin successfully registered");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method is to for adding new available slots for booking.The admin need to provide the number of days that
     * needs to be added.
     * Currently, the bookings slots are adding only Monday to Friday: 9-18  every 30 minutes.
     * Price for the booking by default has been set at 20.
     *
     * @param days int parameter that represents number of days to be added
     * @return Response Entity with message and Http Status.
     */

    @PostMapping ("/add")
    public ResponseEntity<Response> addEmptyBookingSlotsFor(@RequestParam int days ) {
        Response response = new Response();

        if (days > 30 || days < 0) {
            response.setMessage("Adding new slots failed: Invalid entry. Please enter the value between 1 and 30 ");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        int updatedSlots = 0;
        LocalDate lastUpdatedAt = LocalDate.from(bookingService.latestDateTime());
        LocalDateTime current= LocalDateTime.of(lastUpdatedAt.getYear(), lastUpdatedAt.getMonth(),
                lastUpdatedAt.getDayOfMonth(), 0, 0);
        LocalDateTime updateTill = current.plusDays(days);

        while (!current.isAfter(updateTill)) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                LocalDateTime startDay = current.withHour(9).withMinute(0);
                LocalDateTime endDay = current.withHour(18).withMinute(0);
                LocalDateTime slotTime = startDay;

                while (!slotTime.isAfter(endDay)) {
                    Booking booking = new Booking(slotTime, 20);
                        bookingService.saveBooking(booking);
                        slotTime = slotTime.plusMinutes(30);
                        updatedSlots++;
                }
            }
            current = current.plusDays(1);
        }
        response.setMessage("Booking slots have been updated successfully: " + updatedSlots);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This method is for display all users bookings.
     * @return Response entity with map of bookings and Http status.
     */
    @GetMapping("/usersBookings")
    public ResponseEntity<?> userBookings() {
        List<Booking> bookings = bookingService.allBooked();

        Map<String, String> bookingsMap = bookings.stream().collect(Collectors.toMap
                (booking -> booking.getLocalDateTime().toString(), booking -> booking.getAppUser().getEmail()));

        return new ResponseEntity<>(bookingsMap, HttpStatus.OK);
    }

    /**
     * This method is for cancel user bookings
     * @param ldt - String parameter that represent the date.
     * @return Response Entity with message and Http status
     */
    @PutMapping ("/cancelBooking")
    public ResponseEntity<Response> cancelBookingByDataTime (@RequestParam String ldt) {
        LocalDateTime localDateTime = LocalDateTime.parse(ldt);
        Booking booking = bookingService.getByDataTime(localDateTime);
        Response response = new Response();

        if (booking.getAppUser() == null) {
            response.setMessage("No appointment is booked for this date and time");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        booking.setAppUser(null);
        bookingService.saveBooking(booking);
        response.setMessage("Booking successfully cancelled");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
