package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    BookingService bookingService;
    UserService userService;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    public AdminController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

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
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        user.setRole("ROLE_ADMIN");
        user.setPassword(encoder.encode(user.getPassword()));
        userService.registerUser(user);
        response.setMessage("Admin successfully registered");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
        response.setMessage("Booking Slots has been updated: " + updatedSlots);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/usersBookings")
    public ResponseEntity<?> userBookings(@AuthenticationPrincipal UserDetails userDetails) {
        List<Booking> bookings = bookingService.allBooked();
        HashMap<String, String> bookingsMap= new HashMap<>();

        for (Booking booking : bookings) {
            bookingsMap.put(booking.localDateTime.toString(), booking.appUser.email);
        }

        return new ResponseEntity<>(bookingsMap, HttpStatus.OK);
    }
    @PutMapping ("/cancelBooking")
    public ResponseEntity<Response> cancelBookingByDataTime (@RequestParam String ldt) {
        LocalDateTime localDateTime = LocalDateTime.parse(ldt);
        Booking booking = bookingService.getByDataTime(localDateTime);
        booking.setAppUser(null);
        bookingService.saveBooking(booking);
        Response response = new Response();
        response.setMessage("Booking Cancelled");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
