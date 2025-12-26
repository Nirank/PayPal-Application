package com.paypal.api_gateway.filter;


import com.paypal.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/signup",
            "/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();

        // Allow public APIs
        if (PUBLIC_PATHS.contains(path)) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.validateToken(token);

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-User-Email", claims.getSubject())
                            .header("X-User-Id", String.valueOf(claims.get("userId")))
                            .header("X-User-Role", (String) claims.get("role"))
                            .build())
                    .build();

            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

//
//@Component
//public class JwtAuthFilter implements GlobalFilter, Ordered {
//
//    private static final List<String> PUBLIC_PATHS = List.of(
//            "/auth/signup",
//            "/auth/login"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().value();
//        String normalizedPath = path.replaceAll("/+$", "");
//
//        System.out.println("Incoming path: " + path);
//        System.out.println("Normalized path: " + normalizedPath);
//
//        // Skip JWT check for public paths
//        if (PUBLIC_PATHS.contains(normalizedPath)) {
//            System.out.println("Skipping JWT for public path: " + normalizedPath);
//            return chain.filter(exchange)
//                    .doOnSubscribe(s -> System.out.println("Proceeding without JWT for public path"))
//                    .doOnSuccess(v -> System.out.println("Successfully passed public path without JWT"))
//                    .doOnError(e -> System.err.println("Error during public path filter chain: " + e.getMessage()));
//        }
//
//        // Extract Authorization header once
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        System.out.println("Authorization header found: " + true);
//
//        if ( authHeader == null ||  !authHeader.startsWith("Bearer ")) {
//            System.err.println("Missing or invalid Authorization header");
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        try {
////            String token = authHeader.substring(7);
////            System.out.println("JWT token extracted from header: " + token);
////
////            // DEBUG: replace <your_expected_token_here> with actual token for debugging only
////            String expectedToken = "<your_expected_token_here>";
////            System.out.println("Comparing tokens:");
////            System.out.println("Expected token: " + expectedToken);
////            System.out.println("Extracted token: " + token);
////
////            Claims claims = JwtUtil.validateToken(token);
////            System.out.println("JWT validated successfully. Claims subject: " + claims.getSubject());
////
////            // Add user email to headers for downstream services
////            Long userId = claims.get("userId", Long.class);
////            exchange.getRequest().mutate()
////                    .header("X-User-Email", claims.getSubject())
////                    .header("X-User-Id", userId.toString())
////                    .header("X-User-Role", claims.get("role",String.class))
////
////                    .build();
////            System.out.println("Added X-User-Email header to request: " + claims.getSubject());
////
////            return chain.filter(exchange)
////                    .doOnSubscribe(s -> System.out.println("Proceeding with JWT authenticated request"))
////                    .doOnSuccess(v -> System.out.println("Successfully passed JWT auth filter"))
////                    .doOnError(e -> System.err.println("Error during authenticated filter chain: " + e.getMessage()));
//
//            String token = authHeader.substring(7);
//            Claims claims = JwtUtil.validateToken(token);
//
//            System.out.println("Token validated. Claims:");
//            System.out.println("   userId=" + claims.get("userId"));
//            System.out.println("   email=" + claims.getSubject());
//            System.out.println("   role=" + claims.get("role"));
//
//            // Mutate request with claims
//            ServerWebExchange mutatedExchange = exchange.mutate()
//                    .request(exchange.getRequest().mutate()
//                            .header("X-User-Email", claims.getSubject())
//                            .header("X-User-Id", String.valueOf(claims.get("userId")))
//                            .header("X-User-Role", (String) claims.get("role"))
//                            .build())
//                    .build();
//
//            return chain.filter(mutatedExchange);
//        } catch (Exception e) {
//            System.err.println("JWT validation failed: " + e.getMessage());
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}