package com.piotrzawada.BarberShopBookingSystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    @JsonIgnore
    LocalDateTime localDateTime;
    double price;

    @JsonIgnore
    @ManyToOne
    Userr userr;

    public Booking(LocalDateTime localDateTime, double price) {
        this.localDateTime = localDateTime;
        this.price = price;
    }
}
