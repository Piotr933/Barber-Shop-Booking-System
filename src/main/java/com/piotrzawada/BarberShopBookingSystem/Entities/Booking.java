package com.piotrzawada.BarberShopBookingSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * JPA Entity
 * @author Piotr Zawada
 * @version 1.0.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;

    public LocalDateTime localDateTime;
    public double price;

    @JsonIgnore
    @ManyToOne
    public AppUser appUser;

    public Booking(LocalDateTime localDateTime, double price) {
        this.localDateTime = localDateTime;
        this.price = price;
    }
}
