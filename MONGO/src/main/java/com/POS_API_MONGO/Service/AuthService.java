package com.POS_API_MONGO.Service;

import auth.AuthServiceOuterClass;
import com.POS_API_MONGO.Advice.Exception.IdmServiceException;
import com.POS_API_MONGO.Advice.Exception.ResourceNotFoundException;
import com.POS_API_MONGO.DTO.SqlServiceRequestDTO;
import com.POS_API_MONGO.Helper.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService {

    public final static AuthServiceOuterClass.Role ADMIN = AuthServiceOuterClass.Role.admin;
    public final static AuthServiceOuterClass.Role PROFESOR = AuthServiceOuterClass.Role.profesor;
    public final static AuthServiceOuterClass.Role STUDENT = AuthServiceOuterClass.Role.student;
    public final static AuthServiceOuterClass.Role SQL = AuthServiceOuterClass.Role.sql;
    public final static AuthServiceOuterClass.Role MONGO = AuthServiceOuterClass.Role.mongo;


    private final AuthServiceGrpcClient authServiceGrpcClient;

    @Value("${sql.service.url}")
    private String sqlServiceUrl;


    @Autowired
    public AuthService(AuthServiceGrpcClient authServiceGrpcClient) {
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    public AuthServiceOuterClass.Role canAccessResource(String codDisciplina, String authorizationHeader, List<AuthServiceOuterClass.Role> allowedRoles) {
        AuthServiceOuterClass.GetUserDetailsResponse response = getUserDetails(authorizationHeader, allowedRoles);

        SqlServiceRequestDTO sqlReq = new SqlServiceRequestDTO();
        sqlReq.setRole(response.getRole().toString().toLowerCase());
        sqlReq.setEmail(response.getEmail());
        sqlReq.setCodDisciplina(codDisciplina);

        String url = sqlServiceUrl + "/validate/access_resource";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getHeader());

        HttpEntity<SqlServiceRequestDTO> requestEntity = new HttpEntity<>(sqlReq, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> sqlResponse = restTemplate.postForEntity(url, requestEntity, String.class);

            return response.getRole();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new ResourceNotFoundException("Disciplina", "cod", codDisciplina);
            else if (ex.getStatusCode() == HttpStatus.FORBIDDEN)
                throw new IdmServiceException(HttpStatus.FORBIDDEN, "Access denied to resource");

            throw new IdmServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Error communicating with SQL service: " + ex.getMessage());
        }
    }


    public AuthServiceOuterClass.Role canModifyResource(String codDisciplina, String authorizationHeader, List<AuthServiceOuterClass.Role> allowedRoles) {
        AuthServiceOuterClass.GetUserDetailsResponse response = getUserDetails(authorizationHeader, allowedRoles);

        SqlServiceRequestDTO sqlReq = new SqlServiceRequestDTO();
        sqlReq.setRole(response.getRole().toString().toLowerCase());
        sqlReq.setEmail(response.getEmail());
        sqlReq.setCodDisciplina(codDisciplina);

        String url = sqlServiceUrl + "/validate/modify_resource";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getHeader());

        HttpEntity<SqlServiceRequestDTO> requestEntity = new HttpEntity<>(sqlReq, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> sqlResponse = restTemplate.postForEntity(url, requestEntity, String.class);

            return response.getRole();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Disciplina", "cod", codDisciplina);
            } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new IdmServiceException(HttpStatus.FORBIDDEN, "Modification access denied");
            }
            throw new IdmServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Error communicating with SQL service: " + ex.getMessage());
        }
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

    private AuthServiceOuterClass.GetUserDetailsResponse getUserDetails(String authorizationHeader, List<AuthServiceOuterClass.Role> allowedRoles) {
        String token = extractToken(authorizationHeader);

        AuthServiceOuterClass.GetUserDetailsResponse response = authServiceGrpcClient.getUserDetails(token);

        if (!response.getSuccess()) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, response.getMessage());
        }

        if (!allowedRoles.contains(response.getRole())) {
            throw new IdmServiceException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        return response;
    }

    private String getHeader() {
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
