package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.LoginDTO;
import com.smartshop.smartshop.dto.UserDto;
import com.smartshop.smartshop.entity.User;
import com.smartshop.smartshop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO,
                                   HttpServletRequest request) {

        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElse(null);

        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logout successful");
    }
}
