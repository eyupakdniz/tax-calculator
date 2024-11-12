package com.eyup.challenge.service;

import com.eyup.challenge.dto.user.AuthenticationResponseDTO;
import com.eyup.challenge.dto.user.LoginRequestDTO;
import com.eyup.challenge.dto.user.RegisterRequestDTO;
import com.eyup.challenge.model.Token;
import com.eyup.challenge.model.User;
import com.eyup.challenge.repository.TokenRepository;
import com.eyup.challenge.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        // Testler için gerekli objeleri başlatıyoruz.
        registerRequest = new RegisterRequestDTO("John", "Doe", "johndoe", "password123", "USER");
        loginRequest = new LoginRequestDTO("johndoe", "password123");
    }

    // Test 1: Kullanıcı zaten varsa hata mesajı döner
    @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(new User()));

        // Act
        AuthenticationResponseDTO response = userService.register(registerRequest);

        // Assert
        assertNull(response.getAccessToken());
        assertNull(response.getRefreshToken());
        assertEquals("User already exist", response.getMessage());
    }

    // Test 2: Kullanıcı başarılı bir şekilde kayıt olur
    @Test
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // Act
        AuthenticationResponseDTO response = userService.register(registerRequest);

        // Assert
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("User registration was successful", response.getMessage());
    }

    // Test 3: Geçerli token ile kullanıcı alınır
    @Test
    void testGetCurrentUserFromJwt() {
        // Arrange
        String token = "validToken";
        User user = new User();
        user.setUsername("johndoe");
        when(jwtService.extractUsername(token)).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act
        User result = userService.getCurrentUserFromJwt(token);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    // Test 4: Token ile kullanıcı bulunamazsa hata fırlatılır
    @Test
    void testGetCurrentUserFromJwtUserNotFound() {
        // Arrange
        String token = "invalidToken";
        when(jwtService.extractUsername(token)).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.getCurrentUserFromJwt(token));
    }

    // Test 5: Başarılı giriş
    @Test
    void testAuthenticateSuccess() {
        // Arrange
        User user = new User();
        user.setUsername("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // Act
        AuthenticationResponseDTO response = userService.authenticate(loginRequest);

        // Assert
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("User login was successful", response.getMessage());
    }

    // Test 6: Geçersiz giriş (Yanlış kullanıcı adı veya şifre)
    @Test
    void testAuthenticateFailure() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.authenticate(loginRequest));
    }



    // Test 8: Kullanıcının geçerli token'ı yoksa işlem yapılmaz
    @Test
    void testRevokeAllTokenByUserNoTokens() {
        // Arrange
        User user = new User();
        when(tokenRepository.findAllAccessTokensByUser(user.getId())).thenReturn(List.of());

        // Act
        userService.revokeAllTokenByUser(user);

        // Assert
        verify(tokenRepository, times(0)).saveAll(anyList());
    }

    // Test 9: Geçerli refresh token ile yeni token oluşturulması
    @Test
    void testRefreshTokenSuccess() {
        // Arrange
        String refreshToken = "validRefreshToken";
        String username = "johndoe";
        User user = new User();
        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.isValidRefreshToken(refreshToken, user)).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        // Act
        ResponseEntity<AuthenticationResponseDTO> responseEntity = userService.refreshToken(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("New token generated", responseEntity.getBody().getMessage());
    }

    // Test 10: Geçersiz refresh token ile işlem yapılması
    @Test
    void testRefreshTokenFailure() {
        // Arrange
        String refreshToken = "invalidRefreshToken";
        String username = "johndoe";
        User user = new User();
        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.isValidRefreshToken(refreshToken, user)).thenReturn(false);

        // Act
        ResponseEntity<AuthenticationResponseDTO> responseEntity = userService.refreshToken(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
