package com.asim.auth.core.service.UserService;

import com.asim.auth.common.exception.DuplicateResourceException;
import com.asim.auth.common.exception.IllegalAttemptToModify;
import com.asim.auth.common.exception.ResourceNotFoundException;
import com.asim.auth.common.exception.UnauthorizedException;
import com.asim.auth.core.model.dto.UserInput;
import com.asim.auth.core.model.dto.UserPublic;
import com.asim.auth.core.model.mapper.UserInputMapper;
import com.asim.auth.core.model.mapper.UserInternalMapper;
import com.asim.auth.core.model.mapper.UserPublicMapper;
import com.asim.auth.core.repository.UserRepository;
import com.asim.auth.infrastructure.grpc.GrpcClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInputMapper userInputMapper;
    private final UserPublicMapper userPublicMapper;
    private final UserInternalMapper userInternalMapper;
    private final PasswordEncoder passwordEncoder;
    private final GrpcClientService grpcClientService;

    public UserPublic registerUser(UserInput userInput) {
        // Check if a user already exists by name
        if (userRepository.existsByName(userInput.getName())) {
            throw new DuplicateResourceException("User", "name", userInput.getName());
        }

        // Hash the password
        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));

        // Save to the db
        var user = userInputMapper.toEntity(userInput);
        user = userRepository.save(user);

        // send to business service via gRPC
        var userInternal = userInternalMapper.toDto(user);
        var isDone = grpcClientService.sendUserCreated(userInternal);

        log.info("User registration is done: {}", isDone);

        return userPublicMapper.toDto(user);
    }

    public void deleteUser() {
        Long userId = getCurrentUserId();

        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User");
        }

        // Delete the user
        userRepository.deleteById(userId);
    }

    public UserPublic changePassword(UserInput userInput) {
        Long userId = getCurrentUserId();

        // get the user from the db
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        // Check if the name is different from the current one
        if (!user.getName().equals(userInput.getName())) {
            throw new IllegalAttemptToModify("user name");
        }

        // Check if the new password is different from the old one
        if (passwordEncoder.matches(userInput.getPassword(), user.getPassword())) {
            return userPublicMapper.toDto(user);
        }

        // Hash the new password
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));

        // Save the updated user
        user = userRepository.save(user);

        return userPublicMapper.toDto(user);
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        throw new UnauthorizedException("User not authenticated");
    }
}
