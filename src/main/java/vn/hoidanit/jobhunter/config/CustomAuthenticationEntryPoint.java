package vn.hoidanit.jobhunter.config;

import java.io.IOException;import ava.import 
org.springframework.http.Himport rg.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.RestResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;
    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;

    }
<<<<<    // et error
} 
    @Overri

    response.setContentType("application/json;charset=UTF-8");
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
// <<<<<<< HEAD
// ====

    ==
        String errorMessage = Optional.ofNullable(authException.getCause())
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());
// >>>>>>> master
        res.setError(authException.getCause().getMessage());
res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
String errorMessage = Optional.ofNullable(authException.getCause())
        .map(Throwable::getMessage)
        .orElse(authException.getMessage());
res.setError(errorMessage);
