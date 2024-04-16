package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private AppUser appUser, appUser1, admin;


    @BeforeEach
    public void init() {
         appUser = AppUser.builder()
                 .nickname("Adam")
                .email("adam443243433@gmail.com")
                .role("ROLE_USER")
                .password("Password123#")
                .build();
         appUser1 = AppUser.builder()
                .nickname("Aoife")
                .role("ROLE_USER")
                .build();
         admin = AppUser.builder()
                .nickname("AdminBooking1")
                .role("ROLE_ADMIN")
                .build();
    }

    @Test
    void save_returnAppUser() {
        AppUser appUserSaved = userRepo.save(appUser);

        Assertions.assertNotNull(appUserSaved.getId());
        Assertions.assertTrue(appUserSaved.getId() > 0);
        Assertions.assertEquals("adam443243433@gmail.com", appUserSaved.getEmail());
        Assertions.assertEquals("Password123#", appUserSaved.getPassword());
    }

    @Test
    void findAll_returnListOfAppUsers() {
        userRepo.save(appUser);
        userRepo.save(appUser1);

        List<AppUser> repo = (List<AppUser>) userRepo.findAll();
        Assertions.assertEquals(2, repo.size());
    }

    @Test
    void  findById_returnAppUser() {
        userRepo.save(appUser);

        Long id = userRepo.findAll().iterator().next().getId();  //gets the user ID

        Optional<AppUser> returnedAppUser = userRepo.findById(id);
        Assertions.assertTrue(returnedAppUser.isPresent());
        returnedAppUser.ifPresent(user -> Assertions.assertEquals("Adam", user.nickname));
    }

    @Test
    void findByEmail_returnAppUser() {
        userRepo.save(appUser);

        Assertions.assertNotNull(userRepo.findByEmail("adam443243433@gmail.com"));
        Assertions.assertEquals("Adam", userRepo.findByEmail("adam443243433@gmail.com").getNickname());
    }
    @Test
    void findAllByRole_returnListOfAppUsers() {
        userRepo.save(appUser);
        userRepo.save(appUser1);
        userRepo.save(admin);

        List <AppUser> users = userRepo.findAllByRole("ROLE_USER");
        List <AppUser> admins = userRepo.findAllByRole("ROLE_ADMIN");

        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(1, admins.size());
        Assertions.assertEquals("Adam", users.get(0).getNickname());
        Assertions.assertEquals("Aoife", users.get(1).getNickname());
        Assertions.assertEquals("AdminBooking1", admins.get(0).getNickname());
    }
}