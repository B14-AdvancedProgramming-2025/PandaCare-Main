package id.ac.ui.cs.advprog.b14.pandacare.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        
        // Define allowed origins
        String[] allowedOrigins = {
            "http://localhost:3000",
            "https://pandacare.netlify.app",
            "http://pandacare.netlify.app",
            "http://159.89.209.87:3003",
            "https://159.89.209.87:3003",
            "https://pandacare.abhipraya.dev"
        };

        // Check if origin is allowed
        boolean isAllowed = false;
        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    isAllowed = true;
                    break;
                }
            }
        }

        // Set CORS headers only if origin is allowed
        if (isAllowed) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
            response.setHeader("Access-Control-Max-Age", "3600");
        }

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
} 