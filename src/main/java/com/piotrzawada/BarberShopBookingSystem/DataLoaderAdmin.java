package com.piotrzawada.BarberShopBookingSystem;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Loads the initial admin user into the database if no admin users are present.
 *
 * This method is designed for the admin to change the initial admin credentials.
 *
 * @author Piotr Zawada
 * @version 2.0
 */

@Component
@AllArgsConstructor
public class DataLoaderAdmin implements CommandLineRunner {


    UserService userService;

    PasswordEncoder encoder;

    /**
     * Checks for existing admin users and creates a default admin if none are found.
     *
     */

    @Override
    public void run(String... args) throws Exception {
        if (userService.usersByRole("ROLE_ADMIN").size() == 0) {
            AppUser appUser = AppUser.builder()
                    .nickname("admin")
                    .email("admin@admin.com")
                    .password(encoder.encode("admin"))
                    .role("ROLE_ADMIN")
                    .build();
            userService.registerUser(appUser);
        }
    }
}
