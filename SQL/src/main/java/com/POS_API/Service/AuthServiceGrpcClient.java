package com.POS_API.Service;

import auth.AuthServiceGrpc;
import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.EnumException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class AuthServiceGrpcClient {

    private final ManagedChannel channel;
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    @Value("${sql.service.user}")
    private String sqlUser;

    @Value("${sql.service.pass}")
    private String sqlPass;

    public AuthServiceGrpcClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AuthServiceOuterClass.RegisterUserResponse registerUser(String email, String password, String role) {
        AuthServiceOuterClass.Role userRole = switch (role) {
            case "admin" -> AuthServiceOuterClass.Role.admin;
            case "profesor" -> AuthServiceOuterClass.Role.profesor;
            case "student" -> AuthServiceOuterClass.Role.student;
            case "mongo" -> AuthServiceOuterClass.Role.mongo;
            case "sql" -> AuthServiceOuterClass.Role.sql;
            default -> throw new EnumException(role, "rol");
        };

        AuthServiceOuterClass.RegisterUserRequest request = AuthServiceOuterClass.RegisterUserRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .setRole(userRole)
                .build();

        return blockingStub.registerUser(request);
    }

    public AuthServiceOuterClass.VerifyTokenResponse verifyToken(String jwt) {
        AuthServiceOuterClass.VerifyTokenRequest request = AuthServiceOuterClass.VerifyTokenRequest.newBuilder()
                .setToken(jwt.trim())
                .build();

        return blockingStub.verifyToken(request);
    }

    public AuthServiceOuterClass.GetUserDetailsResponse getUserDetails(String jwt) {
        AuthServiceOuterClass.GetUserDetailsRequest request = AuthServiceOuterClass.GetUserDetailsRequest.newBuilder()
                .setToken(jwt)
                .build();

        return blockingStub.getUserDetails(request);
    }

    public String loginUser() {
        AuthServiceOuterClass.AuthenticateUserRequest request = AuthServiceOuterClass.AuthenticateUserRequest.newBuilder()
                .setEmail(sqlUser)
                .setPassword(sqlPass)
                .build();

        AuthServiceOuterClass.AuthenticateUserResponse response = blockingStub.authenticateUser(request);

        if (!response.getSuccess()) {
            throw new RuntimeException("Login failed: " + response.getMessage());
        }

        return response.getToken();
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }

}
