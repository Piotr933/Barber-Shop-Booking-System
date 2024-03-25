package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BookingRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * The BookingService class offers a complete interface for Booking entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 1.1
 */
@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepo;

    /**
     * Retrieves Booking slots from the database by provided date and time
     * @param localDateTime  Local Data Time
     * @return Booking object
     */

    public Booking getByDataTime(LocalDateTime localDateTime) {
        return bookingRepo.findByLocalDateTime(localDateTime);
    }

    /**
     * Saves provided booking in the database.
     * @param booking Booking object
     */
    public Booking saveBooking(Booking booking) {
        return bookingRepo.save(booking);
    }

    /** It removes Booking Slot from the database
     * @param booking Booking Slot
     */

    public void removeBooking(Booking booking) {
        bookingRepo.delete(booking);
    }

    /**
     * Retrieves All reserved Bookings from the database
     * @return List of BookingsSlots  objects when appUser is not equal to null.
     */
    public List<Booking> allBooked() {
        return bookingRepo.findByAppUserIsNotNull();
    }

    public LocalDateTime latestDateTime() {
        Optional<LocalDateTime> localDateTimeOptional = Optional.ofNullable(bookingRepo.findLatestDateTime());
        if (localDateTimeOptional.isEmpty()) {
            return LocalDateTime.now();
        }
        return bookingRepo.findLatestDateTime();
    }
    /**
     * Retrieves all available to reserve Bookings from the database by provided date.
     * @param localDate Local Date Time
     * @return List of bookings by specified days with AppUser equal to null.
     */
    public List<Booking> availableByDate (LocalDate localDate) {
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(23, 59,00);

        return bookingRepo.findByAppUserNullAndLocalDateTimeBetween(startDateTime, endDateTime);
    }
}
