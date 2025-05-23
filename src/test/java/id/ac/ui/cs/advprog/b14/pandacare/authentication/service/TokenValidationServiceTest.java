package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenValidationServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenValidationService tokenValidationService;

    private Token validToken;
    private Token invalidToken;
    private Token exceptionToken;
    private List<Token> tokenList;

    @BeforeEach
    void setUp() {
        validToken = new Token("valid.jwt.token", new Date(System.currentTimeMillis() + 3600000));
        invalidToken = new Token("invalid.jwt.token", new Date(System.currentTimeMillis() + 3600000));
        exceptionToken = new Token("exception.jwt.token", new Date(System.currentTimeMillis() + 3600000));

        tokenList = Arrays.asList(validToken, invalidToken, exceptionToken);
    }

    @Test
    void validateTokens_DeletesExpiredTokens() {
        when(tokenRepository.findAll()).thenReturn(List.of());

        tokenValidationService.validateTokens();

        verify(tokenRepository).deleteExpiredTokens(any(Date.class));
    }

    @Test
    void validateTokens_KeepsValidTokens() {
        when(tokenRepository.findAll()).thenReturn(List.of(validToken));
        when(jwtUtil.validateToken(validToken.getToken())).thenReturn(true);

        tokenValidationService.validateTokens();

        verify(tokenRepository, never()).delete(validToken);
    }

    @Test
    void validateTokens_DeletesInvalidTokens() {
        when(tokenRepository.findAll()).thenReturn(List.of(invalidToken));
        when(jwtUtil.validateToken(invalidToken.getToken())).thenReturn(false);

        tokenValidationService.validateTokens();

        verify(tokenRepository).delete(invalidToken);
    }

    @Test
    void validateTokens_DeletesTokensWithValidationException() {
        when(tokenRepository.findAll()).thenReturn(List.of(exceptionToken));
        when(jwtUtil.validateToken(exceptionToken.getToken())).thenThrow(new RuntimeException("Token validation failed"));

        tokenValidationService.validateTokens();

        verify(tokenRepository).delete(exceptionToken);
    }

    @Test
    void validateTokens_ProcessesMultipleTokens() {
        when(tokenRepository.findAll()).thenReturn(tokenList);
        when(jwtUtil.validateToken(validToken.getToken())).thenReturn(true);
        when(jwtUtil.validateToken(invalidToken.getToken())).thenReturn(false);
        when(jwtUtil.validateToken(exceptionToken.getToken())).thenThrow(new RuntimeException("Token validation failed"));

        tokenValidationService.validateTokens();

        verify(tokenRepository, never()).delete(validToken);

        verify(tokenRepository).delete(invalidToken);
        verify(tokenRepository).delete(exceptionToken);
    }

    @Test
    void validateTokens_HandlesEmptyTokenList() {
        when(tokenRepository.findAll()).thenReturn(List.of());

        tokenValidationService.validateTokens();

        verify(tokenRepository).findAll();
        verify(tokenRepository, never()).delete(any(Token.class));
    }
}