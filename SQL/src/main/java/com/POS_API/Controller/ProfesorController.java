package com.POS_API.Controller;

import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Model.Disciplina;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.POS_API.Model.Profesor;
import com.POS_API.Service.DisciplinaService;
import com.POS_API.Service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<ProfesorDTO>>> findAllProfesori(
            @RequestParam(required = false) String acad_rank,
            @RequestParam(required = false) String nume,
            @RequestParam(required = false) String an_studiu,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10") String items_per_page) {

        List<ProfesorDTO> profesori;
        if (nume != null) {
            profesori = profesorService.findProfesoriByName(nume);
        } else {
            profesori = profesorService.findAllProfesori();
        }

        if (acad_rank != null) {
            profesori = profesorService.filterProfesoriByRank(profesori, acad_rank);
        }

        if (an_studiu != null) {
            profesori = profesorService.filterProfesoriByAnStudii(profesori, an_studiu);
        }

        int totalItems = profesori.size();
        int integerItemPerPage = HelperFunctions.stringToInt(items_per_page, "items_per_page");
        int integerPage = HelperFunctions.stringToInt(page, "page");

        int fromIndex = Math.min(integerPage * integerItemPerPage, totalItems);
        int toIndex = Math.min(fromIndex + integerItemPerPage, totalItems);

        List<ProfesorDTO> paginatedProfesori = profesori.subList(fromIndex, toIndex);

        List<EntityModel<ProfesorDTO>> profesorModels = paginatedProfesori.stream()
                .map(profesor -> EntityModel.of(profesor,
                        linkTo(methodOn(ProfesorController.class)
                                .findProfesorById(profesor.getId()))
                                .withSelfRel()
                                .withType("GET"),
                        linkTo(methodOn(ProfesorController.class)
                                .findDisciplinaByProfesorId(profesor.getId()))
                                .withRel("lectures")
                                .withType("GET")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ProfesorController.class)
                .findAllProfesori(acad_rank, nume, an_studiu, page, items_per_page))
                .withSelfRel()
                .withType("GET");
        CollectionModel<EntityModel<ProfesorDTO>> collectionModel = CollectionModel.of(profesorModels, selfLink);

        collectionModel.add(
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage), Integer.toString(integerItemPerPage)))
                        .withRel("current_page")
                        .withType("GET")
        );
        if (fromIndex > 0) {
            collectionModel.add(
                    linkTo(methodOn(ProfesorController.class)
                            .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage - 1), Integer.toString(integerItemPerPage)))
                            .withRel("previous_page")
                            .withType("GET")
            );
        }
        if (toIndex < totalItems) {
            collectionModel.add(
                    linkTo(methodOn(ProfesorController.class)
                            .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage + 1), Integer.toString(integerItemPerPage)))
                            .withRel("next_page")
                            .withType("GET")
            );
        }

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = "application/JSON")
    public ResponseEntity<EntityModel<ProfesorDTO>> findProfesorById(@PathVariable int id) {
        ProfesorDTO profesor = profesorService.findProfesorById(id);

        EntityModel<ProfesorDTO> profesorJSON = EntityModel.of(profesor);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class)
                        .findProfesorById(id))
                .withSelfRel()
                .withType("GET");
        Link findDisciplinaLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class)
                        .findDisciplinaByProfesorId(id))
                .withRel("findDisciplinaByProfesorId")
                .withType("GET");

        profesorJSON.add(selfLink);
        profesorJSON.add(findDisciplinaLink);

        return ResponseEntity.ok(profesorJSON);
    }

    @GetMapping(value = "/{id}/lectures", produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> findDisciplinaByProfesorId(@PathVariable int id) {
        int profId = profesorService.findProfesorById(id).getId();

        List<DisciplinaDTO> discipline = disciplinaService.findDisciplinaByProfesorId(profId);

        List<EntityModel<DisciplinaDTO>> disciplinaModels = discipline.stream()
                .map(disciplinaDTO -> EntityModel.of(disciplinaDTO,
                        linkTo(methodOn(DisciplinaController.class)
                                .findDisciplinaByCod(disciplinaDTO.getCod()))
                                .withSelfRel()
                                .withType("GET")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplinaModels,
                linkTo(methodOn(ProfesorController.class)
                        .findProfesorById(id))
                        .withRel("profesor")
                        .withType("GET"),
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(null, null, null, null, null))
                        .withRel("allProfessors")
                        .withType("GET"));

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<ProfesorDTO>> addProfesor(@RequestBody @Valid ProfesorDTO profesorDTO) {
        ProfesorDTO savedProfesor = profesorService.addProfesor(profesorDTO);

        EntityModel<ProfesorDTO> profesorModel = EntityModel.of(savedProfesor,
                linkTo(methodOn(ProfesorController.class)
                        .findProfesorById(savedProfesor.getId()))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(null, null, null, null, null))
                        .withRel("all-professors")
                        .withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(profesorModel);
    }
}
