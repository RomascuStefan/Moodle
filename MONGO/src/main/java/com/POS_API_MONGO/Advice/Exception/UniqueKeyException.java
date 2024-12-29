package com.POS_API_MONGO.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UniqueKeyException extends RuntimeException {
    private final String fieldName;

    @Override
    public String getMessage() {
        return String.format("%s already exists.",fieldName);
    }
}
