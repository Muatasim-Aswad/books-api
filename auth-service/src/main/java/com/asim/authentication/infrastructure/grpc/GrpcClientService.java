package com.asim.authentication.infrastructure.grpc;

import com.asim.authentication.core.model.dto.UserInternal;

public interface GrpcClientService {
    boolean sendUserCreated(UserInternal user);

    boolean blockSession(String sessionId);
}
