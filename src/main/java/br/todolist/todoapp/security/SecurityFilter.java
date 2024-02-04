package br.todolist.todoapp.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.todolist.todoapp.Constants;
import br.todolist.todoapp.security.services.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (Constants.WHITE_LIST_URIS.contains(uri)) {
            filterChain.doFilter(request, response);

            return;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            sendAccessDeniedResponse(response);

            return;
        }

        Cookie sessionCookie = Arrays.asList(cookies).stream().filter(c -> c.getName().equals("session"))
                .findFirst().orElse(null);

        if (sessionCookie == null) {
            sendAccessDeniedResponse(response);

            return;
        }

        var tokenPayload = jwtUtils.decodeToken(sessionCookie.getValue());
        var user = jwtUtils.decodePayloadToMap(tokenPayload);

        var authentication = new UsernamePasswordAuthenticationToken(user.get("email"), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendAccessDeniedResponse(HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper body = new ObjectMapper();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body.writeValueAsString(Map.of("message",
                "Access denied")));
    }
}
