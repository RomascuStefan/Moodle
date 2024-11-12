package com.POS_API.Controller;

import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Model.Disciplina;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Profesor;
import com.POS_API.Service.DisciplinaService;
import com.POS_API.Service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public ResponseEntity<CollectionModel<EntityModel<Profesor>>> findAllProfesori(
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

        List<EntityModel<Profesor>> profesorModels = profesori.stream()
                .map(profesor -> EntityModel.of(profesor,
                        linkTo(methodOn(ProfesorController.class).findProfesorById(profesor.getId())).withSelfRel(),
                        linkTo(methodOn(ProfesorController.class).findDisciplinaByProfesorId(profesor.getId())).withRel("lectures")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ProfesorController.class).findAllProfesori(acad_rank, nume)).withSelfRel();
        CollectionModel<EntityModel<Profesor>> collectionModel = CollectionModel.of(profesorModels, selfLink);

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Profesor>> findProfesorById(@PathVariable int id) {
        Profesor profesor = profesorService.findProfesorById(id);

        EntityModel<Profesor> profesorJSON = EntityModel.of(profesor);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class).findProfesorById(id)).withSelfRel();
        Link findDisciplinaLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class).findDisciplinaByProfesorId(id)).withRel("findDisciplinaByProfesorId");

        profesorJSON.add(selfLink);
        profesorJSON.add(findDisciplinaLink);

        return ResponseEntity.ok(profesorJSON);
    }

    @GetMapping("/{id}/lectures")
    public ResponseEntity<CollectionModel<EntityModel<Disciplina>>> findDisciplinaByProfesorId(@PathVariable int id) {
        int profId = profesorService.findProfesorById(id).getId();

        List<Disciplina> discipline = disciplinaService.findDisciplinaByProfesorId(profId);

        List<EntityModel<Disciplina>> disciplinaModels = discipline.stream()
                .map(disciplina -> EntityModel.of(disciplina,
                        linkTo(methodOn(ProfesorController.class).findDisciplinaByProfesorId(id)).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Disciplina>> collectionModel = CollectionModel.of(disciplinaModels,
                linkTo(methodOn(ProfesorController.class).findProfesorById(id)).withRel("profesor"),
                linkTo(methodOn(ProfesorController.class).findAllProfesori(null, null)).withRel("allProfessors"));

        return ResponseEntity.ok(collectionModel);
    }
}

