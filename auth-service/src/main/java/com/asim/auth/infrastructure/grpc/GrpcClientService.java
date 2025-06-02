package com.asim.auth.infrastructure.grpc;

import com.asim.auth.core.model.dto.UserInternal;

public interface GrpcClientService {
    boolean sendUserCreated(UserInternal user);

    boolean blockSession(String sessionId);
}
