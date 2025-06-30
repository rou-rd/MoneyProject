package com.example.accountapp.security.service;

import com.example.accountapp.common.Status;
import com.example.accountapp.security.config.JwtUtil;
import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.model.VerificationToken;
import com.example.accountapp.security.repository.RoleRepository;
import com.example.accountapp.security.repository.UserRepository;
import com.example.accountapp.security.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registerRequest;
    private User testUser;
    private Role testRole;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");

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

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setStatus(Status.INFORCE);
        testUser.setRoles(Set.of(testRole));

        authRequest = new AuthRequest("testuser", "password123");
    }

    @Test
    void registerUser_WhenValidData_ShouldCreateUser() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(new VerificationToken());

        // When
        String result = userService.registerUser(registerRequest);

        // Then
        assertEquals("User registered!", result);
        verify(roleRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void registerUser_WhenRoleNotFound_ShouldThrowException() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.registerUser(registerRequest));

        assertEquals("ROLE_USER non trouvé", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void authenticateUser_WhenValidCredentials_ShouldReturnToken() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", testUser.getRoles())).thenReturn("jwt-token");

        // When
        String result = userService.authenticateUser(authRequest);

        // Then
        assertEquals("jwt-token", result);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtil, times(1)).generateToken("testuser", testUser.getRoles());
    }

    @Test
    void authenticateUser_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> userService.authenticateUser(authRequest));

        assertEquals("User not found", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), any());
    }

    @Test
    void authenticateUser_WhenUserNotAccepted_ShouldThrowException() {
        // Given
        testUser.setStatus(Status.Pending);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.authenticateUser(authRequest));

        assertEquals("the user not accepted", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), any());
    }

    @Test
    void authenticateUser_WhenInvalidPassword_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.authenticateUser(authRequest));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(jwtUtil, never()).generateToken(anyString(), any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
}