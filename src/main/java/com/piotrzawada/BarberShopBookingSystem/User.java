package com.piotrzawada.BarberShopBookingSystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    String name;
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)" +
            "+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
    String email;

    @Size(min = 8,max = 25)
    @NotBlank
    String password;

    @JsonIgnore
    List<Booking> booking;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
