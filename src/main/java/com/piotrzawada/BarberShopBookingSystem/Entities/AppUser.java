package com.piotrzawada.BarberShopBookingSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * JPA Entity
 * @author Piotr Zawada
 * @version 1.2
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;
    public String nickname;
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)" +
            "+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message = "Invalid Email Address")
    public String email;

    @Column(length = 1024)
    @Pattern(regexp ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Invalid password. " +
            "Please ensure it includes at least one uppercase letter, one lowercase letter, one digit, one special" +
            " character (#?!@$%^&*-), and is at least eight characters long.")
    public String password;

    @JsonIgnore
    public String role;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    public List<Booking> booking;

    public AppUser(String name, String email) {
        this.nickname = name;
        this.email = email;
    }
}
