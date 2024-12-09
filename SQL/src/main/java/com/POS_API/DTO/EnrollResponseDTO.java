package com.POS_API.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//clasa pentru HATEOAS
// nu pot folosi doar un string pentru ca EntityModel nu lucreaza cu stringuri

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollResponseDTO {
    private String message;
}

