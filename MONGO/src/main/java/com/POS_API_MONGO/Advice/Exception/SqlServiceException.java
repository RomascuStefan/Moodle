package com.POS_API_MONGO.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class SqlServiceException extends RuntimeException {
    private final HttpStatusCode status;
    private final String responseBody;
}