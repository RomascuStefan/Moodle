package com.POS_API_MONGO.Advice;

import com.POS_API_MONGO.Advice.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {

    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle UniqueKeyException
    @ExceptionHandler(UniqueKeyException.class)
    public ResponseEntity<Object> handleUniqueKeyException(UniqueKeyException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handle WrongLocationException
    @ExceptionHandler(WrongLocationException.class)
    public ResponseEntity<Object> handleWrongLocationException(WrongLocationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidPonderiException.class)
    public ResponseEntity<Object> handleInvalidPonderiException(InvalidPonderiException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IdmServiceException.class)
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(IdmServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(SqlServiceException.class)
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(SqlServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(FileNameExistsException.class)
    public ResponseEntity<Object> handleFileNameExistsException(FileNameExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", String.format("Fisierul '%s' exista deja în locatia '%s'.", ex.getFilename(), ex.getLocation()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }



    // Handle generic RuntimeException (fallback)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("details", "An unexpected error occurred");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
