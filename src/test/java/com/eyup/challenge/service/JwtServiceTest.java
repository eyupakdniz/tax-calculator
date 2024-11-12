package com.eyup.challenge.service;

import com.eyup.challenge.model.User;
import com.eyup.challenge.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtService jwtService;

    private User user;
    private String validAccessToken;
    private String validRefreshToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");

        // Simulate a valid token (You may use a real library to generate a valid token here)
        validAccessToken = jwtService.generateAccessToken(user);
        validRefreshToken = jwtService.generateRefreshToken(user);

        // Simulate an invalid token
        invalidToken = "invalidToken";
    }

    // Test: Access token doğrulama
    @Test
    void testIsValidAccessToken() {
        // Arrange
        when(tokenRepository.findByAccessToken(validAccessToken)).thenReturn(java.util.Optional.ofNullable(null)); // Simulate valid token in repository

        // Act
        boolean isValid = jwtService.isValid(validAccessToken, user);

        // Assert
        assertTrue(isValid);
    }

    // Test: Refresh token doğrulama
    @Test
    void testIsValidRefreshToken() {
        // Arrange
        when(tokenRepository.findByRefreshToken(validRefreshToken)).thenReturn(java.util.Optional.ofNullable(null)); // Simulate valid refresh token in repository

        // Act
        boolean isValid = jwtService.isValidRefreshToken(validRefreshToken, user);

        // Assert
        assertTrue(isValid);
    }

    // Test: Token geçersiz
    @Test
    void testIsInvalidToken() {
        // Act
        boolean isValid = jwtService.isValid(invalidToken, user);

        // Assert
        assertFalse(isValid);
    }



    // Test: Access token ve refresh token doğru şekilde oluşturulması
    @Test
    void testGenerateAccessToken() {
        // Act
        String token = jwtService.generateAccessToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(token.contains("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"));  // part of a valid token
    }

    @Test
    void testGenerateRefreshToken() {
        // Act
        String token = jwtService.generateRefreshToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(token.contains("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"));  // part of a valid token
    }

    // Test: Token'dan kullanıcı adı ekstraksiyonu
    @Test
    void testExtractUsernameFromToken() {
        // Act
        String username = jwtService.extractUsername(validAccessToken);

        // Assert
        assertEquals("testuser", username);
    }


}
