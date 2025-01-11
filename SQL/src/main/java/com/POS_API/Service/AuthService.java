package com.POS_API.Service;

import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.IdmServiceException;
import com.POS_API.DTO.UserDetailDTO;
import com.POS_API.Helper.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserDetailDTO getUserDetail(String authorizationHeader, List<AuthServiceOuterClass.Role> allowedRoles) {
        String token = extractToken(authorizationHeader);

        AuthServiceOuterClass.GetUserDetailsResponse response = authServiceGrpcClient.getUserDetails(token);

        if (!response.getSuccess())
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, response.getMessage());

        if (!allowedRoles.contains(response.getRole()))
            throw new IdmServiceException(HttpStatus.FORBIDDEN, "Not allowed");


        UserDetailDTO userDetail = new UserDetailDTO();
        userDetail.setEmail(response.getEmail());
        userDetail.setRole(response.getRole());

        return userDetail;
    }

    public AuthServiceOuterClass.Role verifyRequest(String authorizationHeader, List<AuthServiceOuterClass.Role> allowedRoles) {
        String token = extractToken(authorizationHeader);

        AuthServiceOuterClass.VerifyTokenResponse response = authServiceGrpcClient.verifyToken(token);

        if (!response.getValid())
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, response.getMessage());

        if (!allowedRoles.contains(response.getRole()))
            throw new IdmServiceException(HttpStatus.FORBIDDEN, "Not allowed");

        return response.getRole();
    }

    private String extractToken(String authorizationHeader) {
        final String errorMessage = "Authorization header is missing or invalid: ";

        if (authorizationHeader == null) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header is null");
        }
        if (authorizationHeader.trim().isEmpty()) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header is empty");
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header must start with 'Bearer '");
        }

        return authorizationHeader.substring(7);
    }

    public String getHeader() {
        try {
            String token;

            if(!TokenValidator.isTokenValid()) {
                token = authServiceGrpcClient.loginUser();
                TokenValidator.setToken(token);
            }
            else
                token= TokenValidator.getToken();


            return "Bearer " + token;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Authorization header: " + e.getMessage(), e);
        }
    }
}