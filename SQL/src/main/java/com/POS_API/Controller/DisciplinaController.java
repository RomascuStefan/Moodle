package com.POS_API.Controller;

import auth.AuthServiceOuterClass;
import com.POS_API.DTO.*;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Service.AuthService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.POS_API.Helper.HelperFunctions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia/lectures")
@CrossOrigin(origins = "${frontend.origin}", exposedHeaders = "Authorization")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;
    private final ProfesorService profesorService;
    private final DisciplinaStudentService disciplinaStudentService;
    private final AuthService authService;


    @Autowired
    public DisciplinaController(DisciplinaService disciplinaService, ProfesorService profesorService, DisciplinaStudentService disciplinaStudentService, AuthService authService) {
        this.disciplinaService = disciplinaService;
        this.profesorService = profesorService;
        this.disciplinaStudentService = disciplinaStudentService;
        this.authService = authService;
    }

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> findAllDiscipline(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10") String items_per_page,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {


        AuthServiceOuterClass.Role role = authService.verifyRequest(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));

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
                .map(disciplina -> {
                    List<Link> links = new ArrayList<>();

                    links.add(linkTo(methodOn(DisciplinaController.class)
                            .findDisciplinaByCod(disciplina.getCod(), null))
                            .withSelfRel().withType("GET"));


                    if (role == ADMIN) {
                        links.add(linkTo(methodOn(DisciplinaController.class)
                                .enrollStudents(disciplina.getCod(), null, null))
                                .withRel("enroll-students").withType("POST"));
                    }

                    if (role == PROFESOR) {
                        links.add(linkTo(methodOn(DisciplinaController.class)
                                .enrollStudents(disciplina.getCod(), null, null))
                                .withRel("enroll-students").withType("POST"));
                    }

                    return EntityModel.of(disciplina, links);
                })
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(DisciplinaController.class)
                .findAllDiscipline(type, category, page, items_per_page, null))
                .withSelfRel()
                .withType("GET");
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplineModels, selfLink);

        collectionModel.add(
                linkTo(methodOn(DisciplinaController.class)
                        .findAllDiscipline(type, category, Integer.toString(integerPage), Integer.toString(integerItemPerPage), null))
                        .withRel("current_page")
                        .withType("GET")
        );
        if (role == ADMIN) {
            collectionModel.add(linkTo(methodOn(DisciplinaController.class)
                    .addDisciplina(null, null))
                    .withRel("add-disciplina").withType("POST"));
        }
        if (role == STUDENT) {
            collectionModel.add(linkTo(methodOn(StudentController.class)
                    .getDisciplineForStudent(null, null))
                    .withRel("view-your-discipline").withType("GET"));
        }
        if (role == PROFESOR) {
            collectionModel.add(linkTo(methodOn(ProfesorController.class)
                    .findDisciplinaByProfesorId(null, null))
                    .withRel("view-your-discipline").withType("GET"));
        }
        if (fromIndex > 0) {
            collectionModel.add(
                    linkTo(methodOn(DisciplinaController.class)
                            .findAllDiscipline(type, category, Integer.toString(integerPage - 1), Integer.toString(integerItemPerPage), null))
                            .withRel("previous_page")
                            .withType("GET")
            );
        }
        if (toIndex < totalItems) {
            collectionModel.add(
                    linkTo(methodOn(DisciplinaController.class)
                            .findAllDiscipline(type, category, Integer.toString(integerPage + 1), Integer.toString(integerItemPerPage), null))
                            .withRel("next_page")
                            .withType("GET")
            );
        }

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{cod}", produces = "application/JSON")
    public ResponseEntity<EntityModel<DisciplinaDTO>> findDisciplinaByCod(@PathVariable String cod, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        UserDetailDTO userDetail = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));


        DisciplinaDTO disciplina = disciplinaService.findDisciplinaByCod(cod);
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod, null)).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null, null, null, null)).withRel("all-disciplines").withType("GET"));


        if (userDetail.getRole() == PROFESOR && disciplinaService.isTeaching(cod, userDetail.getEmail())) {
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/upload_file".replace("{codMaterie}", cod)).withRel("upload-files").withType("POST"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/download_file".replace("{codMaterie}", cod)).withRel("download-files").withType("GET"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/files".replace("{codMaterie}", cod)).withRel("get-file-names").withType("GET"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/grading".replace("{codMaterie}", cod)).withRel("update-grades").withType("PUT"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/grading".replace("{codMaterie}", cod)).withRel("get-grades").withType("GET"));
            links.add(linkTo(methodOn(DisciplinaController.class).enrollStudents(cod, null, null)).withRel("enroll-students").withType("POST"));
        }

        if (userDetail.getRole() == STUDENT && disciplinaService.isAttending(cod, userDetail.getEmail())) {
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/download_file".replace("{codMaterie}", cod)).withRel("download-files").withType("GET"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/files".replace("{codMaterie}", cod)).withRel("get-file-names").withType("GET"));
            links.add(Link.of("http://localhost:8081/api/academia_mongo/{codMaterie}/grading".replace("{codMaterie}", cod)).withRel("get-grades").withType("GET"));
        }

        return ResponseEntity.ok(EntityModel.of(disciplina, links));
    }


    @PostMapping(value = "/{cod}/students", consumes = "application/JSON", produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<EnrollResponseDTO>>> enrollStudents(
            @PathVariable String cod,
            @RequestBody @Valid EnrollStudentsDTO enrollStudentsDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        authService.verifyRequest(authorizationHeader, List.of(ADMIN, PROFESOR));

        disciplinaStudentService.verifyData(enrollStudentsDTO.getStudents(), cod);
        disciplinaStudentService.saveData(enrollStudentsDTO.getStudents(), cod);

        List<EntityModel<EnrollResponseDTO>> enrolledStudentsLinks = enrollStudentsDTO.getStudents().stream()
                .map(studentId -> EntityModel.of(
                        new EnrollResponseDTO(String.format("Studentul cu ID-ul %d a fost înscris cu succes la disciplina %s.", studentId, cod)),
                        linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod, null)).withRel("discipline-details").withType("GET"),
                        linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null, null, null, null)).withRel("all-disciplines").withType("GET")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EnrollResponseDTO>> response = CollectionModel.of(
                enrolledStudentsLinks,
                linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(cod, null)).withRel("discipline-details").withType("GET"),
                linkTo(methodOn(DisciplinaController.class).findAllDiscipline(null, null, null, null, null)).withRel("all-disciplines").withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<DisciplinaDTO>> addDisciplina(
            @RequestBody @Valid DisciplinaDTO disciplinaDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {


        authService.verifyRequest(authorizationHeader, List.of(ADMIN));

        int titularId = HelperFunctions.stringToInt(disciplinaDTO.getTitularId(), "Titular ID");

        ProfesorDTO titular = profesorService.findProfesorById(titularId);

        String header = authService.getHeader();

        DisciplinaDTO savedDisciplina = disciplinaService.addDisciplina(header, disciplinaDTO, titular);

        EntityModel<DisciplinaDTO> disciplinaModel = EntityModel.of(savedDisciplina,
                linkTo(methodOn(DisciplinaController.class)
                        .findDisciplinaByCod(savedDisciplina.getCod(), null))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(DisciplinaController.class)
                        .findAllDiscipline(null, null, null, null, null))
                        .withRel("all-lectures")
                        .withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaModel);
    }
}
