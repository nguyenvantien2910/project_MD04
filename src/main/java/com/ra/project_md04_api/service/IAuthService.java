package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormLogin;
import com.ra.project_md04_api.model.dto.request.FormRegister;
import com.ra.project_md04_api.model.dto.response.JwtResponse;

public interface IAuthService {
    JwtResponse handleLogin(FormLogin formLogin) throws CustomException;

    void handleRegister(FormRegister formRegister);
}
