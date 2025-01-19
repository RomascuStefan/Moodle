package com.POS_API.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnprocesableEntityException extends RuntimeException{
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}
