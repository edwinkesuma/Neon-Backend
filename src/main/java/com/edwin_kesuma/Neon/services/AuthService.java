package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.user.RequestRegisterDTO;

public interface AuthService {
    void registerUser(RequestRegisterDTO registerDTO);
}
