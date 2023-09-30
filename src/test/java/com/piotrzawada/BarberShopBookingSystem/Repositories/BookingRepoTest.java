package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookingRepoTest {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepo userRepo;

    private final AppUser appUser = AppUser.builder()
            .nickname("Adam")
            .build();

    private final Booking booking = Booking.builder()
            .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
            .price(20)
            .build();

    private final Booking booking2 = Booking.builder()
            .localDateTime(LocalDateTime.of(2026, 11, 23, 13, 0))
            .appUser(appUser)
            .price(20)
            .build();

    private final Booking booking3 = Booking.builder()
            .localDateTime( LocalDateTime.of(2026, 12, 11, 11, 0))
            .appUser(appUser)
            .build();
    private final Booking booking4 = Booking.builder()
            .localDateTime(LocalDateTime.of(2026, 10, 2, 13, 30))
            .build();


    @Test
    void bookingRepo_save_returnBooking() {
        double price = 20;
        Booking savedBooking = bookingRepo.save(booking);

        Assertions.assertNotNull(savedBooking.getId());
        Assertions.assertEquals(price, savedBooking.price);
        Assertions.assertEquals(booking.getLocalDateTime(), savedBooking.getLocalDateTime());
    }
    @Test
    void bookingRepo_findAll_returnAllBookings() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);

        List<Booking> bookingList = (List<Booking>) bookingRepo.findAll();

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals(booking.getLocalDateTime(), bookingList.get(0).getLocalDateTime());
    }

    @Test
    void  bookingRepo_findById_returnBooking() {
        bookingRepo.save(booking);

        long id = bookingRepo.findAll().iterator().next().getId();
        Optional<Booking> returnedBooking = bookingRepo.findById(id);

        Assertions.assertTrue(returnedBooking.isPresent());
        returnedBooking.ifPresent(user -> Assertions.assertEquals(booking.getLocalDateTime(), returnedBooking.get().getLocalDateTime()));
    }

    @Test
    void bookingRepo_findAllByAppUser_returnBookingsByAppUser() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);
        bookingRepo.save(booking3);

        List<Booking> returnedBookings = bookingRepo.findAllByAppUser(appUser);

        Assertions.assertEquals(2, returnedBookings.size());
        Assertions.assertEquals(appUser.getNickname(), returnedBookings.get(0).appUser.getNickname());
    }

    @Test
    void bookingRepo_findByAppUserIsNotNull_returnListOfBookings() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);
        bookingRepo.save(booking3);

        List<Booking> returnedBookings =bookingRepo.findByAppUserIsNotNull();

        Assertions.assertNotNull(returnedBookings.get(0).getAppUser());
        Assertions.assertEquals(appUser, returnedBookings.get(0).getAppUser());
    }

    @Test
    void bookingRepo_findByLocalDateTime_returnBooking() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);
        bookingRepo.save(booking3);

        Booking returnedBooking = bookingRepo.findByLocalDateTime(booking3.getLocalDateTime());

        Assertions.assertEquals(booking3.getLocalDateTime(), returnedBooking.getLocalDateTime());
    }

    @Test
    void bookingRepo_findLatestDateTime_returnLocalDataTime() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);
        bookingRepo.save(booking3);

        LocalDateTime localDateTime = bookingRepo.findLatestDateTime();
        Assertions.assertEquals(booking3.getLocalDateTime(), localDateTime);
    }

    @Test
    void bookingRepo_findByAppUserNullAndLocalDateTimeBetween_returnListOfBooking() {
        userRepo.save(appUser);
        bookingRepo.save(booking);
        bookingRepo.save(booking2);
        bookingRepo.save(booking3);
        bookingRepo.save(booking4);

        List<Booking> returnedBookings = bookingRepo.findByAppUserNullAndLocalDateTimeBetween
                (LocalDateTime.of(2026, 10, 1, 9, 0),
                 LocalDateTime.of(2026,12, 23, 8, 0));

        Assertions.assertEquals(1, returnedBookings.size());
        Assertions.assertEquals(booking4.getLocalDateTime(), returnedBookings.get(0).getLocalDateTime());
    }
}