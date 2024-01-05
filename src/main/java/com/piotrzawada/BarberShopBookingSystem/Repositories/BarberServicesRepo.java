package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface BarberServicesRepo extends CrudRepository<BarberServices, Long> {
    BarberServices findByName(String name);
    List<BarberServices> findAll();
}
