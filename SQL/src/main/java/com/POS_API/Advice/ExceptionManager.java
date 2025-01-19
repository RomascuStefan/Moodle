package com.POS_API.Advice;

import com.POS_API.Advice.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Resource '%s' not found with %s = '%s'", ex.getResourceName(), ex.getFieldName(), ex.getFieldValue()));

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
        response.put("details", String.format("Duplicate entry detected for '%s' in '%s'", ex.getFieldName(), ex.getResourceName()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PaginatedViewOutOfBoundsException.class)
    public ResponseEntity<Map<String, Object>> handlePaginatedViewOutOfBoundsException(PaginatedViewOutOfBoundsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("error", "Unprocessable Entity");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }


    // Handle RequestParamWrong
    @ExceptionHandler(RequestParamWrong.class)
    public ResponseEntity<Object> handleRequestParamWrong(RequestParamWrong ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Invalid parameter '%s' with value '%s'. %s", ex.getParameterName(), ex.getParameterValue(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle EnumException
    @ExceptionHandler(EnumException.class)
    public ResponseEntity<Object> handleEnumException(EnumException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid Enum Value");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Invalid value '%s' for enum parameter '%s'", ex.getValue(), ex.getParameter()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle generic RuntimeException (fallback)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        response.put("details", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message",  String.format("Invalid value provided for the parameter '%s'", ex.getName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Illegal State");
        response.put("message", ex.getMessage());
        response.put("details", "Operation cannot be completed due to a conflict in the current state.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(MongoServiceException.class)
    public ResponseEntity<Map<String, Object>> handleMongoServiceException(MongoServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Mongo Service Error");
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);

    }

    @ExceptionHandler(IdmServiceException.class)
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(IdmServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}