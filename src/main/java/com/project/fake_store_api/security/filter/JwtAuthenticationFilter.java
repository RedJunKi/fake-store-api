package com.project.fake_store_api.security.filter;

import com.project.fake_store_api.security.exception.JwtExceptionCode;
import com.project.fake_store_api.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = "";

        try {
            token = getToken(request);
            if (StringUtils.hasText(token)) {
                getAuthentication(token);
            }
            filterChain.doFilter(request, response);
        } catch (NullPointerException | IllegalStateException e) {
            log.error("Not found Token // token : {}", token, e);
            handleException(request, JwtExceptionCode.NOT_FOUND_TOKEN);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid Token // token : {}", token, e);
            handleException(request, JwtExceptionCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("EXPIRED Token // token : {}", token, e);
            handleException(request, JwtExceptionCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Token // token : {}", token, e);
            handleException(request, JwtExceptionCode.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            log.error("====================================================");
            log.error("JwtFilter - doFilterInternal() 오류 발생");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {", e);
            log.error("====================================================");
            handleException(request, JwtExceptionCode.UNKNOWN_ERROR);
        }
    }

    private void handleException(HttpServletRequest request, JwtExceptionCode exceptionCode) {
        request.setAttribute("exception", exceptionCode.getCode());
        throw new BadCredentialsException("throw new " + exceptionCode.getMessage() + " exception");
    }

    private void getAuthentication(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            String[] arr = authorization.split(" ");
            return arr[1];
        }
        return null;
    }
}


