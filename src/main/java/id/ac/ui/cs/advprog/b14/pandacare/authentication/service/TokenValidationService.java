package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TokenValidationService {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidationService.class);

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public TokenValidationService(TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Scheduled(fixedRateString = "${jwt.check_rate}")
    @Transactional
    public void validateTokens() {
        logger.info("Starting token validation at {}", new Date());

        tokenRepository.deleteExpiredTokens(new Date());

        List<Token> tokens = tokenRepository.findAll();

        for (Token token : tokens) {
            try {
                if (!jwtUtil.validateToken(token.getToken())) {
                    logger.info("Token has become invalid: {}", token.getToken().substring(0, 10) + "...");
                    tokenRepository.delete(token);
                }
            } catch (Exception e) {
                logger.error("Error validating token: {}", e.getMessage());
                tokenRepository.delete(token);
            }
        }

        logger.info("Token validation completed. Tokens count: {}", tokenRepository.count());
    }
}