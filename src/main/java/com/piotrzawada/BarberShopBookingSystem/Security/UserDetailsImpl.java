package com.piotrzawada.BarberShopBookingSystem.Security;

import com.piotrzawada.BarberShopBookingSystem.Entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
/**
 * Implementation of the Spring Security UserDetails interface, providing user detail for authentication and
 * authorization purposes.
 * @author Piotr Zawada
 * @version 0.3.0
 */

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;

    public UserDetailsImpl (AppUser appUser) {
        username = appUser.getEmail();
        password = appUser.getPassword();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(appUser.getRole()));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 4 remaining methods that just return true

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
