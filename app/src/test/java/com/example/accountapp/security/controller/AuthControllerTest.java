package com.example.accountapp.security.controller;

import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.repository.UserRepository;
import com.example.accountapp.security.service.UserServiceImpl;
import com.example.accountapp.common.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
            "testuser",
            "password123",
            "test@example.com",
            "1234567890",
            Status.Pending,
            "John",
            "Doe",
            1L,
            LocalDate.of(1990, 1, 1)
        );

        authRequest = new AuthRequest("testuser", "password123");
    }

    @Test
    void register_WhenValidData_ShouldReturnCreated() throws Exception {
        // Given
        when(userService.registerUser(any(RegisterRequest.class))).thenReturn("User registered!");

        // When & Then
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
    }

    @Test
    void register_WhenServiceThrowsException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("Registration failed"));

        // When & Then
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error creating user: Registration failed"));

        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
    }

    @Test
    void login_WhenValidCredentials_ShouldReturnToken() throws Exception {
        // Given
        when(userService.authenticateUser(any(AuthRequest.class))).thenReturn("jwt-token");

        // When & Then
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));

        verify(userService, times(1)).authenticateUser(any(AuthRequest.class));
    }

    @Test
    void deleteUser_ShouldReturnSuccess() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When & Then
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void checkUsernameExists_WhenUsernameExists_ShouldReturnTrue() throws Exception {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/users/exists")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    @Test
    void checkUsernameExists_WhenUsernameDoesNotExist_ShouldReturnFalse() throws Exception {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/users/exists")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(userRepository, times(1)).existsByUsername("testuser");
    }
}