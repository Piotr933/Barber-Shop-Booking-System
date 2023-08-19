package com.piotrzawada.BarberShopBookingSystem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * The BookingService class offers a complete interface for Booking entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 0.3.0
 */
@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepo;

    /**
     * Retrieves Booking from the database by provided ID number.
     * @param id represents booking ID
     * @return Optional Booking object
     */
    public Optional<Booking> getByID(Long id) {
        return bookingRepo.findById(id);
    }

    /**
     * Retrieves a list of all bookings stored in the booking repository.
     * @return A list of Booking objects representing all bookings.
     */
    public List<Booking> allBookings() {
        return (List<Booking>) bookingRepo.findAll();
    }

    /**
     * Retrieves Booking from the database by provided date and time
     * @param localDateTime  Local Data Time
     * @return Booking object
     */

    public Booking getByDataTime(LocalDateTime localDateTime) {
        return bookingRepo.findByLocalDateTime(localDateTime);
    }

    /**
     * Retrieves list of available time slots.
     * @return List of Bookings object which appUser is equal null.
     */

    public List<Booking> findAvailableTimeSlots() {
        return bookingRepo.findAllByAppUser(null);
    }

    /**
     * Saves provided booking in the database.
     * @param booking Booking object
     */
    public void saveBooking(Booking booking) {
        bookingRepo.save(booking);
    }

    /**
     * Retrieves All reserved Bookings from the database
     * @return List of Bookings objects when appUser is not equal to null.
     */
    public List<Booking> allBooked() {
        return bookingRepo.findByAppUserIsNotNull();
    }

    /**
     * It returns the latest Booking data time of bookings in the database. This method play important role when admin
     * want to add new empty booking slots.It helps to avoid overwriting data in the database.
     * @return Data and Time of last booking in the database.
     */
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
        LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return bookingRepo.findByAppUserNullAndLocalDateTimeBetween(startDateTime, endDateTime);
    }
}
