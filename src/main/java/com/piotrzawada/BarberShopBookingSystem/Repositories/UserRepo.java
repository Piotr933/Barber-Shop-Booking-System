package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * This interface defines the contract for accessing and managing AppUser entities
 * in the database. It extends the CrudRepository.
 *
 * @author Piotr Zawada
 * @version 1.0.2
 */
public interface UserRepo extends CrudRepository<AppUser, Long> {

    /**
     * Retrieves a AppUser associated with the given email
     * @param email - String value represents the AppUser email address
     * @return AppUser
     */
    AppUser findByEmail(String email);

    /**
     * Retrieves List of App Users by given role (Admin,User);
     * @param role
     * @return
     */
    List<AppUser> findAllByRole(String role);
}
