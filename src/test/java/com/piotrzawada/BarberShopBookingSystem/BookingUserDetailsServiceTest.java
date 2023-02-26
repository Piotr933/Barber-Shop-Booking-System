package com.piotrzawada.BarberShopBookingSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BookingUserDetailsServiceTest {


    @InjectMocks
    BookingUserDetailsService bookingUserDetailsService;

    @Mock
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenPassEmailAddressShouldReturnUserDetails() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setEmail("testEmail@email.com");
        appUser.setPassword("password");
        appUser.setRole("ROLE_USER");
        when(userRepo.findByEmail(anyString())).thenReturn(appUser);
        BookingUserDetailsService userDetailsService = new BookingUserDetailsService(userRepo);
        UserDetailsImpl userDetails = new UserDetailsImpl(appUser);
        assertNotNull(userDetailsService.loadUserByUsername("SampleEmail"));
    }
    @Test
    void whenAppUserNullShouldThrowUsernameNotFoundException () {
        when(userRepo.findByEmail(anyString())).thenReturn(null);
        BookingUserDetailsService userDetailsService = new BookingUserDetailsService(userRepo);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("AnyString)"));
    }
}