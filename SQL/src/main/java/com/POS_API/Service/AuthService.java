package com.POS_API.Service;

import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.IdmServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthServiceGrpcClient authServiceGrpcClient;

    @Autowired
    public AuthService(AuthServiceGrpcClient authServiceGrpcClient) {
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    public boolean registerUser(String email, String password, String role) {
        AuthServiceOuterClass.RegisterUserResponse response = authServiceGrpcClient.registerUser(email, password, role);

        if (response.getSuccess())
            return true;

        throw new IdmServiceException(HttpStatus.INTERNAL_SERVER_ERROR, response.getMessage());

    }
}