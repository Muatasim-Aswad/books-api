package com.asim.authentication.core.service.UserService;

import com.asim.authentication.core.model.dto.UserInput;
import com.asim.authentication.core.model.dto.UserPublic;

public interface UserService {
    UserPublic registerUser(UserInput userInput);
    void deleteUser();
    UserPublic changePassword(UserInput userInput);
}
