package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends CrudRepository<Booking, Long> {
    List<Booking> findAllByAppUser(AppUser appUser);

    List<Booking> findByAppUserIsNotNull();
    Booking findByLocalDateTime(LocalDateTime localDateTime);

    @Query("SELECT MAX(b.localDateTime) FROM Booking b")
    LocalDateTime findLatestDateTime();

    List<Booking> findByAppUserNullAndLocalDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
