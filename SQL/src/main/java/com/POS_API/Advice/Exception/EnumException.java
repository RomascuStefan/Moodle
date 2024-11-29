package com.POS_API.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnumException extends RuntimeException {

    private final String parameter;
    private final String value;

    @Override
    public String getMessage() {
        return String.format("Invalid value '%s' for parameter '%s'", value, parameter);
    }
}


