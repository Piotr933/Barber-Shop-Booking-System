package com.piotrzawada.BarberShopBookingSystem.Services;

import com.piotrzawada.BarberShopBookingSystem.Entities.BarberServices;
import com.piotrzawada.BarberShopBookingSystem.Repositories.BarberServicesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarberServicesServiceTest {
    @Mock
    private BarberServicesRepo repo;
    @InjectMocks
    private BarberServices_Service service;

    private BarberServices standard, beardTrim;

    @BeforeEach
    public void init(){
        standard = BarberServices.builder()
                .name("Standard Haircut")
                .price(20.00)
                .build();
        beardTrim = BarberServices.builder()
                .name("Beard Trim")
                .price(15.99)
                .build();
    }
    @Test
    void service_save_returnBarberServices() {
        when(repo.save(standard)).thenReturn(standard);
        Assertions.assertEquals(service.save(standard), standard);
    }
    @Test
    void service_getByName_returnBarberService() {
        when(repo.findByName(beardTrim.getName())).thenReturn(beardTrim);
        Assertions.assertEquals(beardTrim, service.getByName("Beard Trim"));
    }

    @Test
    void service_getAllServices_returnListOfBarberServices() {
        when(repo.findAll()).thenReturn(List.of(standard,beardTrim));
        Assertions.assertEquals(2, service.getAllServices().size());
    }
}