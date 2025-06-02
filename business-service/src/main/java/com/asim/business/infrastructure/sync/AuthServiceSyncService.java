package com.asim.business.infrastructure.sync;

import com.asim.business.domain.user.model.dto.UserCreateDto;

public interface AuthServiceSyncService {
    boolean processUserCreation(UserCreateDto user);

    boolean invalidateToken(String sessionId);
}
