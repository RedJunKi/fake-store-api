package com.project.fake_store_api.security.provider;

import com.project.fake_store_api.security.util.JwtTokenizer;
import com.project.fake_store_api.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenizer jwtTokenizer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        Jws<Claims> claims;

        try {
            claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
        } catch (Exception e) {
            log.error("JWT 토큰 파싱 중 예외 발생: {}", e.getMessage());
            throw e;
        }

        String email = claims.getBody().getSubject();
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims.getBody());

        return new JwtAuthenticationToken(authorities, email, null);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles
                .forEach(r -> authorities.add(() -> r));

        return authorities;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
