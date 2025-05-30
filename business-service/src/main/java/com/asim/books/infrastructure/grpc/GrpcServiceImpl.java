package com.asim.books.infrastructure.grpc;

import com.asim.grpc.generated.GrpcServiceGrpc;
import com.asim.grpc.generated.InvalidateToken;
import com.asim.grpc.generated.NewUser;
import com.asim.grpc.generated.NewUserSynced;
import com.asim.grpc.generated.TokenInvalidated;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    private final UserSyncService userSyncService;

    @Override
    public void sendUserCreated(NewUser request, StreamObserver<NewUserSynced> responseObserver) {
        log.info("Received user created notification for user ID: {}", request.getId());

        userSyncService.processUserCreation(request);

        NewUserSynced response = NewUserSynced.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void blockSession(InvalidateToken request, StreamObserver<TokenInvalidated> responseObserver) {
        String sessionId = request.getSessionId();
        log.info("Received request to invalidate token with session ID: {}", sessionId);

        userSyncService.invalidateToken(sessionId);

        TokenInvalidated response = TokenInvalidated.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}