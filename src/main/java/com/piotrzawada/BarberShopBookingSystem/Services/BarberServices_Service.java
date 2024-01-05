package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BarberServicesRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BarberServices_Service {

    private final BarberServicesRepo repo;

    public BarberServices save(BarberServices service) {
        return repo.save(service);
    }
    public void deleteByName(String name) {
        repo.deleteById(repo.findByName(name).getId());
    }
    public BarberServices getByName(String name) {
        return  repo.findByName(name);
    }

    public List<BarberServices> getAllServices() {
        return repo.findAll();
    }
}
