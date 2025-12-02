package com.smartshop.smartshop.config;

import com.smartshop.smartshop.entity.User;
import com.smartshop.smartshop.entity.enums.UserRole;
import com.smartshop.smartshop.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/logout") || path.startsWith("/public")){
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/api/auth/login");
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() == null) {
            response.sendRedirect("/api/auth/login");
            return false;
        }

        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }

        if (user.getRole() == UserRole.CLIENT) {
            String method = request.getMethod();

            if (!"GET".equalsIgnoreCase(method)) {
                throw new ForbiddenException("Access denied: insufficient permissions");
            }

            if (path.startsWith("/api/products") ||
                path.startsWith("/api/clients") ||
                path.startsWith("/api/orders")) {
                return true;
            }

            throw new ForbiddenException("Access denied: insufficient permissions");
        }

        throw new ForbiddenException("Access denied: unknown role");
    }
}
