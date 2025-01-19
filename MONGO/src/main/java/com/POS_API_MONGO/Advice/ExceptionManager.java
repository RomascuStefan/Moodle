package com.POS_API_MONGO.Advice;

import com.POS_API_MONGO.Advice.Exception.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Resource Not Found")
    })
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UniqueKeyException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Conflict: Duplicate Entry Detected")
    })
    public ResponseEntity<Object> handleUniqueKeyException(UniqueKeyException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WrongLocationException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid File Location")
    })
    public ResponseEntity<Object> handleWrongLocationException(WrongLocationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidPonderiException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity: Invalid Grading Weights")
    })
    public ResponseEntity<Object> handleInvalidPonderiException(InvalidPonderiException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("error", "Unprocessable Entity");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(IdmServiceException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized: IDM Service Error"),
            @ApiResponse(responseCode = "403", description = "Forbidden: IDM Service Error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: IDM Service Error")
    })
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(IdmServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(SqlServiceException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Conflict: SQL Service Error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: SQL Service Error")
    })
    public ResponseEntity<Map<String, Object>> handleIdmServiceException(SqlServiceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("details", ex.getResponseBody());
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(FileNameExistsException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Conflict: File Already Exists")
    })
    public ResponseEntity<Object> handleFileNameExistsException(FileNameExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", String.format("Fisierul '%s' exista deja în locatia '%s'.", ex.getFilename(), ex.getLocation()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "Internal Server Error: An unexpected error occurred")
    })
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
