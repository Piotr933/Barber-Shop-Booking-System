package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
/**
 * This interface defines the contract for accessing and managing Barber Services
 * in the database. It extends the CrudRepository.
 *
 * @author Piotr Zawada
 * @version 1.1
 */
public interface BarberServicesRepo extends CrudRepository<BarberServices, Long> {

    /**
     * Retrieve BarberService by the given Barber Service name
     * @param name name of the Barber Service
     * @return
     */
    BarberServices findByName(String name);

    /**
     * Retrieves all BarberService objects
     * @return List of BarberService objects
     */
    List<BarberServices> findAll();
}
