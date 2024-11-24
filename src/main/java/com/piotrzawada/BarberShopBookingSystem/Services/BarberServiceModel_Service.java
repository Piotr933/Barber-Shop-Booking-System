package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BarberServiceModelRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The class offers a complete interface for Barber Services entity management, handling CRUD operations
 * @author Piotr Zawada
 * @version 1.3
 */

@Service
@AllArgsConstructor
public class BarberServiceModel_Service {

    private final BarberServiceModelRepo repo;

    /**
     * Saves provided BarberService in the database.
     * @param service Barber Service Object
     * @return BarberService Object
     */
    public BarberServiceModel save(BarberServiceModel service) {
        return repo.save(service);
    }

    /**
     * Remove Barber Service from the database
     * @param name name of The Barber Service to remove
     */
    public void deleteByName(String name) {
        repo.deleteById(repo.findByName(name).getId());
    }

    /**
     * Retrieves BarberService Object from the database by provided name.
     * @param name name of the Barber Service object
     * @return Barber Service object
     */
    public BarberServiceModel getByName(String name) {
        return  repo.findByName(name);
    }

    /**
     * Retrieves List of the all BarberServices object from the database.
     * @return List of Barber Services objects
     */
    public List<BarberServiceModel> getAllServices() {
        return repo.findAll();
    }
}
