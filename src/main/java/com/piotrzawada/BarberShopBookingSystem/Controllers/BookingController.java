package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingService;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Rest Controller for handles REST requests related to the Bookings.
 *
 * @author Piotr Zawada
 * @version 1.0.0
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    BookingService bookingService;

    UserService userService;

    /**
     * Book visit in a Barber
     * @param userDetails - represents the user who make the appointment
     * @param localDateTime - String value of date and time of appointment
     * @return Response Entity with message and Http Status.
     */

    @PutMapping("/book")
    public ResponseEntity<Response> bookVisit(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam String localDateTime) {
        Response response = new Response();
        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        Booking booking = bookingService.getByDataTime(LocalDateTime.parse(localDateTime));

        if (booking == null) {
            response.setMessage("There is not Booking available at this data time");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (booking.getAppUser() == null) {
            booking.setAppUser(appUser);
            bookingService.saveBooking(booking);
            response.setMessage("Your visit has been booked");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("This time is already booked");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Cancel booking by user
     * @param userDetails  represents the user who want to cancel appointment.
     * @param ldt String value of date and time of cancelling appointment
     * @return Response Entity with message and Http Status.
     */

    @PutMapping("/cancel")
    public ResponseEntity<Response> cancelVisit(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam String ldt) {

        LocalDateTime localDateTime = LocalDateTime.parse(ldt);
        Booking booking = bookingService.getByDataTime(localDateTime);
        Response response = new Response();

        if (booking.getAppUser() == null) {
            response.setMessage("There is not existing bookings of that data and time");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (booking.getAppUser().email.equals(userDetails.getUsername())) {

            if (booking.localDateTime.isBefore(LocalDateTime.now().plusHours(24))){
                response.setMessage("You cannot cancel the booking.Please note that all cancellations needs to be " +
                        "requested at least 24 hours in advance.");

                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            booking.setAppUser(null);
            bookingService.saveBooking(booking);
            response.setMessage("Your Booking has been cancelled");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("Bad request");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Display all user bookings
     * @param userDetails represents the user
     * @return Response Entity with list of user bookings and Http Status
     */

    @GetMapping("/myBookings")
    public ResponseEntity<?> myBookings(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        List<Booking> myBookings = appUser.booking;
        if (myBookings.isEmpty()) {
            return new ResponseEntity<>("You didn't book any visit yet", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(myBookings, HttpStatus.OK);
    }

    /**
     * Display the time slots available to book by date
     * @param date String value with date
     * @return Response Entity with list of available times and Http Status
     */
    @GetMapping("/availableTimes")
    public ResponseEntity<?> getAvailableSlots(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);

        if (localDate.isBefore(LocalDate.now()) || localDate.equals(LocalDate.now())) {
            return new ResponseEntity<>("Wrong date has been chosen", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bookingService.availableByDate(localDate), HttpStatus.OK);
    }
}
