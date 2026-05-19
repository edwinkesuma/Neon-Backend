package com.edwin_kesuma.Neon.services.impl;

import com.edwin_kesuma.Neon.domain.dtos.user.RequestLoginDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.RequestRegisterDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.ResponseLoginDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.UserDTO;
import com.edwin_kesuma.Neon.domain.entities.user.Role;
import com.edwin_kesuma.Neon.domain.entities.user.User;
import com.edwin_kesuma.Neon.repositories.UserRepository;
import com.edwin_kesuma.Neon.security.util.JwtUtil;
import com.edwin_kesuma.Neon.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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

    @Override
    public ResponseLoginDTO loginUser(RequestLoginDTO loginDTO) {
        try {
            var
                    resultAuthentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(),
                            loginDTO.password()));

            // Generate JWT Token
            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);

            var loggedInUser = (User) resultAuthentication.getPrincipal();

            var
                    userDto =
                    new UserDTO(loggedInUser.getId(),
                            loggedInUser.getName(),
                            loggedInUser.getEmail(),
                            loggedInUser.getRole().name(),
                            loggedInUser.getImageUrl());

            return new ResponseLoginDTO(HttpStatus.OK.getReasonPhrase(), userDto, jwtToken);
        } catch (
                BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (
                AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    private ResponseLoginDTO buildErrorResponse(HttpStatus status,
                                                String message) {
        return new ResponseLoginDTO(message, null, null);
    }
}
