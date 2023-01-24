package com.piotrzawada.BarberShopBookingSystem;

import org.springframework.data.repository.CrudRepository;

public interface BookingRepo extends CrudRepository<Booking, Long> {
}
