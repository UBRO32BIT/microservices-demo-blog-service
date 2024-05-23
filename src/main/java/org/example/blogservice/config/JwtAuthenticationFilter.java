package org.example.blogservice.config;

import feign.FeignException;
import feign.RetryableException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.blogservice.dto.client.User;
import org.example.blogservice.service.client.CheckAuthRequest;
import org.example.blogservice.service.client.UserClient;
import org.example.blogservice.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserClient userClient;

    private static final Set<String> WHITELISTED_ENDPOINTS = Set.of(
            "/actuator/**"
    );

    @Override
//    @Retryable(retryFor = RetryableException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Check if the request URI matches any whitelisted pattern
        String requestURI = request.getRequestURI();
        if (isWhitelisted(requestURI)) {
            // Skip the filter logic and continue the filter chain
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtils.getJwtFromRequest(request);
        CheckAuthRequest checkAuthRequest = new CheckAuthRequest(token);
        User user = null;
        try {
            user = userClient.checkAuth(checkAuthRequest);
            request.setAttribute("userId", user.getId());
            filterChain.doFilter(request, response);
        }
        catch (Exception exception) {
            if (exception instanceof FeignException) {
                System.out.println(exception.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid Access Token");
            }
            else throw exception;
        }
    }

    private boolean isWhitelisted(String requestURI) {
        return WHITELISTED_ENDPOINTS.contains(requestURI);
    }
}
