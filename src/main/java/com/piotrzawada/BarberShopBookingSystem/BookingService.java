package com.piotrzawada.BarberShopBookingSystem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepo;

    public Optional<Booking> getByID(Long id) {
        return bookingRepo.findById(id);
    }

    public List<Booking> allBookings() {
        return (List<Booking>) bookingRepo.findAll();
    }

    public Booking getByDataTime(LocalDateTime localDateTime) {
        return bookingRepo.findByLocalDateTime(localDateTime);
    }
    public List<Booking> findAvailableTimeSlots() {
        return bookingRepo.findAllByAppUser(null);
    }

    public void saveBooking(Booking booking) {
        bookingRepo.save(booking);
    }
}
