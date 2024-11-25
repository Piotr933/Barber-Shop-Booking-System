package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BookingSlotsRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * The BookingService class offers a complete interface for Booking entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 1.3
 */
@Service
@AllArgsConstructor
public class BookingSlotsService {

    private final BookingSlotsRepo bookingSlotsRepo;

    /**
     * Retrieves Booking slots from the database by provided date and time
     * @param localDateTime  Local Data Time
     * @return Booking object
     */

    public BookingSlot getByDataTime(LocalDateTime localDateTime) {
        return bookingSlotsRepo.findByLocalDateTime(localDateTime);
    }

    /**
     * Saves provided booking in the database.
     * @param bookingSlot Booking object
     */
    public BookingSlot saveBooking(BookingSlot bookingSlot) {
        return bookingSlotsRepo.save(bookingSlot);
    }

    /** It removes Booking Slot from the database
     * @param bookingSlot Booking Slot
     */

    public void removeBooking(BookingSlot bookingSlot) {
        bookingSlotsRepo.delete(bookingSlot);
    }

    /**
     * Retrieves All reserved Bookings from the database
     * @return List of BookingsSlots  objects when appUser is not equal to null.
     */
    public List<BookingSlot> allBooked() {
        return bookingSlotsRepo.findByAppUserIsNotNull();
    }

    /**
     * Retrieves all available to reserve Bookings from the database by provided date.
     * @param localDate Local Date Time
     * @return List of bookings by specified days with AppUser equal to null.
     */
    public List<BookingSlot> availableByDate (LocalDate localDate) {
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(23, 59,00);

        return bookingSlotsRepo.findByAppUserNullAndLocalDateTimeBetween(startDateTime, endDateTime);
    }
}
