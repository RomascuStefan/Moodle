package com.POS_API.DTO;

import com.POS_API.Model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DisciplinaDTO {

    @JsonIgnore
    private String cod;

    @NotBlank(message = "Disciplina trebuie sa aiba titular")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String titularId;

    private String numeTitular;

    @NotBlank(message = "Numele disciplinei nu poate sa fie gol")
    private String numeDisciplina;

    @NotBlank(message = "Disciplina trebuie sa aiba un an de studiu")
    private String anStudiu;

    @NotBlank(message = "Tipul disciplinei nu poate sa fie gol")
    private String tipDisciplina;

    @NotBlank(message = "Categoria disciplinei nu poate sa fie gol")
    private String categorieDisciplina;

    @NotBlank(message = "Tipul examinarii nu poate sa fie gol")
    private String tipExaminare;

    @JsonIgnore
    private List<Student> studenti;

}
