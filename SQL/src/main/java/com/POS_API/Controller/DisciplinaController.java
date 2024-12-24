package com.POS_API.Controller;

import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.EnrollResponseDTO;
import com.POS_API.DTO.EnrollStudentsDTO;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Service.DisciplinaService;
import com.POS_API.Service.DisciplinaStudentService;
import com.POS_API.Service.ProfesorService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia/lectures")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;
    private final ProfesorService profesorService;
    private final DisciplinaStudentService disciplinaStudentService;

    @Autowired
    public DisciplinaController(DisciplinaService disciplinaService, ProfesorService profesorService, DisciplinaStudentService disciplinaStudentService) {
        this.disciplinaService = disciplinaService;
        this.profesorService = profesorService;
        this.disciplinaStudentService = disciplinaStudentService;
    }

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> findAllDiscipline(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10") String items_per_page) {

        List<DisciplinaDTO> discipline = disciplinaService.findAllDiscipline();

        if (type != null) {
            discipline = disciplinaService.filterDisciplineByType(discipline, type);
        }

        if (category != null) {
            discipline = disciplinaService.filterDisciplineByCategory(discipline, category);
        }

        int totalItems = discipline.size();
        int integerItemPerPage = HelperFunctions.stringToInt(items_per_page, "items_per_page");
        int integerPage = HelperFunctions.stringToInt(page, "page");

        int fromIndex = Math.min(integerPage * integerItemPerPage, totalItems);
        int toIndex = Math.min(fromIndex + integerItemPerPage, totalItems);

        List<DisciplinaDTO> paginatedDiscipline = discipline.subList(fromIndex, toIndex);

        List<EntityModel<DisciplinaDTO>> disciplineModels = paginatedDiscipline.stream()
                .map(disciplina -> EntityModel.of(disciplina,
                        linkTo(methodOn(DisciplinaController.class)
                                .findDisciplinaByCod(disciplina.getCod()))
                                .withSelfRel()
                                .withType("GET"),
                        linkTo(methodOn(DisciplinaController.class)
                                .findAllDiscipline(null, null, page, items_per_page))
                                .withRel("all-lectures")
                                .withType("GET")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(DisciplinaController.class)
                .findAllDiscipline(type, category, page, items_per_page))
                .withSelfRel()
                .withType("GET");
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplineModels, selfLink);

        collectionModel.add(
                linkTo(methodOn(DisciplinaController.class)
                        .findAllDiscipline(type, category, Integer.toString(integerPage), Integer.toString(integerItemPerPage)))
                        .withRel("current_page")
                        .withType("GET")
        );
        if (fromIndex > 0) {
            collectionModel.add(
                    linkTo(methodOn(DisciplinaController.class)
                            .findAllDiscipline(type, category, Integer.toString(integerPage - 1), Integer.toString(integerItemPerPage)))
                            .withRel("previous_page")
                            .withType("GET")
            );
        }
        if (toIndex < totalItems) {
            collectionModel.add(
                    linkTo(methodOn(DisciplinaController.class)
                            .findAllDiscipline(type, category, Integer.toString(integerPage + 1), Integer.toString(integerItemPerPage)))
                            .withRel("next_page")
                            .withType("GET")
            );
        }

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{cod}", produces = "application/JSON")
    public ResponseEntity<EntityModel<DisciplinaDTO>> findDisciplinaByCod(@PathVariable String cod) {
        DisciplinaDTO disciplina = disciplinaService.findDisciplinaByCod(cod);

        EntityModel<DisciplinaDTO> disciplinaModel = EntityModel.of(disciplina,
                linkTo(methodOn(DisciplinaController.class)
                        .findDisciplinaByCod(cod))
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(DisciplinaController.class)
                        .findAllDiscipline(null, null, null, null))
                        .withRel("all-lectures")
                        .withType("GET"));

        return ResponseEntity.ok(disciplinaModel);
    }

    @PostMapping(value = "/{cod}/enroll", consumes = "application/JSON", produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<EnrollResponseDTO>>> enrollStudents(
            @PathVariable String cod,
            @RequestBody @Valid EnrollStudentsDTO enrollStudentsDTO) {

        disciplinaStudentService.verifyData(enrollStudentsDTO.getStudents(), cod);

        disciplinaStudentService.saveData(enrollStudentsDTO.getStudents(), cod);

        List<EntityModel<EnrollResponseDTO>> enrolledStudentsLinks = enrollStudentsDTO.getStudents().stream()
                .map(studentId -> EntityModel.of(
                        new EnrollResponseDTO(String.format("Studentul cu ID-ul %d a fost înscris cu succes la disciplina %s.", studentId, cod)),
                        linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod))
                                .withRel("discipline-details")
                                .withType("GET"),
                        linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null, null, null))
                                .withRel("all-disciplines")
                                .withType("GET")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EnrollResponseDTO>> response = CollectionModel.of(
                enrolledStudentsLinks,
                linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod))
                        .withRel("discipline-details")
                        .withType("GET"),
                linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null, null, null))
                        .withRel("all-disciplines")
                        .withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<DisciplinaDTO>> addDisciplina(@RequestBody @Valid DisciplinaDTO disciplinaDTO) {
        int titularId = HelperFunctions.stringToInt(disciplinaDTO.getTitularId(), "Titular ID");

        ProfesorDTO titular = profesorService.findProfesorById(titularId);

        DisciplinaDTO savedDisciplina = disciplinaService.addDisciplina(disciplinaDTO, titular);

        EntityModel<DisciplinaDTO> disciplinaModel = EntityModel.of(savedDisciplina,
                linkTo(methodOn(DisciplinaController.class)
                        .findDisciplinaByCod(savedDisciplina.getCod()))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(DisciplinaController.class)
                        .findAllDiscipline(null, null, null, null))
                        .withRel("all-lectures")
                        .withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaModel);
    }

    @GetMapping("/{cod}/exists")
    public ResponseEntity<Boolean> existsByCod(@PathVariable String cod) {
        boolean exists = disciplinaService.existsByCod(cod);
        return ResponseEntity.ok(exists);
    }


}
