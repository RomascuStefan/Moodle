package com.POS_API.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class EnrollStudentsDTO {
    @NotNull(message = "Lista de studenți nu poate fi null")
    private List<Integer> students;
}
