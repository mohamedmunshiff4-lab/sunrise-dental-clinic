package com.sunrise.dental.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor enforces the "only authorised staff" rule: every page except
 * /login and static resources requires an active session.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String path = request.getRequestURI();
                if (path.equals("/") || path.equals("/login") || path.startsWith("/css")
                        || path.startsWith("/h2-console") || path.startsWith("/api/auth")) {
                    return true;
                }
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("staff") == null) {
                    response.sendRedirect("/login");
                    return false;
                }
                return true;
            }
        });
    }
}
