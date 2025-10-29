package org.kush.share.api.controller.dtos;

public record ErrorDto(
        String error_message,
        String errorType
){}
