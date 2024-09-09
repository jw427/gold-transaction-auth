package com.wanted.gold.user.service;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.grpc.AuthProto;
import com.wanted.gold.grpc.AuthRequest;
import com.wanted.gold.grpc.AuthResponse;
import com.wanted.gold.grpc.AuthServiceGrpc;
import com.wanted.gold.user.config.TokenProvider;
import com.wanted.gold.user.domain.User;
import com.wanted.gold.user.repository.UserRepository;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceGrpcImpl extends AuthServiceGrpc.AuthServiceImplBase {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void authCall(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        try {
            String accessToken = request.getAccessToken();

            // 액세스 토큰이 유효하지 않을 경우 401 상태
            if(!tokenProvider.validToken(accessToken))
                throw new UnauthorizedException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
            // 액세스 토큰으로 username 찾기
            String username = tokenProvider.getUsername(accessToken);
            // username으로 User 찾기
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            // user의 userId
            UUID userId = user.getUserId();
            // 응답 생성
            AuthResponse response = AuthResponse.newBuilder()
                    // UUID를 문자열로 변환하여 설정
                    .setUserId(userId.toString())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
