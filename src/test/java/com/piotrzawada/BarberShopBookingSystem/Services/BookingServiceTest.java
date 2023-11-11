package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Entities.Booking;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BookingRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepo bookingRepo;

    @InjectMocks
    BookingService bookingService;

    private AppUser appUser;

    private Booking booking, booking2, booking3, booking4;

    @BeforeEach
    public void init() {
        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();

        booking = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 12, 30))
                .appUser(appUser)
                .build();

        booking2 = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 9, 20, 13, 0))
                .build();

        booking3 = Booking.builder()
                .localDateTime( LocalDateTime.of(2026, 12, 11, 11, 0))
                .build();
        booking4 = Booking.builder()
                .localDateTime(LocalDateTime.of(2026, 10, 2, 13, 30))
                .price(20)
                .appUser(appUser)
                .build();

    }


    @Test
    void getByDataTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2026, 10, 2, 13, 30);

        when(bookingRepo.findByLocalDateTime((localDateTime))).thenReturn(booking4);

        Assertions.assertNotNull(bookingService.getByDataTime(localDateTime));
        Assertions.assertEquals(localDateTime, bookingService.getByDataTime(localDateTime).getLocalDateTime());
        Assertions.assertEquals(20, bookingService.getByDataTime(localDateTime).getPrice());
    }

    @Test
    void allBooked() {
        when(bookingRepo.findByAppUserIsNotNull()).thenReturn(List.of(booking, booking4));

        Assertions.assertEquals(2, bookingService.allBooked().size());
    }

    @Test
    void latestDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2026, 12, 11, 11, 0);

        when(bookingRepo.findLatestDateTime()).thenReturn(booking3.getLocalDateTime());

        Assertions.assertEquals(localDateTime, bookingService.latestDateTime());
    }

    @Test
    void availableByDate() {
        LocalDate localDate = LocalDate.of(2026, 9, 20);

        when(bookingService.availableByDate(localDate)).thenReturn(List.of(booking2));

        Assertions.assertEquals(1, bookingService.availableByDate(localDate).size());
    }
}