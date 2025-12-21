package com.paypal.user_service.utils;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JWTRequestFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        //1. from client request take Auth Header and get JWT token and fetch username
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try{
                username = jwtUtils.extractUsername(jwt);
            }catch (Exception e){
                log.info("Error while extracting UserName");
            }
        }

        // from SecurityContext check for authentication
        //1. Validate JWT --> and set authentication token

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //username = null not authenticated
            if (jwtUtils.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if (jwt == null || jwt.isBlank()) {
                filterChain.doFilter(request, response);
                return; // skip processing if token empty
            }
            try {
                username = jwtUtils.extractUsername(jwt);
                // only extract role if JWT is valid and present
                String role = jwtUtils.extractRole(jwt);
                // use role for authorities as needed
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);


                filterChain.doFilter(request, response);
            } catch (Exception e) {
                // log error if you want
                log.info("Error while extracting userName & Role ");
            }
        } else {
            filterChain.doFilter(request, response);
            return;
        }

    }
}
