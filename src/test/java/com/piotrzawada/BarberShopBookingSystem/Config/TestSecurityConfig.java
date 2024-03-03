package com.piotrzawada.BarberShopBookingSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configureTest(HttpSecurity httpSecurity) throws Exception {
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
}

