package com.piotrzawada.BarberShopBookingSystem;

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
 * @version 0.3.0
 */

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    PasswordEncoder encoder;

    /**
     * It is for register new app user
     * @param user AppUser object
     * @return ResponseEntity containing a response with the message
     */
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






