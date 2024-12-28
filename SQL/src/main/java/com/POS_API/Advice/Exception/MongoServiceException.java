package com.POS_API.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class MongoServiceException extends RuntimeException {
    private final HttpStatusCode status;
    private final String responseBody;
}
