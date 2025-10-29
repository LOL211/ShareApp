package org.kush.share.api.controller;


import org.kush.share.api.controller.dtos.ErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandlerAdvice
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex)
    {
        ErrorDto dto = new ErrorDto(ex.getMessage(), ex.getClass().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage(), ex.getClass().toString()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleSqlError(DataIntegrityViolationException ex)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Internal Server Error when saving", "DB error"));
    }

}
