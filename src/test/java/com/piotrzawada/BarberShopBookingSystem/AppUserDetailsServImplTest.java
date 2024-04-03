package com.piotrzawada.BarberShopBookingSystem;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Repositories.UserRepo;
import com.piotrzawada.BarberShopBookingSystem.Security.AppUserAdapter;
import com.piotrzawada.BarberShopBookingSystem.Security.AppUserDetailsServImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AppUserDetailsServImplTest {


    @InjectMocks
    AppUserDetailsServImpl appUserDetailsServImpl;

    @Mock
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_returnUserDetails() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setEmail("testEmail@email.com");
        appUser.setPassword("password");
        appUser.setRole("ROLE_USER");
        when(userRepo.findByEmail(anyString())).thenReturn(appUser);
        AppUserDetailsServImpl userDetailsService = new AppUserDetailsServImpl(userRepo);
        AppUserAdapter userDetails = new AppUserAdapter(appUser);
        assertNotNull(userDetailsService.loadUserByUsername("SampleEmail"));
    }
    @Test
    void whenAppUserNullShouldThrowUsernameNotFoundException () {
        when(userRepo.findByEmail(anyString())).thenReturn(null);
        AppUserDetailsServImpl userDetailsService = new AppUserDetailsServImpl(userRepo);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("AnyString)"));
    }
}