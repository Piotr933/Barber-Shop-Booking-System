package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface defines the contract for accessing and managing Bookings Slots entities
 * in the database. It extends the CrudRepository.
 *
 * @author Piotr Zawada
 * @version 1.2
 */
public interface BookingRepo extends CrudRepository<Booking, Long> {

    /**
     * Retrieves a list of bookings associated with the given appUser
     * @param appUser
     * @return List of Bookings Slots
     */
    List<Booking> findAllByAppUser(AppUser appUser);

    /**
     * Retrieves a list of bookings with AppUser equal to null
     * @return List of Bookings Slots
     */
    List<Booking> findByAppUserIsNotNull();

    /**
     * Retrieves a Booking associated with the given LocalDataTime
     * @param localDateTime LocalDataTime
     * @return BookingSlot object
     */
    Booking findByLocalDateTime(LocalDateTime localDateTime);

    /**
     * Retrieves a list of bookings with no assigned app user and local date and time within a specified range.
     * This custom query is executed to find all bookings that meet the following criteria:
     * - The app user is not assigned (null).
     * - The local date and time of the booking fall within the specified range
     * @param startDateTime startDateTime The start of the local date and time range.
     * @param endDateTime endDateTime The end of the local date and time range.
     * @return list of bookings objects that meet the specified criteria.
     */
    List<Booking> findByAppUserNullAndLocalDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
