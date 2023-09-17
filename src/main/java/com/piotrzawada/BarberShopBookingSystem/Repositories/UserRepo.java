package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * This interface defines the contract for accessing and managing AppUser entities
 * in the database. It extends the CrudRepository.
 *
 * @author Piotr Zawada
 * @version 0.3.0
 */
public interface UserRepo extends CrudRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    List<AppUser> findAllByRole(String role);
}
