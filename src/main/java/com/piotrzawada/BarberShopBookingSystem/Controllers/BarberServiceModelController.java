package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServiceModel_Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for handles REST requests related to the Barber Services.
 *
 * @author Piotr Zawada
 * @version 1.1
 */

@RestController
@RequestMapping("/api/services")
@AllArgsConstructor
public class BarberServiceModelController {

    BarberServiceModel_Service service;

    /**
     * This method is for display all services providing by the Barber Shop
     * @return Response Entity (List of BarberServices objects, Http Status)
     */
    @GetMapping("/all")
    public ResponseEntity<?> allServices() {
        return new ResponseEntity<>(service.getAllServices(), HttpStatus.OK);
    }

    /**
     * Add new Barber Service that the Barber Shop will be providing
     * @param name  service name
     * @param price price of the barber service
     * @return Response Entity(message, Http Status)
     */
    @PostMapping("/add")
    public ResponseEntity<Response> addService(String name, double price) {
        Response response = new Response();
        BarberServiceModel barberService = BarberServiceModel.builder()
                .name(name)
                .price(price)
                .build();

        service.save(barberService);
        response.setMessage("New Barber service has been successfully added");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Edit the price of the Barber Service
     * @param name the name of the service to edit
     * @param newPrice new price
     * @return Response Entity(message, Http Status)
     */
    @PutMapping("/update")
    public ResponseEntity<Response> editPrice(String name, double newPrice) {
        Response response = new Response();

        BarberServiceModel barberService =  service.getByName(name);
        barberService.setPrice(newPrice);

        service.save(barberService);
        response.setMessage("The Price of this service has been successfully updated.");

       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * Delete the barber service
     * @param name name of the service
     * @return Response Entity with message and HttpStatus
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(String name) {
        Response response = new Response();

        service.deleteByName(name);

        response.setMessage("This Service has been successfully deleted");

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
