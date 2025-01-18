package com.POS_API.Config;

import auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${idm.service.host}")
    private String grpcHost;

    @Value("${idm.service.port}")
    private int grpcPort;
    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub(ManagedChannel grpcChannel) {
        return AuthServiceGrpc.newBlockingStub(grpcChannel);
    }
}