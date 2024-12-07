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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/admin/register/*").permitAll();
                    auth.requestMatchers("/api/admin/addSlots").hasRole("ADMIN");
                    auth.requestMatchers("/api/admin/removeSlots").hasRole("ADMIN");
                    auth.requestMatchers("/api/admin/removeOneSlotBy").hasRole("ADMIN");
                    auth.requestMatchers("/api/admin/usersBookings").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/admin/cancelBooking").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/register").permitAll();
                    auth.requestMatchers("/api/bookings/availableTimes").permitAll();
                    auth.requestMatchers("/api/bookings/book").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/api/bookings/cancel").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/api/bookings/myBookings").hasRole("USER");
                    auth.requestMatchers("/api/services/all").permitAll();
                    auth.requestMatchers("/api/services/add").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/services/update").hasAnyRole("ADMIN");
                    auth.requestMatchers("/api/services/delete").hasAnyRole("ADMIN");
                    auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).hasAnyRole("ADMIN");
                    auth.anyRequest().denyAll();
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}

