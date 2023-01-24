package com.piotrzawada.BarberShopBookingSystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    @JsonIgnore
    LocalDateTime localDateTime;
    double price;

    @JsonIgnore
    User user;

    public Booking(LocalDateTime localDateTime, double price) {
        this.localDateTime = localDateTime;
        this.price = price;
    }
}
