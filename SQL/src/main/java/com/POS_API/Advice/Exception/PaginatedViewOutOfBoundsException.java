package com.POS_API.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginatedViewOutOfBoundsException extends RuntimeException{
    private String message;
}
