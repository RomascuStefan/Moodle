package com.POS_API.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UniqueKeyException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;

    @Override
    public String getMessage() {
        return String.format("%s already has an entry with %s", resourceName, fieldName);
    }
}
