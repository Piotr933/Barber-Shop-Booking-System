package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServiceModel_Service;
import com.piotrzawada.BarberShopBookingSystem.Services.BookingSlotsService;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Rest Controller for handles REST requests related to the Bookings.
 *
 * @author Piotr Zawada
 * @version 1.3
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    BookingSlotsService bookingSlotsService;

    UserService userService;

    BarberServiceModel_Service barberService;

    /**
     * Book visit in a Barber
     * @param userDetails - details of the user who make a booking
     * @param localDateTime - date and time of the appointment
     * @param name - name of the service example:Dry Hair Cut
     * @return Response Entity with message and HttpStatus
     */
    @PutMapping("/book")
    public ResponseEntity<Response> bookVisit(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam String localDateTime, @RequestParam String name) {
        Response response = new Response();
        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        BookingSlot bookingSlot = bookingSlotsService.getByDataTime(LocalDateTime.parse(localDateTime));


        if (bookingSlot == null) {
            response.setMessage("No booking is available at this date and time");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (bookingSlot.getAppUser() == null) {

            bookingSlot.setAppUser(appUser);
            bookingSlot.setPrice(barberService.getByName(name).getPrice());
            bookingSlot.setName(name);
            bookingSlotsService.saveBooking(bookingSlot);
            response.setMessage("Your "+ name + " has been successfully booked");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("This time slot is already booked");

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Cancel booking by user
     * @param userDetails  details of the user who want to cancel the booking
     * @param ldt date and time
     * @return Response Entity with message and HttpStatus
     */

    @PutMapping("/cancel")
    public ResponseEntity<Response> cancelVisit(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam String ldt) {
        LocalDateTime localDateTime = LocalDateTime.parse(ldt);
        BookingSlot bookingSlot = bookingSlotsService.getByDataTime(localDateTime);
        Response response = new Response();

        if (bookingSlot.getAppUser() == null) {
            response.setMessage("No existing bookings for that date and time");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (bookingSlot.getAppUser().email.equals(userDetails.getUsername())) {

            if (bookingSlot.localDateTime.isBefore(LocalDateTime.now().plusHours(24))){
                response.setMessage("You cannot cancel the booking.Please note that all cancellations needs to be " +
                        "requested at least 24 hours in advance");

                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }

            bookingSlot.setAppUser(null);
            bookingSlot.setName(null);
            bookingSlot.setPrice(0.0);
            bookingSlotsService.saveBooking(bookingSlot);
            response.setMessage("Your booking has been successfully cancelled");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("Bad request");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Display all existing and past customer bookings
     * @param userDetails details of the user who want to see his/her bookings details
     * @return  Response Entity with map of user bookings and HttpStatus
     */

    @GetMapping("/myBookings")
    public ResponseEntity<?> myBookings(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        List<BookingSlot> myBookingSlots = appUser.bookingSlot;
        Response response = new Response();

        if (myBookingSlots.isEmpty()) {
            response.setMessage("You haven't booked any visits yet");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(myBookingSlots, HttpStatus.OK);
    }

    /**
     * Display the time slots available to book by date
     * @param date String value with date
     * @return Response Entity (List of strings in hour format HH:mm, Http Status)
     */
    @GetMapping("/availableTimes")
    public ResponseEntity<?> getAvailableSlots(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);

        if (localDate.isBefore(LocalDate.now()) || localDate.equals(LocalDate.now())) {
            Response response = new Response();
            response.setMessage("Wrong date has been chosen");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        List<LocalDateTime> availableTimes = bookingSlotsService.availableByDate(localDate)
                .stream().map(BookingSlot::getLocalDateTime).toList();

        return new ResponseEntity<>(availableTimes.stream()
                .map(dateTime -> dateTime.format(formatter)).toList(), HttpStatus.OK);
    }
}
