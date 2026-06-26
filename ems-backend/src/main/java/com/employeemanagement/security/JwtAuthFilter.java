package com.employeemanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT AUTH FILTER
 * ---------------
 * Intercepts every HTTP request and validates the JWT token.
 * If valid, allows the request through.
 * If invalid/missing, blocks the request (for protected routes).
 *
 * Runs ONCE per request (OncePerRequestFilter).
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header: "Bearer eyJhbGc..."
        String authHeader = request.getHeader("Authorization");

        // If no token, just continue (public routes will be allowed by SecurityConfig)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // Validate token
        if (jwtUtil.validateToken(token)) {
            // Token is valid - store user info in request for controllers to use
            request.setAttribute("userEmail", jwtUtil.extractEmail(token));
            request.setAttribute("userRole", jwtUtil.extractRole(token));
            request.setAttribute("userId", jwtUtil.extractUserId(token));
        }

        filterChain.doFilter(request, response);
    }
}