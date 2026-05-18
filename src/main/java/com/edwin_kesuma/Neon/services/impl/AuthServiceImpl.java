package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.user.RequestRegisterDTO;
import com.edwin_kesuma.Neon.domain.entities.user.Role;
import com.edwin_kesuma.Neon.domain.entities.user.User;
import com.edwin_kesuma.Neon.repositories.UserRepository;
import com.edwin_kesuma.Neon.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(RequestRegisterDTO registerDTO) {
        User
                user =
                User.builder()
                        .name(registerDTO.name())
                        .email(registerDTO.email())
                        .build();

        // Hash user password before saving it
        user.setPassword(passwordEncoder.encode(registerDTO.password()));

        // Set user role
        user.setRole(Role.USER);

        // Save user
        userRepository.save(user);
    }
}
