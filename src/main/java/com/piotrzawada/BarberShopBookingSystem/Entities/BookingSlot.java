package com.piotrzawada.BarberShopBookingSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * JPA Entity representing an BookingSlot (User or Admin)
 * @author Piotr Zawada
 * @version 1.3
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class BookingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;

    public String name;

    public LocalDateTime localDateTime;

    public double price;

    @JsonIgnore
    @ManyToOne
    public AppUser appUser;

    public BookingSlot(LocalDateTime localDateTime, double price) {
        this.localDateTime = localDateTime;
        this.price = price;
    }
}
