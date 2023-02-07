package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    BookingService bookingService;

    @Autowired
    UserService userService;

    @Autowired
    public BookingController( BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PutMapping("/book")
    public  ResponseEntity<?> bookVisit(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam String localDateTime) {

        AppUser appUser = userService.getByEmail(userDetails.getUsername());
        Booking booking = bookingService.getByDataTime(LocalDateTime.parse(localDateTime));
        if (booking == null) {
            return new ResponseEntity<>("There is not Booking available at this data time", HttpStatus.BAD_REQUEST);
        }

        if (booking.getAppUser() == null) {
            booking.setAppUser(appUser);
            bookingService.saveBooking(booking);
            return new ResponseEntity<>("Your visit has been booked", HttpStatus.OK);
        }

        return new ResponseEntity<>("This time is already booked", HttpStatus.BAD_REQUEST);
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
