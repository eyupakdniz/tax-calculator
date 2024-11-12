package com.eyup.challenge.controller;

import com.eyup.challenge.dto.user.AuthenticationResponseDTO;
import com.eyup.challenge.dto.user.LoginRequestDTO;
import com.eyup.challenge.dto.user.RegisterRequestDTO;
import com.eyup.challenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private AuthenticationResponseDTO authenticationResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");

        loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        authenticationResponse = new AuthenticationResponseDTO();
        authenticationResponse.setAccessToken("mocked-jwt-token");
    }

    @Test
    void register_shouldReturnAuthenticationResponse() throws Exception {
        when(userService.register(any(RegisterRequestDTO.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void login_shouldReturnAuthenticationResponse() throws Exception {
        when(userService.authenticate(any(LoginRequestDTO.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

}
