package com.smartshop.smartshop.service;

import com.smartshop.smartshop.entity.User;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    User login(String username, String password);
    void logout(HttpSession session);

}

