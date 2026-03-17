package com.example.Ecommerce_Backend.Controller;

import com.example.Ecommerce_Backend.DTO.AdminUserDto;
import com.example.Ecommerce_Backend.Enum.Role;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<AdminUserDto> users = userRepository.findAll().stream()
                .map(user -> new AdminUserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam Role role, Authentication authentication) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String currentAdminEmail = authentication.getName();
        if (user.getEmail().equals(currentAdminEmail) && role != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot demote your own admin account");
        }

        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok(new AdminUserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String currentAdminEmail = authentication.getName();
        if (user.getEmail().equals(currentAdminEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete your own admin account");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}
