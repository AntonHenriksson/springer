package se.jensen.anton.springer.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
}
