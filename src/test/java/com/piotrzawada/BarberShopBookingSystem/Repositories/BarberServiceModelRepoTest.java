package com.piotrzawada.BarberShopBookingSystem.Repositories;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BarberServiceModelRepoTest {

    @Autowired
    BarberServiceModelRepo repo;


    private BarberServiceModel standard, beardTrim, returnedService, returnedService2;


    @BeforeEach
    public void init() {
        standard = BarberServiceModel.builder()
                .name("Standard Haircut")
                .price(20.00)
                .build();
        beardTrim = BarberServiceModel.builder()
                .name("Beard Trim")
                .price(15.99)
                .build();

        returnedService =  repo.save(standard);
        returnedService2 = repo.save(beardTrim);

    }
    @Test
    void repo_save_returnBarberService() {
        Assertions.assertNotNull(returnedService);
        Assertions.assertNotNull(returnedService2);
        Assertions.assertTrue(returnedService.getId() > 0);
        Assertions.assertEquals("Standard Haircut", returnedService.getName());
        Assertions.assertEquals(20.00, returnedService.getPrice());
    }

    @Test
    void repo_save_returnListOfServices() {
        List<BarberServiceModel> list = repo.findAll();

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(15.99, list.get(1).getPrice());
    }

    @Test
    void repo_findByName_returnBarberService() {
        BarberServiceModel service = repo.findByName("Standard Haircut");

        Assertions.assertNotNull(service);
        Assertions.assertEquals(20.00, service.getPrice());
    }
}