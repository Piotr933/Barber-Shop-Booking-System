package com.piotrzawada.BarberShopBookingSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * JPA Entity
 * @author Piotr Zawada
 * @version 1.1
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class BarberServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    String name;

    double price;
}
