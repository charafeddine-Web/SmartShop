package com.smartshop.smartshop.controller;

import com.smartshop.smartshop.dto.LoginDTO;
import com.smartshop.smartshop.dto.UserDto;
import com.smartshop.smartshop.entity.User;
import com.smartshop.smartshop.repository.UserRepository;
import com.smartshop.smartshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletRequest request) {

        User user = authService.login(dto.getUsername(), dto.getPassword());

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        UserDto retdto = new UserDto();
        retdto.setId(user.getId());
        retdto.setUsername(user.getUsername());
        retdto.setRole(user.getRole());

        return ResponseEntity.ok(retdto);    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request.getSession(false));
        return ResponseEntity.ok("Logout successful");
    }
}

