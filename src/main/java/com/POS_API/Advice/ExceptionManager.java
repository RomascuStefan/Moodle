package com.POS_API.Advice;

import com.POS_API.Advice.Exception.EnumException;
import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Advice.Exception.UniqueKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("message", ex.getMessage());
        errorDetails.put("resource", ex.getResourceName());
        errorDetails.put("field", ex.getFieldName());
        errorDetails.put("value", ex.getFieldValue().toString());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EnumException.class)
    public ResponseEntity<Map<String, String>> handleRequestParamWrong(EnumException ex) {
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("message", ex.getMessage());
        errorDetails.put("parameter", ex.getParameter());
        errorDetails.put("invalidValue", ex.getValue());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UniqueKeyException.class)
    public  ResponseEntity<Map<String, String>> handleUniqueKeyException (UniqueKeyException ex){
        Map<String,String> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("invalidValue", ex.getFieldName());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }

}