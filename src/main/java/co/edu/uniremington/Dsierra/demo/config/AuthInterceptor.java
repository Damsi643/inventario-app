package co.edu.uniremington.Dsierra.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String path = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            logger.warn("Petición {} a {} bloqueada - sesión no válida", method, path);
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
