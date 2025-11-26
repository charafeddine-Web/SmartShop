package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.entity.User;
import com.smartshop.smartshop.repository.UserRepository;
import com.smartshop.smartshop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public User login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }
        return user;
    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}

