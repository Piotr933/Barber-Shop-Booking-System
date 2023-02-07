package com.piotrzawada.BarberShopBookingSystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;
    String nickname;
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)" +
            "+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
    String email;

    @Column(length = 1024)
    @NotBlank
    String password;

    @JsonIgnore
    String role;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    List<Booking> booking;

    public AppUser(String name, String email) {
        this.nickname = name;
        this.email = email;
    }
}