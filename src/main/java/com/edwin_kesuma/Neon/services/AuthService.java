package com.edwin_kesuma.Neon.services;

import com.edwin_kesuma.Neon.domain.dtos.user.RequestLoginDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.RequestRegisterDTO;
import com.edwin_kesuma.Neon.domain.dtos.user.ResponseLoginDTO;

public interface AuthService {
    void registerUser(RequestRegisterDTO registerDTO);

    ResponseLoginDTO loginUser(RequestLoginDTO loginDTO);
}
