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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rest Controller for handles REST requests related to the Admins.
 *
 * @author Piotr Zawada
 * @version 1.1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    BookingService bookingService;

    UserService userService;

    PasswordEncoder encoder;

    /**
     * This method is for register Admin. If AppUser object meet all required criteria, the new admin will be registered.
     *
     * @param user - Admin details
     * @return Response Entity(message, Http Status)
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
     * This method is to for adding new available slots for booking.The admin need to provide the
     * date, business hours, and duration of the visit
     *
     * @param localDate - date of new booking slots
     * @param open - represent time that shop will open
     * @param close - represent time that shop will close
     * @param duration - The duration of each appointment.
     * @return Response Entity with message with quantity of added now booking slots and Http status
     */

    @PostMapping("/addSlots")
    public ResponseEntity<Response> addSlotsByDay(String localDate, String open, String close, int duration) {

        LocalDate date = LocalDate.parse(localDate);
        LocalDateTime startDay = date.atTime(LocalTime.parse(open));
        LocalDateTime closing = date.atTime(LocalTime.parse(close));
        LocalDateTime slotTime = startDay;
        int count = 0;

        while (!slotTime.isAfter(closing)) {
            Booking booking = new Booking(slotTime, 0);
            if (bookingService.getByDataTime(booking.localDateTime) == null) {
                bookingService.saveBooking(booking);
                count++;
            }
            slotTime = slotTime.plusMinutes(duration);
        }

        return new ResponseEntity<Response>(new Response(count + " booking slots added on " + date ),
                HttpStatus.CREATED);
    }

    /**
     * This method is for removing all booking slots by provided date.
     * @param localDate - local date
     * @return Response Entity with message, quantity of deleted booking slots and HttpStatus
     */

    @DeleteMapping("/removeSlots")
    public ResponseEntity<Response> removeSlotsByDay(String localDate) {
        List<Booking> bookings = bookingService.availableByDate(LocalDate.parse(localDate));
        for (Booking booking : bookings) {
            bookingService.removeBooking(booking);
        }
        return new ResponseEntity<Response>(new Response(bookings.size() + " removed  slots on  " + localDate),
                HttpStatus.OK);
    }

    /**
     * This method is for removing single booking slot by provided date and time
     * @param localDateTime - local date and time
     * @return Response Entity with message and HttpStatus
     */

    @DeleteMapping("/removeOneSlotBy")
    public ResponseEntity<Response> removeSingleSlot(String localDateTime) {
        Booking booking = bookingService.getByDataTime(LocalDateTime.parse(localDateTime));
        if (booking != null) {
            bookingService.removeBooking(booking);
            return new ResponseEntity<>(new Response("The booking slot has been removed"), HttpStatus.OK);

        }
        return new ResponseEntity<Response>(new Response("There is no Booking Slot at this data and time"), HttpStatus.CONFLICT);
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
     * @param ldt - date and time
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
