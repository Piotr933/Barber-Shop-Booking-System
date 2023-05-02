package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    BookingService bookingService;

    @Autowired
    public AdminController(BookingService bookingService) {
        this.bookingService = bookingService;
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
