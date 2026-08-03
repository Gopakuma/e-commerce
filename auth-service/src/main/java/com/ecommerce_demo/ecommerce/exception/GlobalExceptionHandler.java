package com.ecommerce_demo.ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
           MethodArgumentNotValidException ex,
           HttpServletRequest request) {

      String errorMessage = ex.getBindingResult()
              .getFieldErrors()
              .stream()
              .map(error -> error.getField() + ": " + error.getDefaultMessage())
              .collect(Collectors.joining(", "));

      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.BAD_REQUEST.value())
              .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
              .message(errorMessage)
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errorResponse);
   }

   @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {

       ErrorResponse errorResponse = ErrorResponse.builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.CONFLICT.value())
               .error(HttpStatus.CONFLICT.getReasonPhrase())
               .message(ex.getMessage())
               .path(request.getRequestURI())
               .build();

       return ResponseEntity.status(HttpStatus.CONFLICT)
               .body(errorResponse);
   }

   @ExceptionHandler(UserRegistrationFailedException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationFailedException(
            UserRegistrationFailedException ex,
            HttpServletRequest request) {

       ErrorResponse errorResponse = ErrorResponse.builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
               .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
               .message(ex.getMessage())
               .path(request.getRequestURI())
               .build();

         ex.printStackTrace();

       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(errorResponse);
   }

   @ExceptionHandler(LoginFailedException.class)
   public ResponseEntity<ErrorResponse> handleLoginFailedException(
           LoginFailedException ex,
           HttpServletRequest request) {
      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
              .status(HttpStatus.BAD_REQUEST.value())
              .message(ex.getMessage())
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errorResponse);
   }

   @ExceptionHandler(RefreshTokenFailedException.class)
   public ResponseEntity<ErrorResponse> handleRefreshTokenFailedException(
           RefreshTokenFailedException ex,
           HttpServletRequest request) {

      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
              .status(HttpStatus.BAD_REQUEST.value())
              .message(ex.getMessage())
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errorResponse);
   }

   @ExceptionHandler(ResetPasswordFailedException.class)
   public ResponseEntity<ErrorResponse> handleRefreshTokenFailedException(
           ResetPasswordFailedException ex,
           HttpServletRequest request) {

      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
              .status(HttpStatus.BAD_REQUEST.value())
              .message(ex.getMessage())
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errorResponse);
   }

   @ExceptionHandler(AccessDeniedException.class)
   public ResponseEntity<ErrorResponse> handleAccessDeniedException(
           AccessDeniedException ex,
           HttpServletRequest request) {

      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .error(HttpStatus.UNPROCESSABLE_CONTENT.getReasonPhrase())
              .status(HttpStatus.UNPROCESSABLE_CONTENT.value())
              .message(ex.getMessage())
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
              .body(errorResponse);
   }

   @ExceptionHandler(UserDeactivatedException.class)
   public ResponseEntity<ErrorResponse> handleUserDeactivatedException(
           UserDeactivatedException ex,
           HttpServletRequest request) {

      ErrorResponse errorResponse = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .error(HttpStatus.FORBIDDEN.getReasonPhrase())
              .status(HttpStatus.FORBIDDEN.value())
              .message(ex.getMessage())
              .path(request.getRequestURI())
              .build();

      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(errorResponse);
   }

   @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
           Exception ex,
            HttpServletRequest request) {

       ErrorResponse errorResponse = ErrorResponse.builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
               .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
               .message("Something went wrong")
               .path(request.getRequestURI())
               .build();
            ex.printStackTrace();
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(errorResponse);
   }
}
