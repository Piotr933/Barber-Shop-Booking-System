package com.piotrzawada.BarberShopBookingSystem.Controllers;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Dto.Response;
import com.piotrzawada.BarberShopBookingSystem.Services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * Rest Controller for handles REST requests related to the App User.
 *
 * @author Piotr Zawada
 * @version 1.3
 */
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    PasswordEncoder encoder;

    /**
     * Register new App user
     * @param user user details
     * @return ResponseEntity (message, Http Status)
     */
    @PostMapping("/api/register")
    public ResponseEntity<Response> register(@RequestBody @Valid AppUser user) {
        Response response = new Response();

        if (userService.userExist(user.email)) {
            response.setMessage("A user with that username already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        user.setRole("ROLE_USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userService.registerUser(user);
        response.setMessage("User has been successfully registered");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}






