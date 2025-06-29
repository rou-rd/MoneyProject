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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    private RoleRepository roleRepository;
    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.verification-token.expiration}") private long tokenExpiration;


    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepository, VerificationTokenRepository tokenRepo, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
        this.tokenRepo = tokenRepo;
        this.mailSender = mailSender;
    }

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired private
    PasswordEncoder passwordEncoder;
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
//mail config
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUser(user);
        vt.setExpiryDate(LocalDateTime.now().plus(Duration.ofMillis(tokenExpiration)));
        vt.setCreatedBy(user.getUsername());
        tokenRepo.save(vt);
//mail config
        sendVerificationEmail(user.getEmail(), token);
        return "User registered!";
    }
    @Override
    public String authenticateUser(AuthRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!Status.INFORCE.equals(user.getStatus())){
            throw new RuntimeException("the user not accepted");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }
    private void sendVerificationEmail(String email, String token) {
        String url = "http://localhost:8081/api/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email");
        message.setText("Click the link to verify your account: " + url);
        mailSender.send(message);
    }


    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Supprimer l'utilisateur de chaque rôle
        for (Role role : user.getRoles()) {
            role.getUsers().remove(user); // très important pour mise à jour côté Role
        }

        user.getRoles().clear(); // supprimer toutes les relations côté user

        userRepo.delete(user); // suppression de l'utilisateur
    }
}

