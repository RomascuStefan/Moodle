package com.POS_API.Controller;

import com.POS_API.Model.Disciplina;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Profesor;
import com.POS_API.Service.DisciplinaService;
import com.POS_API.Service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/academia/profesori")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final DisciplinaService disciplinaService;

    @Autowired
    public ProfesorController(ProfesorService profesorService, DisciplinaService disciplinaService) {
        this.profesorService = profesorService;
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public List<Profesor> findAllProfesori(
            @RequestParam(required = false) String acad_rank,
            @RequestParam(required = false) String nume) {

        List<Profesor> profesori;
        if (nume != null) {
            profesori = profesorService.findProfesoriByName(nume);
        } else {
            profesori = profesorService.findAllProfesori();
        }

        if (acad_rank != null) {
            profesori = profesorService.filterProfesoriByRank(profesori, acad_rank);
        }

        if (profesori == null) {
            //TODO
        }

        return profesori;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Profesor>> findProfesorById(@PathVariable int id) {
        return profesorService.findProfesorById(id) //
                .map(profesor -> EntityModel.of(profesor, //
                        linkTo(methodOn(ProfesorController.class).findProfesorById(profesor.getId())).withSelfRel(), //
                        linkTo(methodOn(ProfesorController.class).findAllProfesori(null,null)).withRel("employees"))) //
                .map(ResponseEntity::ok) //
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/lectures")
    public List<Disciplina> findDisciplinaByProfesorId(@PathVariable int id) {
        return disciplinaService.findDisciplinaByProfesorId(id);
    }
}

