package com.example.accountapp.security.service;

import com.example.accountapp.common.Status;
import com.example.accountapp.security.config.JwtUtil;
import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.RoleRepository;
import com.example.accountapp.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepository) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
    }

    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Override
    public String registerUser(RegisterRequest request) {
        User user = new User();
        Role userRole = roleRepository.findById(request.getRoleid())
                .orElseThrow(() -> new RuntimeException("ROLE_USER non trouvé"));
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Set.of(userRole));
        user.setCreatedBy(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(Status.Pending);
        user.setBirthDate(request.getBirthDate());
        userRepo.save(user);
        return "User registered!";
    }
    @Override
    public String authenticateUser(AuthRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }
}

