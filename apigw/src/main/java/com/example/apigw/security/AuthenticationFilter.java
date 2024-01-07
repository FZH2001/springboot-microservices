package com.example.apigw.security;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.List;
import java.util.function.Predicate;

import com.example.apigw.exception.JwtTokenMalformedException;
import com.example.apigw.exception.JwtTokenMissingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;


import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouterValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            if (validator.isSecured.test(exchange.getRequest())) {
//                //header contains token or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                }
////                try {
////                    //REST call to AUTH service
////                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
//                    jwtUtil.validateToken(authHeader);
//
////                } catch (Exception e) {
////                    System.out.println("invalid access...!");
////                    throw new RuntimeException("un authorized access to application");
////                }
//            }
//            return chain.filter(exchange);
//        });
//    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return handleAuthorizationError(exchange, "missing authorization header",HttpStatusCode.valueOf(400));
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    // REST call to AUTH service
                    RestTemplate template = new RestTemplate();
                    ResponseEntity<String> response = template.exchange(

                            "http://localhost:8081/api/v1/auth/validate?token=" + authHeader,
                            HttpMethod.GET,
                            null,
                            String.class);

                    if (response.getStatusCode() != HttpStatus.OK) {

                            return handleAuthorizationError(exchange, "unauthorized access to application",HttpStatus.UNAUTHORIZED);


                    }

                    // Validate the token locally using your jwtUtil
                    jwtUtil.validateToken(authHeader);

                } catch (RestClientException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e)  {
                    // Handle REST call failure
                    return handleAuthorizationError(exchange, "Invalid token",HttpStatus.UNAUTHORIZED);
                }
                catch (ExpiredJwtException e){
                    return handleAuthorizationError(exchange, "Token expired",HttpStatusCode.valueOf(401));

                }
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleAuthorizationError(ServerWebExchange exchange, String errorMessage,HttpStatusCode code) {
        exchange.getResponse().setStatusCode(code);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String errorResponse = "{\"error\": \"" + errorMessage + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {

    }
}