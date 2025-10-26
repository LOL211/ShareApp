package org.kush.share.api.controller;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.user.UserRegisterRequest;
import org.kush.share.api.controller.dtos.user.UserRegisterResponse;
import org.kush.share.api.controller.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController
{
    private final UserService userService;

    @PostMapping
    public UserRegisterResponse registerUser(@RequestBody UserRegisterRequest userRegisterRequest)
    {
        return userService.registerUser()
    }
}
