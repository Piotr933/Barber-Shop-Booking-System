package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepo extends CrudRepository<Booking, Long> {
    List<Booking> findAllByAppUser(AppUser appUser);
}
