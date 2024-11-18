package com.POS_API.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestParamWrong extends RuntimeException {

    private final String parameterName;
    private final String parameterValue;
    private final String message;

    @Override
    public String getMessage() {
        return String.format("Invalid value '%s' for parameter '%s': %s", parameterValue, parameterName, message);
    }
}
