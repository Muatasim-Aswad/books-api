package com.asim.auth.core.service.UserService;

import com.asim.auth.core.model.dto.UserInput;
import com.asim.auth.core.model.dto.UserPublic;

public interface UserService {
    UserPublic registerUser(UserInput userInput);

    void deleteUser();

    UserPublic changePassword(UserInput userInput);
}
