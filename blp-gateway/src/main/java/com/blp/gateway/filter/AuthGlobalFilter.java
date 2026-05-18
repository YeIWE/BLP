package com.blp.gateway.filter;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Set;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Set<String> ALLOWED_PATHS = Set.of(
        "/auth/login",
        "/auth/oauth2/jwks",
        "/product/",
        "/review/",
        "/search/",
        "/promotion/seckill/list",
        "/v3/api-docs",
        "/swagger-ui",
        "/doc.html",
        "/webjars"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (ALLOWED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            String token = authHeader.substring(7);
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (exp != null && exp.before(new Date())) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            Long userId = signedJWT.getJWTClaimsSet().getLongClaim("userId");
            String username = signedJWT.getJWTClaimsSet().getSubject();
            ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header("X-User-Id", String.valueOf(userId != null ? userId : 1L))
                .header("X-Username", username != null ? username : "admin")
                .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
