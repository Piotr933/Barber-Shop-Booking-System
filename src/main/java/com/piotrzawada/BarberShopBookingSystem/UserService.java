package com.piotrzawada.BarberShopBookingSystem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;


    public void registerUser(AppUser user) {
        userRepo.save(user);
    }

    public boolean userExist(String email) {
        Optional<AppUser> user = Optional.ofNullable(userRepo.getByEmail(email));
        return user.isPresent();
    }
}


