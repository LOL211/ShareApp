package org.kush.share.api.config;

public record JwtTokenDto(
        String email,
        String scopes
){}
