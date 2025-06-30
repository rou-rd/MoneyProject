package com.example.accountapp.security.service;

import com.example.accountapp.security.dto.RoleRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.RoleRepository;
import com.example.accountapp.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private User testUser;
    private Role testRole;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ADMIN");
        testRole.setCreatedDate(LocalDateTime.now());

        roleRequest = new RoleRequest(1L, "ADMIN");
    }

    @Test
    void registerRole_WhenUserExists_ShouldCreateRole() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        // When
        String result = roleService.registerRole(roleRequest);

        // Then
        assertEquals("Role created with id: 1", result);
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void registerRole_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> roleService.registerRole(roleRequest));

        assertEquals("User not found", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getAllRoles_ShouldReturnAllRoles() {
        // Given
        List<Role> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // When
        List<RoleRequest> result = roleService.getAllRoles();

        // Then
        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).getRoleName());
        assertEquals(1L, result.get(0).getUserId());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getAccountById_WhenRoleExists_ShouldReturnRole() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));

        // When
        Optional<Role> result = roleService.getAccountById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testRole, result.get());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void getAccountById_WhenRoleDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Role> result = roleService.getAccountById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void deleteRole_ShouldCallRepositoryDelete() {
        // When
        roleService.deleteRole(1L);

        // Then
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldUpdateRole() {
        // Given
        RoleRequest updateRequest = new RoleRequest(1L, "UPDATED_ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        // When
        Role result = roleService.updateRole(1L, updateRequest);

        // Then
        assertNotNull(result);
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void updateRole_WhenRoleDoesNotExist_ShouldThrowException() {
        // Given
        RoleRequest updateRequest = new RoleRequest(1L, "UPDATED_ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> roleService.updateRole(1L, updateRequest));

        assertEquals("Rôle non trouvé", exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, never()).save(any(Role.class));
    }
}