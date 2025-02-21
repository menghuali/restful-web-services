package aloha.spring.restful_web_services.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import aloha.spring.restful_web_services.user.UserNotFoundException;

// Implementation suggested by ChatGPT. No need to extend Spring's default handler class.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom exceptions
    // IMO, annotate exceptions with ResponseStatus is better
    // @ExceptionHandler(UserNotFoundException.class)
    // public ResponseEntity<ErrorDetails> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
    //     ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
    //             request.getDescription(false));
    //     return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    // }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(exception = Exception.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorDetails handleAllExceptions(Exception ex, WebRequest request) {
        // Ignore certain types of exceptions
        if (ex instanceof UserNotFoundException) {
            throw (UserNotFoundException) ex;
        }
        return new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    }
    // public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
    //     // Ignore certain types of exceptions
    //     if (ex instanceof UserNotFoundException) {
    //         throw (UserNotFoundException) ex;
    //     }

    //     ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
    //             request.getDescription(false));
    //     return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    /**
     * Custom handler for invalid request:
     * Solution 1: Returns custom object and use annotations to define response
     * code, body, and content-type.
     * 
     * Solution 2: Use ResponseEntity to set response code, body, and content type.
     * 
     * IMO, Solution 1 is simpler and better.
     */
    // Solution 1:
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = MethodArgumentNotValidException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorDetails handleNotValidRequest(MethodArgumentNotValidException ex, WebRequest request) {
        // Customize error messages
        List<ObjectError> errors = ex.getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError objectError : errors) {
            message.append(objectError.getObjectName()).append(": ").append(objectError.getDefaultMessage())
                    .append(". ");
        }
        message.deleteCharAt(message.length() - 1);
        return new ErrorDetails(message.toString(), request.getDescription(false));
    }
    // Solution 2:
    // public ResponseEntity<ErrorDetails> handleNotValidRequest(MethodArgumentNotValidException ex, WebRequest request) {
    //     // Customize error messages
    //     List<ObjectError> errors = ex.getAllErrors();
    //     StringBuilder message = new StringBuilder();
    //     for (ObjectError objectError : errors) {
    //         message.append(objectError.getObjectName()).append(": ").append(objectError.getDefaultMessage())
    //                 .append(". ");
    //     }
    //     message.deleteCharAt(message.length() - 1);
    //     ErrorDetails errorDetails = new ErrorDetails(message.toString(), request.getDescription(false));
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    //     ResponseEntity<ErrorDetails> resp = ResponseEntity.badRequest().headers(headers).body(errorDetails);
    //     return resp;
    // }

}
