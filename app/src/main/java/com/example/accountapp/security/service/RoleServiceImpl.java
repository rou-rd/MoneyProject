package com.example.accountapp.security.service;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.security.dto.RoleRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.RoleRepository;
import com.example.accountapp.security.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    public RoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    @Override
    public String registerRole(RoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = new Role();
        role.setName(request.getRoleName());
        role.setCreatedBy(user.getUsername());

        Role savedRole = roleRepository.save(role);
        return "Role created with id: " + savedRole.getId();
    }

    @Override
    public List<RoleRequest> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleRequest(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Role> getAccountById(Long id) {
        return roleRepository.findById(id);
    }
    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role updateRole(Long id, RoleRequest request) {
        return roleRepository.findById(id).map(role -> {
            role.setName(request.getRoleName());
            role.setLastModifiedDate(LocalDateTime.now());
            return roleRepository.save(role);
        }).orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
    }
}
