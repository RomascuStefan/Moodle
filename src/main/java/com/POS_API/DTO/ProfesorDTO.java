package com.POS_API.DTO;


import com.POS_API.Model.Disciplina;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfesorDTO {
    @JsonIgnore
    private int id;

    @NotBlank(message = "Numele nu poate fi gol.")
    private String nume;

    @NotBlank(message = "Prenumele nu poate fi gol.")
    private String prenume;

    @NotBlank(message = "Email-ul nu poate fi gol.")
    @Email(message = "Email-ul trebuie să fie valid.")
    private String email;

    @NotBlank(message = "Gradul didactic nu poate fi gol")
    private String gradDidactic;

    @NotBlank(message = "Tipul asocierii nu poate fi nul")
    private String tipAsociere;

    private String afiliere;

    @JsonIgnore
    private List<Disciplina> discipline;
}

