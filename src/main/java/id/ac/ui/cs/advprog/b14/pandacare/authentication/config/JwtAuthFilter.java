package id.ac.ui.cs.advprog.b14.pandacare.authentication.config;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, TokenRepository tokenRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Handle OPTIONS preflight requests
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        logger.debug("Auth header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenString = authHeader.substring(7);

        try {
            if (jwtUtil.validateToken(tokenString)) {
                Token token = tokenRepository.findByToken(tokenString);

                if (token != null && !token.isExpired()) {
                    String email = jwtUtil.getEmailFromToken(tokenString);
                    String role = jwtUtil.getRoleFromToken(tokenString);
                    User user = userRepository.findByEmail(email);

                    if (user != null) {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user, null, authorities
                        );

                        logger.debug("Setting authentication for user: {}", email);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        logger.warn("User not found for email: {}", email);
                    }
                } else {
                    logger.warn("Token not found in database or expired: {}", tokenString);
                }
            } else {
                logger.warn("Invalid JWT token: {}", tokenString);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}