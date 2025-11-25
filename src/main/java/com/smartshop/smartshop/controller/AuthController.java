package com.smartshop.smartshop.controller;

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
    public ResponseEntity<?> login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        return ResponseEntity.ok("\"Login successful\"");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logout successful!");
    }
}
