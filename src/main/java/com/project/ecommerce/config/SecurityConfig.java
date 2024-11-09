package com.project.ecommerce.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors() // Enable CORS support
                .and().csrf().disable() // Disable CSRF for stateless authentication
                .authorizeHttpRequests()
                // Public endpoints accessible to everyone
                .requestMatchers("/auth/new", "/auth/authenticate", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**",
                        "/swagger-ui.html")
                .permitAll()

                // Admin-only endpoints for Product Management
                .requestMatchers("/updateproduct/**", // Update product by ID
                        "/deleteproduct/**", // Delete product by ID
                        "/products" // Get all products
                ).hasRole("ADMIN") // Only users with ADMIN role can access these endpoints

                // Endpoints that require authentication (can be accessed by any authenticated
                // user)

                // All other requests must be authenticated
                .anyRequest().permitAll()

                .and().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint) // Custom entry
                // point for
                // unauthorized
                // access

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless
                // authentication

                .and().authenticationProvider(authenticationProvider()) // Custom authentication provider

                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add custom authentication
                // filter

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
