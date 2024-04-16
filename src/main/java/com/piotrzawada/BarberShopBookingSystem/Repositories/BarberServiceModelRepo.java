package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
/**
 * This interface defines the contract for accessing and managing Barber Services
 * in the database. It extends the CrudRepository.
 *
 * @author Piotr Zawada
 * @version 1.2
 */
public interface BarberServiceModelRepo extends CrudRepository<BarberServiceModel, Long> {

    /**
     * Retrieve BarberService by the given Barber Service name
     * @param name name
     * @return barberServiceModel
     */
    BarberServiceModel findByName(String name);

    /**
     * Retrieves all BarberService objects
     * @return List of BarberService objects
     */
    List<BarberServiceModel> findAll();
}
