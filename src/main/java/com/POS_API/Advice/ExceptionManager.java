package com.POS_API.Advice;

import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.Advice.Exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(RequestParamWrong.class)
    public ResponseEntity<Map<String, String>> handleRequestParamWrong(RequestParamWrong ex) {
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("message", ex.getMessage());
        errorDetails.put("parameter", ex.getParameter());
        errorDetails.put("invalidValue", ex.getValue());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}