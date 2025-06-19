package crip.gateway.com.pe.beans;

import crip.gateway.com.pe.filters.AuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GatewayBeans {

    private final AuthFilter authFilter;

    public GatewayBeans(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    @Profile("eureka-on")
    public RouteLocator routeLocatorEurekaOn(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("persona-ms", route -> route
                        .path("/persona-ms/**")
                        .uri("lb://persona-ms"))

                .route("oferta-ms", route -> route
                        .path("/oferta-ms/**")
                        .uri("lb://oferta-ms"))

                .route("practica-ms", route -> route
                        .path("/practica-ms/**")
                        .uri("lb://practica-ms"))

                .route("report-ms", route -> route
                        .path("/report-ms/**")
                        .uri("lb://report-ms"))

                .build();
    }

    @Bean
    @Profile("eureka-off")
    public RouteLocator routeLocatorEurekaOff(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        .path("/persona-ms/**")
                        .uri("http://localhost:8081"))
                .route(route -> route
                        .path("/oferta-ms/**")
                        .uri("http://localhost:8082"))
                .route(route -> route
                        .path("/practica-ms/**")
                        .uri("http://localhost:8083"))
                .route(route -> route
                        .path("/report-ms/**")
                        .uri("http://localhost:7070"))
                .build();
    }
}
