package aloha.spring.restful_web_services.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import aloha.spring.restful_web_services.user.UserNotFoundException;

// Implementation suggested by ChatGPT. No need to extend Spring's default handler class.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom exceptions
    // IMO, annotate exceptions with ResponseStatus is better
    // @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Custom handler for invalid request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleNotValidRequest(MethodArgumentNotValidException ex, WebRequest request) {
        // Customize error messages
        List<ObjectError> errors = ex.getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError objectError : errors) {
            message.append(objectError.getObjectName()).append(": ").append(objectError.getDefaultMessage())
                    .append(". ");
        }
        message.deleteCharAt(message.length() - 1);
        ErrorDetails errorDetails = new ErrorDetails(message.toString(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
