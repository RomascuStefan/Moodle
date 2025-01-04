package com.POS_API_MONGO.Service;


import auth.AuthServiceGrpc;
import auth.AuthServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class AuthServiceGrpcClient {

    private final ManagedChannel channel;
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    @Value("${mongo.service.user}")
    private String mongoUser;

    @Value("${mongo.service.pass}")
    private String mongoPass;

    public AuthServiceGrpcClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public String loginUser() {
        AuthServiceOuterClass.AuthenticateUserRequest request = AuthServiceOuterClass.AuthenticateUserRequest.newBuilder()
                .setEmail(mongoUser)
                .setPassword(mongoPass)
                .build();

        AuthServiceOuterClass.AuthenticateUserResponse response = blockingStub.authenticateUser(request);

        if (!response.getSuccess()) {
            throw new RuntimeException("Login failed: " + response.getMessage());
        }

        return response.getToken();
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

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}

