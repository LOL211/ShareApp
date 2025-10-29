package org.kush.share.api.controller.services;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.config.JwtToken;
import org.kush.share.api.controller.dtos.user.UserRegisterRequest;
import org.kush.share.api.controller.dtos.user.UserRegisterResponse;
import org.kush.share.api.database.models.User;
import org.kush.share.api.database.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserRegisterResponse registerUser(UserRegisterRequest userRequest, JwtToken jwtToken)
    {
        userRepository.findByEmail((String) jwtToken.getPrincipal())
                .ifPresent(user -> {throw new IllegalArgumentException("Email "+user.getEmail()+" already exists");});

        User user = new User();
        user.setEmail((String) jwtToken.getPrincipal());
        user.setUsername(userRequest.username());

        user = userRepository.save(user);

        return new UserRegisterResponse(user.getUsername(), user.getEmail());
    }
}
