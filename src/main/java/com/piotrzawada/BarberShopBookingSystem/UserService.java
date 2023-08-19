package com.piotrzawada.BarberShopBookingSystem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class offers a complete interface for User entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 0.3.0
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public void registerUser(AppUser user) {
        userRepo.save(user);
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


