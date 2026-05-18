package com.blp.auth.controller;

import com.blp.common.result.ApiResult;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Tag(name = "认证接口")
@RestController
@RequestMapping("/auth")
public class TokenController {

    private final AuthenticationManager authenticationManager;
    private RSAKey rsaKey;

    public TokenController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostConstruct
    public void init() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();
        this.rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.blp.auth.mapper.UserMapper userMapper;

    @Operation(summary = "登录获取Token")
    @PostMapping("/login")
    public ApiResult<Map<String, Object>> login(@RequestBody LoginRequest request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String accessToken = generateToken(authentication, 30 * 60);
        String refreshToken = generateToken(authentication, 7 * 24 * 60 * 60);

        return ApiResult.ok(Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken,
                "token_type", "Bearer",
                "expires_in", 1800
        ));
    }

    private String generateToken(Authentication authentication, int expiresInSeconds) throws Exception {
        Instant now = Instant.now();
        String[] authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toArray(String[]::new);

        com.blp.auth.model.SecurityUser su = userMapper.selectByUsername(authentication.getName());
        Long userId = su != null ? su.getId() : 1L;

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(authentication.getName())
                .issuer("blp-auth")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(expiresInSeconds)))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", userId)
                .claim("authorities", authorities)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).keyID(rsaKey.getKeyID()).build(),
                claims);
        signedJWT.sign(new RSASSASigner(rsaKey));
        return signedJWT.serialize();
    }

    public record LoginRequest(String username, String password) {}
}
