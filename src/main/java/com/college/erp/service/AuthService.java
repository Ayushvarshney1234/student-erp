package com.college.erp.service;

import com.college.erp.dto.LoginRequest;
import com.college.erp.dto.LoginResponse;
import com.college.erp.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse getCurrentUser(String username);
}
