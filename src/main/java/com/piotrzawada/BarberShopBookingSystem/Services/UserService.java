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
 * @version 1.3
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    /**
     * Saves new AppUser into database
     * @param user
     * @return
     */

    public AppUser registerUser(AppUser user) {
        return userRepo.save(user);
    }

    /**
     * Verify whether a user with the provided email address already exists in the database.
     * @param email email address
     * @return
     */

    public boolean userExist(String email) {
        Optional<AppUser> user = Optional.ofNullable(userRepo.findByEmail(email));

        return user.isPresent();
    }

    /**
     * Retrieves App user by provided email address
     * @param email email address
     * @return
     */
    public AppUser getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    /**
     * Retrieves App user by provided role
     * @param role - user role (ex. ROLE_ADMIN)
     * @return list of Application users
     */
    public List<AppUser> usersByRole(String role) {
        return userRepo.findAllByRole(role);
    }
}


