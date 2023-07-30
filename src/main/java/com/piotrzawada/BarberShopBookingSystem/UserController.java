package com.piotrzawada.BarberShopBookingSystem;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<Response> register(@RequestBody @Valid AppUser user) {
        Response response = new Response();

        if (userService.userExist(user.email)) {
            response.setMessage("User with that username already exists.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        user.setRole("ROLE_USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userService.registerUser(user);
        response.setMessage("User has been registered");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}






