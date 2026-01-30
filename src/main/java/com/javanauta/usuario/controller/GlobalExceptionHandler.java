package com.javanauta.usuario.controller;

import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.exceptions.IllegalArgumentException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.usuario.infrastructure.exceptions.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.ObjectMapper;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e,
      HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        buildMessageError(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            request.getRequestURI(),
            "NOT FOUND"
            )
    );
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException e,
      HttpServletRequest request) {
    return  ResponseEntity.status(HttpStatus.CONFLICT).body(
        buildMessageError(
            HttpStatus.CONFLICT.value(),
            e.getMessage(),
            request.getRequestURI(),
            "CONFLICT ERROR"
        )
    );
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException e,
      HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        buildMessageError(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage(),
            request.getRequestURI(),
            "UNAUTHORIZED"
        )
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e,
      HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        buildMessageError(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            request.getRequestURI(),
            "ILLEGAL ARGUMENTS ERROR"
        )
    );
  }

  private ErrorResponseDTO buildMessageError(int status, String message, String path, String error) {
    return ErrorResponseDTO.builder()
        .status(status)
        .message(message)
        .path(path)
        .error(error)
        .build();

  }
}
