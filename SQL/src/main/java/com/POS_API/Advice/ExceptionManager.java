package com.POS_API.Advice;

import com.POS_API.Advice.Exception.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Resource Not Found")
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Resource '%s' not found with %s = '%s'", ex.getResourceName(), ex.getFieldName(), ex.getFieldValue()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UniqueKeyException.class)
    @ApiResponse(responseCode = "409", description = "Conflict: Duplicate entry detected")
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
    @ApiResponse(responseCode = "422", description = "Unprocessable Entity: Pagination parameters are out of bounds")
    public ResponseEntity<Map<String, Object>> handlePaginatedViewOutOfBoundsException(PaginatedViewOutOfBoundsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("error", "Unprocessable Entity");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(RequestParamWrong.class)
    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid request parameters")
    public ResponseEntity<Object> handleRequestParamWrong(RequestParamWrong ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Invalid parameter '%s' with value '%s'. %s", ex.getParameterName(), ex.getParameterValue(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EnumException.class)
    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid Enum Value")
    public ResponseEntity<Object> handleEnumException(EnumException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid Enum Value");
        response.put("message", ex.getMessage());
        response.put("details", String.format("Invalid value '%s' for enum parameter '%s'", ex.getValue(), ex.getParameter()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error: Unexpected runtime exception")
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
    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid type for request parameter")
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", String.format("Invalid value provided for the parameter '%s'", ex.getName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ApiResponse(responseCode = "409", description = "Conflict: Illegal state for the requested operation")
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
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Mongo Service Error: Conflict"),
            @ApiResponse(responseCode = "500", description = "Mongo Service Error: Internal Service Error")
    })
    public ResponseEntity<Map<String, Object>> handleMongoServiceException(MongoServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Mongo Service Error");
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(IdmServiceException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "IDM Service Error: Unauthorized"),
            @ApiResponse(responseCode = "403", description = "IDM Service Error: Forbidden"),
            @ApiResponse(responseCode = "500", description = "IDM Service Error: Internal Service Error")
    })
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(IdmServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
