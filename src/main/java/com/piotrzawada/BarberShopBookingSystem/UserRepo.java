package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<AppUser, Long> {
    AppUser getByEmail(String username);
}
