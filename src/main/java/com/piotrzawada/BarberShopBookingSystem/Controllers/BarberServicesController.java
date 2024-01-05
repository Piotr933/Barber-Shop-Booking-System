package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import com.piotrzawada.BarberShopBookingSystem.Services.BarberServices_Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@AllArgsConstructor
public class BarberServicesController {

    BarberServices_Service service;

    @GetMapping("/all")
    public ResponseEntity<?> allServices() {
        return new ResponseEntity<>(service.getAllServices(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addService(String name, double price) {
        Response response = new Response();
        BarberServices barberService = BarberServices.builder()
                .name(name)
                .price(price)
                .build();

        service.save(barberService);
        response.setMessage("New Barber service has been successfully added");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> editPrice(String name, double newPrice) {
        Response response = new Response();

        BarberServices barberService =  service.getByName(name);
        barberService.setPrice(newPrice);

        service.save(barberService);
        response.setMessage("The Price of this service has been successfully updated.");

       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(String name) {
        Response response = new Response();

        service.deleteByName(name);

        response.setMessage("This Service has been successfully deleted");

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
