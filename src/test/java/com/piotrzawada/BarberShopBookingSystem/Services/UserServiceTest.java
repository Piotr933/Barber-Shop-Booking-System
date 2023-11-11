package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Repositories.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserService userService;

    private AppUser appUser;

    public void init() {
        appUser = AppUser.builder()
                .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();
    }



    @Test
    void userService_userExist_returnTrue() {
        when(userRepo.findByEmail(Mockito.contains("adam443243433@gmail.com"))).thenReturn(appUser);
        Assertions.assertTrue(userService.userExist("adam443243433@gmail.com"));
    }

    @Test
    void userService_getByEmail_returnAppUser() {
        when(userRepo.findByEmail(Mockito.contains("adam443243433@gmail.com"))).thenReturn(appUser);
        AppUser returnedAppUser = userService.getByEmail("adam443243433@gmail.com");
        Assertions.assertEquals(appUser, returnedAppUser);
    }

    @Test
    void userService_usersByRole_returnListOfUsers() {
        AppUser appUser1 = AppUser.builder()
                .nickname("Aoife")
                .role("ROLE_USER")
                .build();
        AppUser appUser2 = AppUser.builder()
                .nickname("Admin")
                .role("ROLE_ADMIN")
                .build();

        when(userRepo.findAllByRole(Mockito.contains("ROLE_USER"))).thenReturn(List.of(appUser, appUser1));
        when(userRepo.findAllByRole(Mockito.contains("ROLE_ADMIN"))).thenReturn(List.of(appUser2));

        Assertions.assertEquals(2, userService.usersByRole("ROLE_USER").size());
        Assertions.assertEquals(1, userService.usersByRole("ROLE_ADMIN").size());
        Assertions.assertEquals("Admin", userService.usersByRole("ROLE_ADMIN").get(0).getNickname());
    }
}