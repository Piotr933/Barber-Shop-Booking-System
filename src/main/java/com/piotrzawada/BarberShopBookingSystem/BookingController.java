package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    BookingService bookingService;

    public BookingController(@Autowired BookingService bookingService) {
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


    @GetMapping("/availableTimes")
    public ResponseEntity <?> getAvailableSlots() {
        return  new ResponseEntity<>(bookingService.findAvailableTimeSlots(), HttpStatus.OK);
    }
}
