package com.piotrzawada.BarberShopBookingSystem.Security;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import com.piotrzawada.BarberShopBookingSystem.Repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Spring Security UserDetailsService interface, responsible for loading user details based on
 * their username (email)
 * @author Piotr Zawada
 * @version 1.2
 */
@Service
@AllArgsConstructor
public class AppUserDetailsServImpl implements UserDetailsService {

    private final UserRepo userRepo;

    /**
     * Loads user details from the user repository based on the provided username (email)
     *
     * @param username username The username (email) of the user for whom details are to be loaded.
     * @return  An instance of UserDetails
     * @throws UsernameNotFoundException  If no user is found with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new AppUserAdapter(user);
    }
}

