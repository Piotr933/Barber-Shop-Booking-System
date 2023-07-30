package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<AppUser, Long> {
   // AppUser getByEmail(String email);
    AppUser findByEmail(String email);
    List<AppUser> findAllByRole(String role);
}
