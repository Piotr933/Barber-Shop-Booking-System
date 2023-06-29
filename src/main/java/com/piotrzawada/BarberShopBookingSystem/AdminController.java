package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/add")
    public ResponseEntity<?> addEmptyBookingSlots(@RequestParam int day, @RequestParam int month) {
        LocalDateTime localDateTime = LocalDateTime.of(2023, month, day, 9, 0);


        do {
            Booking booking = new Booking(localDateTime, 20);
            bookingService.saveBooking(booking);
            localDateTime = localDateTime.plusMinutes(30);

        } while (localDateTime.getHour() != 18);

        return new ResponseEntity<>("ALL GOOD", HttpStatus.OK);
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
}
