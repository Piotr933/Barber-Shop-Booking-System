package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    BookingService bookingService;

    @Autowired
    UserService userService;


    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

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
            booking.setAppUser(null);
            bookingService.saveBooking(booking);
            response.setMessage("Your Booking has been cancelled");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("Bad request");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/myBookings")
    public ResponseEntity<?> myBookings(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        List<Booking> myBookings = appUser.booking;
        if (myBookings.isEmpty()) {
            return new ResponseEntity<>("You didn't book any visit yet", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(myBookings, HttpStatus.OK);
    }

    @GetMapping("/availableTimes")
    public ResponseEntity<?> getAvailableSlots(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);

        if (localDate.isBefore(LocalDate.now()) || localDate.equals(LocalDate.now())) {
            return new ResponseEntity<>("Wrong date has been chosen", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bookingService.availableByDate(localDate), HttpStatus.OK);
    }
}
