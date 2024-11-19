package com.POS_API.Controller;

import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.Service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/academia/lectures")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    @Autowired
    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> findAllDiscipline(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        List<DisciplinaDTO> discipline = disciplinaService.findAllDiscipline();

        if (type != null) {
            discipline = disciplinaService.filterDisciplineByType(discipline, type);
        }

        if (category != null) {
            discipline = disciplinaService.filterDisciplineByCategory(discipline, category);
        }

        List<EntityModel<DisciplinaDTO>> disciplineModels = discipline.stream()
                .map(disciplina -> EntityModel.of(disciplina,
                        linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(disciplina.getCod())).withSelfRel(),
                        linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null)).withRel("all-lectures")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(DisciplinaController.class).findAllDiscipline(type, category)).withSelfRel();
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplineModels, selfLink);

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{cod}")
    public ResponseEntity<EntityModel<DisciplinaDTO>> findDisciplinaByCod(@PathVariable String cod) {
        DisciplinaDTO disciplina = disciplinaService.findDisciplinaByCod(cod);

        EntityModel<DisciplinaDTO> disciplinaModel = EntityModel.of(disciplina,
                linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod)).withSelfRel(),
                linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null)).withRel("all-lectures"));

        return ResponseEntity.ok(disciplinaModel);
    }
}
