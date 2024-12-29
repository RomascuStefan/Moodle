package com.POS_API_MONGO.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class WrongLocationException extends RuntimeException {

    private final Object fieldValue;

    @Override
    public String getMessage() {
        return String.format("File location not found with '%s'", fieldValue);
    }
}