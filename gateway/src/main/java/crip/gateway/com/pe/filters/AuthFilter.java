package crip.gateway.com.pe.filters;

import crip.gateway.com.pe.dtos.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter {

    private final WebClient webClient;

    private static final String AUTH_VALIDATE_URI = "http://ms-auth:3030/auth-server/auth/jwt"; // IMPORTANTE: URI del validador
    private static final String ACCESS_TOKEN_HEADER_NAME = "accessToken";

    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Rutas públicas (sin autenticación)
        if (path.contains("/auth-server/auth/login") || path.contains("/auth-server/auth/register")) {
            return chain.filter(exchange);
        }

        // Verifica que exista el header Authorization
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return this.onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        final String[] chunks = tokenHeader.split(" ");

        // Verifica el formato del token: "Bearer <token>"
        if (chunks.length != 2 || !chunks[0].equalsIgnoreCase("Bearer")) {
            return this.onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String token = chunks[1];

        // Llama al validador de token del auth-server
        return this.webClient
                .post()
                .uri(AUTH_VALIDATE_URI)
                .header(ACCESS_TOKEN_HEADER_NAME, token)
                .retrieve()
                .bodyToMono(TokenDto.class)
                .map(response -> {
                    // Aquí podrías guardar en atributos del request datos como ID o rol si lo necesitas después
                    return exchange;
                })
                .flatMap(chain::filter)
                .onErrorResume(ex -> this.onError(exchange, HttpStatus.UNAUTHORIZED)); // Captura errores como 401
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}
