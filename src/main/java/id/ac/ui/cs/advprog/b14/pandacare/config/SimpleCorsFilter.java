package id.ac.ui.cs.advprog.b14.pandacare.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Handle preflight requests immediately
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            CorsResponseWrapper wrapper = new CorsResponseWrapper(response, request.getHeader("Origin"));
            wrapper.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Wrap response to control headers
        CorsResponseWrapper wrapper = new CorsResponseWrapper(response, request.getHeader("Origin"));
        chain.doFilter(req, wrapper);
    }

    private static class CorsResponseWrapper extends HttpServletResponseWrapper {
        private final String origin;
        private final Map<String, String> headers = new HashMap<>();

        public CorsResponseWrapper(HttpServletResponse response, String origin) {
            super(response);
            this.origin = origin;
            
            // Set CORS headers immediately
            if (origin != null && !origin.isEmpty()) {
                headers.put("Access-Control-Allow-Origin", origin);
            } else {
                headers.put("Access-Control-Allow-Origin", "*");
            }
            headers.put("Access-Control-Allow-Credentials", "true");
            headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            headers.put("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
            headers.put("Access-Control-Max-Age", "3600");
        }

        @Override
        public void setHeader(String name, String value) {
            if (name.startsWith("Access-Control-")) {
                // Ignore any other CORS headers, use only ours
                return;
            }
            headers.put(name, value);
            super.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            if (name.startsWith("Access-Control-")) {
                // Ignore any other CORS headers, use only ours
                return;
            }
            setHeader(name, value);
        }

        @Override
        public void flushBuffer() throws IOException {
            // Set our CORS headers right before flushing
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey().startsWith("Access-Control-")) {
                    super.setHeader(entry.getKey(), entry.getValue());
                }
            }
            super.flushBuffer();
        }
    }
} 