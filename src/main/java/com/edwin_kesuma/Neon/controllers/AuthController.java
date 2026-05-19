package com.edwin_kesuma.Neon.controllers;

import com.edwin_kesuma.Neon.domain.dtos.user.RequestLoginDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.RequestRegisterDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.ResponseLoginDTO;
import com.edwin_kesuma.Neon.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RequestRegisterDTO registerDTO) {

        authService.registerUser(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> loginUser(@RequestBody RequestLoginDTO loginDTO) {
        ResponseLoginDTO response = authService.loginUser(loginDTO);

        if (response.user() == null && response.jwtToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
