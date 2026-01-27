package se.jensen.anton.springer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * GlobalExceptionHandler is a centralized exception handler for REST controllers.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * This method extracts field errors from the exception and returns them as {@link Map} of field validation errors
     * * them as a map where the key is the field name and the value is the
     * * validation error message.
     *
     * @param ex {@link MethodArgumentNotValidException} thrown by a controller
     * @return {@link ResponseEntity} containing {@link Map} of field validation errors, which consists of the field name as the key and the validation error message as the value, with HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException
    (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (org.springframework.validation.FieldError fieldError :
                ex.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            errors.put(fieldName, message);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * This method returns the exception message as the response.
     * The error occurs when a requested resource is not found in the database or the system.
     *
     * @param ex {@link NoSuchElementException} thrown by a controller
     * @return {@link ResponseEntity} containing the error message with HTTP 404 status
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
