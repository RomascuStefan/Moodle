package com.POS_API.DTO;

import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Enums.CicluStudii;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDTO {

    @JsonIgnore
    private int id;

    @NotBlank(message = "Numele nu poate fi gol.")
    private String nume;

    @NotBlank(message = "Prenumele nu poate fi gol.")
    private String prenume;

    @NotBlank(message = "Email-ul nu poate fi gol.")
    @Email(message = "Email-ul trebuie să fie valid.")
    private String email;

    private String cicluStudii;

    private int anStudiu;

    private String grupa;

    @JsonIgnore
    private List<Disciplina> discipline;

}
