package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class offers a complete interface for User entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public AppUser registerUser(AppUser user) {
        return userRepo.save(user);
    }

    public boolean userExist(String email) {
        Optional<AppUser> user = Optional.ofNullable(userRepo.findByEmail(email));

        return user.isPresent();
    }
    public AppUser getByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public List<AppUser> usersByRole(String role) {
        return userRepo.findAllByRole(role);
    }
}


