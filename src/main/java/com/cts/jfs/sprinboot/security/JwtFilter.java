package com.cts.jfs.sprinboot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Read header name from application.properties
    @Value("${jwt.header}")
    private String headerName;

    // Read token prefix from application.properties
    @Value("${jwt.prefix}")
    private String tokenPrefix;

    // ---------------------------------------------------
    // This method runs ONCE per request
    // ---------------------------------------------------
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get Authorization header from request
        final String authHeader = request.getHeader(headerName);

        String username = null;
        String jwtToken = null;

        // Step 2: Check if header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith(tokenPrefix + " ")) {

            // Step 3: Extract token by removing "Bearer " prefix
            jwtToken = authHeader.substring(7);

            try {
                // Step 4: Extract username from token
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                logger.warn("JWT Token extraction failed: " + e.getMessage());
            }
        }

        // Step 5: If username found and no authentication exists yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load user details from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Step 7: Validate token
            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                // Step 8: Create authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Step 9: Set request details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 10: Set authentication in Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 11: Pass request to next filter in chain
        filterChain.doFilter(request, response);
    }

    // ---------------------------------------------------
    // Skip JWT filter for public endpoints
    // ---------------------------------------------------
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/")         // login and register
                || path.startsWith("/swagger-ui") // swagger UI
                || path.startsWith("/api-docs")   // openapi docs
                || path.startsWith("/h2-console"); // H2 console (dev only)
    }
}
