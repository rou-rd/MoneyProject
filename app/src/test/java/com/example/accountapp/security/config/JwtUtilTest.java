package com.example.accountapp.security.config;

import com.example.accountapp.security.model.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Set<Role> testRoles;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        // Set test values using reflection
        ReflectionTestUtils.setField(jwtUtil, "secret", 
            "c29tZXZlcnlzZWN1cmVqd3RzZWNyZXRrZXl0aGF0aXNhdGVhc3Q2NGJ5dGVsbG9uZ3N0cmluZw==");
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000L);
        
        // Initialize the secret key
        jwtUtil.init();

        // Create test roles
        Role testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");
        testRole.setCreatedDate(LocalDateTime.now());
        
        testRoles = Set.of(testRole);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Given
        String username = "testuser";

        // When
        String token = jwtUtil.generateToken(username, testRoles);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Given
        String username = "testuser";
        String token = jwtUtil.generateToken(username, testRoles);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void extractClaims_ShouldReturnValidClaims() {
        // Given
        String username = "testuser";
        String token = jwtUtil.generateToken(username, testRoles);

        // When
        Claims claims = jwtUtil.extractClaims(token);

        // Then
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
        assertNotNull(claims.get("roles"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void extractRoles_ShouldReturnCorrectRoles() {
        // Given
        String username = "testuser";
        String token = jwtUtil.generateToken(username, testRoles);

        // When
        List<Map<String, Object>> extractedRoles = jwtUtil.extractRoles(token);

        // Then
        assertNotNull(extractedRoles);
        assertEquals(1, extractedRoles.size());
        
        Map<String, Object> roleMap = extractedRoles.get(0);
        assertEquals("USER", roleMap.get("name"));
        assertNotNull(roleMap.get("createdDate"));
    }

    @Test
    void generateToken_WithMultipleRoles_ShouldIncludeAllRoles() {
        // Given
        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");
        adminRole.setCreatedDate(LocalDateTime.now());
        
        Set<Role> multipleRoles = Set.of(
            testRoles.iterator().next(),
            adminRole
        );
        
        String username = "testuser";

        // When
        String token = jwtUtil.generateToken(username, multipleRoles);
        List<Map<String, Object>> extractedRoles = jwtUtil.extractRoles(token);

        // Then
        assertEquals(2, extractedRoles.size());
        
        boolean hasUserRole = extractedRoles.stream()
            .anyMatch(role -> "USER".equals(role.get("name")));
        boolean hasAdminRole = extractedRoles.stream()
            .anyMatch(role -> "ADMIN".equals(role.get("name")));
            
        assertTrue(hasUserRole);
        assertTrue(hasAdminRole);
    }

    @Test
    void extractClaims_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtUtil.extractClaims(invalidToken));
    }
}