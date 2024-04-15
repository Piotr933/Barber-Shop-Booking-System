package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.BookingSlot;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BookingSlotsRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingSlotServiceTest {

    @Mock
    BookingSlotsRepo bookingSlotsRepo;

    @InjectMocks
    BookingSlotsService bookingSlotsService;

    private AppUser appUser;

    private BookingSlot bookingSlot, bookingSlot2, bookingSlot3, bookingSlot4;

    @BeforeEach
    public void init() {
        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();

        bookingSlot = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .appUser(appUser)
                .build();

        bookingSlot2 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 13, 0))
                .build();

        bookingSlot3 = BookingSlot.builder()
                .localDateTime( LocalDateTime.of(2026, 12, 11, 11, 0))
                .build();
        bookingSlot4 = BookingSlot.builder()
                .localDateTime(LocalDateTime.of(2026, 10, 2, 13, 30))
                .price(20)
                .appUser(appUser)
                .build();

    }


    @Test
    void getByDataTime_returnBookingSlot() {
        LocalDateTime localDateTime = LocalDateTime.of(2026, 10, 2, 13, 30);

        when(bookingSlotsRepo.findByLocalDateTime((localDateTime))).thenReturn(bookingSlot4);

        Assertions.assertNotNull(bookingSlotsService.getByDataTime(localDateTime));
        Assertions.assertEquals(localDateTime, bookingSlotsService.getByDataTime(localDateTime).getLocalDateTime());
        Assertions.assertEquals(20, bookingSlotsService.getByDataTime(localDateTime).getPrice());
    }

    @Test
    void allBooked_returnListOfBookedSlots() {
        when(bookingSlotsRepo.findByAppUserIsNotNull()).thenReturn(List.of(bookingSlot, bookingSlot4));

        Assertions.assertEquals(2, bookingSlotsService.allBooked().size());
    }

    @Test
    void availableByDate_returnListOfBookingSlots_returnListOfBookingSlots() {
        LocalDate localDate = LocalDate.of(2026, 9, 20);


        when(bookingSlotsRepo.findByAppUserNullAndLocalDateTimeBetween(ArgumentMatchers.any(),ArgumentMatchers.any())).
                thenReturn(List.of(bookingSlot2));

        Assertions.assertEquals(1, bookingSlotsService.availableByDate(localDate).size());
        Assertions.assertNull(bookingSlotsService.availableByDate(localDate).get(0).getAppUser());
    }
}