package com.cts.jfs.sprinboot.security;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    // ---------------------------------------------------
    // PUBLIC ENDPOINTS - No token required
    // ---------------------------------------------------
    private static final String[] PUBLIC_URLS = {
            "/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/h2-console/**"
    };

    // ---------------------------------------------------
    // SECURITY FILTER CHAIN
    // ---------------------------------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(PUBLIC_URLS).permitAll()

                // Admin only
                .requestMatchers("/users/**").hasAnyRole("Admin")

                // Product Manager + Admin
                .requestMatchers("/products/**", "/milestones/**")
                    .hasAnyRole("ProductManager", "Admin", "ReleaseManager","QAEngineer","DevLead")

                // Dev Lead + Product Manager + Admin
                .requestMatchers("/backlog/**", "/sprints/**")
                    .hasAnyRole("DevLead", "ProductManager", "Admin","ReleaseManager","QAEngineer")

                // Release Manager + Admin
                .requestMatchers("/releases/**", "/environments/**")
                    .hasAnyRole("ReleaseManager","DevLead","QAEngineer", "Admin","ProductManager")

                // QA Engineer + Release Manager + Admin
                .requestMatchers("/qa/**")
                    .hasAnyRole("QAEngineer", "ReleaseManager", "Admin")

                // Release Manager + Product Manager + Admin
                .requestMatchers("/releasenotes/**")
                    .hasAnyRole("ReleaseManager", "ProductManager", "Admin","CustomerAdmin")
                    
                .requestMatchers("/gates/**")
                    .hasAnyRole("ReleaseManager", "QAEngineer", "Admin")

                // All authenticated users
                .requestMatchers("/notifications/**").authenticated()

                // Audit logs - Admin only
                .requestMatchers("/audit/**").hasRole("Admin")
                

                .anyRequest().authenticated()
            )

            // Stateless - no HTTP session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Set authentication provider
            .authenticationProvider(authenticationProvider())

            // Add JWT filter before default auth filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ---------------------------------------------------
    // PASSWORD ENCODER
    // ---------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ---------------------------------------------------
    // AUTHENTICATION PROVIDER
    // Spring Security 7.x - pass UserDetailsService via constructor
    // ---------------------------------------------------
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Boot 4.x / Spring Security 7.x:
        // DaoAuthenticationProvider now takes UserDetailsService in constructor
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ---------------------------------------------------
    // AUTHENTICATION MANAGER
    // ---------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ---------------------------------------------------
    // CORS - Allow React (port 3000) + Spring Boot (port 2026)
    // ---------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:2026"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}