package com.wanted.gold.server;

import com.wanted.gold.user.service.AuthServiceGrpcImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthServer implements ApplicationRunner {
    // gRPC 서버 포트
    private final int port = 50051;
    private final AuthServiceGrpcImpl authServiceGrpc;
    private Server server;

    public AuthServer(AuthServiceGrpcImpl authServiceGrpc) {
        this.authServiceGrpc = authServiceGrpc;
        this.server = ServerBuilder.forPort(port)
                .addService(authServiceGrpc)
                .build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}
