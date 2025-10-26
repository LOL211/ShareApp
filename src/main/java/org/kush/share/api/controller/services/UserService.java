package org.kush.share.api.controller.services;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.database.models.User;
import org.kush.share.api.database.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserRepository userRepository;

    public boolean registerUser(User user)
    {

    }
}
