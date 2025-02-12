package in.ongrid.fitnesstracker.interceptor;

import in.ongrid.fitnesstracker.interceptor.JwtInterceptor;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true;
        }
        // Get the token from the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if the token is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return false;
        }

        // Extract the token
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        try {
            // Validate the token
            jwtUtil.parseToken(token);
            return true; // Allow the request to proceed
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false; // Deny the request
        }
    }
}