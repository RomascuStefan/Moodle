package com.POS_API_MONGO.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteFileResponseDTO {
    private String fileName;
    private String location;

    public String getMessage() {
        return String.format("Removed '%s' from '%s'", fileName, location);
    }
}

