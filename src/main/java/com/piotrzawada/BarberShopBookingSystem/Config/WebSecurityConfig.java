package com.piotrzawada.BarberShopBookingSystem.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Web Security Configuration
 * @author Piotr Zawada
 * @version 1.0.2
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * User Authentication
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(getEncoder());
        return authProvider;
    }

    /**
     * Sets up security rules using the HttpSecurity object.
     * @param httpSecurity . It allows configuring web based security for specific http requests
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/test/testForAll").permitAll();
                    auth.requestMatchers("/test/testAdmin").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/bookings/book").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/api/bookings/cancel").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/api/bookings/myBookings").hasRole("USER");
                    auth.requestMatchers("/api/admin/add").hasRole("ADMIN");
                    auth.requestMatchers("/api/admin/usersBookings").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/admin/cancelBooking").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/services/all").permitAll();
                    auth.requestMatchers("/api/services/add").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/services/update").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/services/delete").hasAnyRole("ADMIN");
                    auth.requestMatchers("/").permitAll();
                    auth.anyRequest().permitAll();
                })
                .headers().frameOptions().disable()
                .and()
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Password Encoder
     * @return Password encoder that it takes care of generating and password hashes using the BCrypt algorithm.
     */
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}