package com.asim.authentication.core.service.UserService;

import com.asim.authentication.common.exception.DuplicateResourceException;
import com.asim.authentication.core.model.dto.UserInput;
import com.asim.authentication.core.model.dto.UserPublic;
import com.asim.authentication.core.model.mapper.UserInputMapper;
import com.asim.authentication.core.model.mapper.UserInternalMapper;
import com.asim.authentication.core.model.mapper.UserPublicMapper;
import com.asim.authentication.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInputMapper userInputMapper;
    private final UserPublicMapper userPublicMapper;
    private final UserInternalMapper userInternalMapper;

    public UserPublic registerUser(UserInput userInput) {
        // Check if user already exists by name
        if (userRepository.existsByName(userInput.getName())) {
            throw new DuplicateResourceException("User", "name", userInput.getName());
        }

        // Hash the password
        // Note: Password hashing should be done using a secure method, such as BCrypt.
        // userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));

        // Save to the db
        var user = userInputMapper.toEntity(userInput);
        user = userRepository.save(user);

        return userPublicMapper.toDto(user);
    }

    public void DeleteUser() {
      Long userId = 1L;

        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new DuplicateResourceException("User", "id", userId.toString());
        }

        // Delete the user
        userRepository.deleteById(userId);
    }
}
