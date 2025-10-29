package org.kush.share.api.controller.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank(message = "Missing username")
        String username
) {}
