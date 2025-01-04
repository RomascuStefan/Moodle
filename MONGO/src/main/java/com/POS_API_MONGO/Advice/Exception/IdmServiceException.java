package com.POS_API_MONGO.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public class IdmServiceException extends RuntimeException{
    private final HttpStatusCode status;
    private final String responseBody;
}

