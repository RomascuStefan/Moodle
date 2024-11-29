package com.POS_API.Model;

import com.POS_API.Model.Enums.CicluStudii;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Entity
@Table(name = "studenti")
@Getter
@Setter
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String nume;

    @Column(nullable = false, length = 50)
    private String prenume;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ciclu_studii", nullable = false)
    private CicluStudii cicluStudii;

    @Column(name = "an_studiu", nullable = false)
    private int anStudiu;

    @Column(nullable = false)
    private String grupa;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "join_ds",
            joinColumns = @JoinColumn(name = "StudentID"),
            inverseJoinColumns = @JoinColumn(name = "DisciplinaID")
    )
    private List<Disciplina> discipline;
}
