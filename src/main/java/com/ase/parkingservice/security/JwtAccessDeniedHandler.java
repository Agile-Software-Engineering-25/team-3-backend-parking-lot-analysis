package com.ase.parkingservice.security;

import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: You don't have permission to access this resource");
    }
}
