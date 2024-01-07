package com.example.apigw.config;





import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GatewayConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("user-service", r -> r.path("/api/v1/users/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://user-service"))
//                .route("AUTHENTICATION-SERVICE", r -> r.path("/api/v1/auth/**")
//                        .uri("lb://authentication-service"))
//                .build();
//    }
// Used by Spring Security if CORS is enabled.
        @Bean
        public CorsFilter corsFilter() {

            UrlBasedCorsConfigurationSource source =
                    new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            //    config.setAllowCredentials(true);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }

}