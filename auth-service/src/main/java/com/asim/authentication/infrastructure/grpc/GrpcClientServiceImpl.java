package com.asim.authentication.infrastructure.grpc;

import com.asim.authentication.core.model.dto.UserInternal;
import com.asim.grpc.generated.GrpcServiceGrpc;
import com.asim.grpc.generated.InvalidateToken;
import com.asim.grpc.generated.NewUser;
import com.asim.grpc.generated.NewUserSynced;
import com.asim.grpc.generated.TokenInvalidated;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrpcClientServiceImpl implements GrpcClientService{

    @GrpcClient("grpc-service")
    private GrpcServiceGrpc.GrpcServiceBlockingStub grpcServiceStub;

    @Override
    public boolean sendUserCreated(UserInternal user) {
        try {
            NewUser newUser = NewUser.newBuilder()
                    .setId(user.getId())
                    .setName(user.getName())
                    .build();

            NewUserSynced response = grpcServiceStub.sendUserCreated(newUser);
            log.info("User created notification sent successfully: {}", response.getSuccess());
            return response.getSuccess();
        } catch (Exception e) {
            log.error("Error sending user created notification", e);
            return false;
        }
    }

    @Override
    public boolean blockSession(String sessionId) {
        try {
            InvalidateToken request = InvalidateToken.newBuilder()
                    .setSessionId(sessionId)
                    .build();

            TokenInvalidated response = grpcServiceStub.blockSession(request);
            log.info("Session invalidation request sent successfully: {}", response.getSuccess());
            return response.getSuccess();
        } catch (Exception e) {
            log.error("Error sending session invalidation request", e);
            return false;
        }
    }

    private void logError(String operation, Exception e) {
        log.error("gRPC operation '{}' failed: {}", operation, e.getMessage());
    }
}