package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class BookingSlotRepoTest {

    @Autowired
    private BookingSlotsRepo bookingSlotsRepo;

    @Autowired
    private UserRepo userRepo;

    private AppUser appUser;

    private BookingSlot bookingSlot, bookingSlot2, bookingSlot3, bookingSlot4;


    @BeforeEach
    public void init() {
        appUser = AppUser.builder()
                .nickname("Adam")
                .build();

        bookingSlot = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .price(20)
                .build();

        bookingSlot2 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 11, 23, 13, 0))
                .appUser(appUser)
                .price(20)
                .build();

        bookingSlot3 = BookingSlot.builder()
                .localDateTime( LocalDateTime.of(2026, 12, 11, 11, 0))
                .appUser(appUser)
                .build();

        bookingSlot4 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 10, 2, 13, 30))
                .build();

    }


    @Test
    void bookingRepo_save_returnBooking() {
        double price = 20;
        BookingSlot savedBookingSlot = bookingSlotsRepo.save(bookingSlot);

        Assertions.assertNotNull(savedBookingSlot.getId());
        Assertions.assertEquals(price, savedBookingSlot.price);
        Assertions.assertEquals(bookingSlot.getLocalDateTime(), savedBookingSlot.getLocalDateTime());
    }
    @Test
    void bookingRepo_findAll_returnAllBookings() {
        userRepo.save(appUser);
        bookingSlotsRepo.save(bookingSlot);
        bookingSlotsRepo.save(bookingSlot2);

        List<BookingSlot> bookingSlotList = (List<BookingSlot>) bookingSlotsRepo.findAll();

        Assertions.assertEquals(2, bookingSlotList.size());
        Assertions.assertEquals(bookingSlot.getLocalDateTime(), bookingSlotList.get(0).getLocalDateTime());
    }

    @Test
    void  bookingRepo_findById_returnBooking() {
        bookingSlotsRepo.save(bookingSlot);

        long id = bookingSlotsRepo.findAll().iterator().next().getId();
        Optional<BookingSlot> returnedBooking = bookingSlotsRepo.findById(id);

        Assertions.assertTrue(returnedBooking.isPresent());
        returnedBooking.ifPresent(user -> Assertions.assertEquals(bookingSlot.getLocalDateTime(), returnedBooking.get().getLocalDateTime()));
    }

    @Test
    void bookingRepo_findAllByAppUser_returnBookingsByAppUser() {
        userRepo.save(appUser);
        bookingSlotsRepo.save(bookingSlot);
        bookingSlotsRepo.save(bookingSlot2);
        bookingSlotsRepo.save(bookingSlot3);

        List<BookingSlot> returnedBookingSlots = bookingSlotsRepo.findAllByAppUser(appUser);

        Assertions.assertEquals(2, returnedBookingSlots.size());
        Assertions.assertEquals(appUser.getNickname(), returnedBookingSlots.get(0).appUser.getNickname());
    }

    @Test
    void bookingRepo_findByAppUserIsNotNull_returnListOfBookings() {
        userRepo.save(appUser);
        bookingSlotsRepo.save(bookingSlot);
        bookingSlotsRepo.save(bookingSlot2);
        bookingSlotsRepo.save(bookingSlot3);

        List<BookingSlot> returnedBookingSlots = bookingSlotsRepo.findByAppUserIsNotNull();

        Assertions.assertNotNull(returnedBookingSlots.get(0).getAppUser());
        Assertions.assertEquals(appUser, returnedBookingSlots.get(0).getAppUser());
    }

    @Test
    void bookingRepo_findByLocalDateTime_returnBooking() {
        userRepo.save(appUser);
        bookingSlotsRepo.save(bookingSlot);
        bookingSlotsRepo.save(bookingSlot2);
        bookingSlotsRepo.save(bookingSlot3);

        BookingSlot returnedBookingSlot = bookingSlotsRepo.findByLocalDateTime(bookingSlot3.getLocalDateTime());

        Assertions.assertEquals(bookingSlot3.getLocalDateTime(), returnedBookingSlot.getLocalDateTime());
    }

    @Test
    void bookingRepo_findByAppUserNullAndLocalDateTimeBetween_returnListOfBooking() {
        userRepo.save(appUser);
        bookingSlotsRepo.save(bookingSlot);
        bookingSlotsRepo.save(bookingSlot2);
        bookingSlotsRepo.save(bookingSlot3);
        bookingSlotsRepo.save(bookingSlot4);

        List<BookingSlot> returnedBookingSlots = bookingSlotsRepo.findByAppUserNullAndLocalDateTimeBetween
                (LocalDateTime.of(2026, 10, 1, 9, 0),
                 LocalDateTime.of(2026,12, 23, 8, 0));

        Assertions.assertEquals(1, returnedBookingSlots.size());
        Assertions.assertEquals(bookingSlot4.getLocalDateTime(), returnedBookingSlots.get(0).getLocalDateTime());
    }
}