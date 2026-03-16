package com.example.Ecommerce_Backend.Service;

import com.example.Ecommerce_Backend.DTO.AuthResponse;
import com.example.Ecommerce_Backend.DTO.LoginRequest;
import com.example.Ecommerce_Backend.DTO.RegisterRequest;
import com.example.Ecommerce_Backend.Enum.Role;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.UserRepository;
import com.example.Ecommerce_Backend.Security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(registerRequest.getEmail(), null, "User already exist"));
        }
        Users user = new Users();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(registerRequest.getEmail(), token, "User registered successfully"));
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(loginRequest.getEmail(), token, "Login Success"));

        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(loginRequest.getEmail(), null, "Invalid email or password"));
        }
    }
}
