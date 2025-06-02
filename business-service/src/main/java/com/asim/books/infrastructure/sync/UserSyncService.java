package com.asim.books.infrastructure.sync;

import com.asim.books.domain.user.model.dto.UserCreateDto;

public interface UserSyncService {
    boolean processUserCreation(UserCreateDto user);

    boolean invalidateToken(String sessionId);
}
