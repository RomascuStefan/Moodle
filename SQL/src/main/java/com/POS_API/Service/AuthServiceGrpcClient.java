package com.POS_API.Service;

import auth.AuthServiceGrpc;
import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.EnumException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Console;

@Component
public class AuthServiceGrpcClient {

    private final ManagedChannel channel;
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

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
            default -> throw new EnumException(role, "rol");
        };

        AuthServiceOuterClass.RegisterUserRequest request = AuthServiceOuterClass.RegisterUserRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .setRole(userRole)
                .build();

        System.out.println(request.getRole());
        return blockingStub.registerUser(request);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
