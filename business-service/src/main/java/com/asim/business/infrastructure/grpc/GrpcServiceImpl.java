package com.asim.business.infrastructure.grpc;

import com.asim.business.infrastructure.sync.AuthServiceSyncServiceImpl;
import com.asim.grpc.generated.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    private final AuthServiceSyncServiceImpl userSyncServiceImpl;
    private final GrpcUserMapper grpcUserMapper;

    @Override
    public void sendUserCreated(NewUser request, StreamObserver<NewUserSynced> responseObserver) {
        log.info("Received user created notification for user ID: {}", request.getId());

        var isDone = userSyncServiceImpl.processUserCreation(grpcUserMapper.toUserCeateDto((request)));

        NewUserSynced response = NewUserSynced.newBuilder()
                .setSuccess(isDone)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void blockSession(InvalidateToken request, StreamObserver<TokenInvalidated> responseObserver) {
        String sessionId = request.getSessionId();
        log.info("Received request to invalidate token with session ID: {}", sessionId);

        var isDone = userSyncServiceImpl.invalidateToken(sessionId);

        TokenInvalidated response = TokenInvalidated.newBuilder()
                .setSuccess(isDone)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}