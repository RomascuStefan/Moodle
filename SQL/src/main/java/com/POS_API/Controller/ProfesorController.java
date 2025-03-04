package com.POS_API.Controller;

import com.POS_API.Advice.Exception.PaginatedViewOutOfBoundsException;
import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.DTO.*;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Service.AuthService;
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

import static com.POS_API.Helper.HelperFunctions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia/profesori")
@CrossOrigin(origins = "${frontend.origin}", exposedHeaders = "Authorization")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final DisciplinaService disciplinaService;
    private final AuthService authService;

    @Autowired
    public ProfesorController(ProfesorService profesorService, DisciplinaService disciplinaService, AuthService authService) {
        this.profesorService = profesorService;
        this.disciplinaService = disciplinaService;
        this.authService = authService;
    }

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<ProfesorDTO>>> findAllProfesori(
            @RequestParam(required = false) String acad_rank,
            @RequestParam(required = false) String nume,
            @RequestParam(required = false) String an_studiu,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10") String items_per_page,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        authService.verifyRequest(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));

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

        if (integerItemPerPage < 0)
            throw new RequestParamWrong("items per page", items_per_page, "cant be negative");
        else if(integerItemPerPage == 0)
            throw new PaginatedViewOutOfBoundsException("Cant display 0 items per page");

        if (integerPage < 0)
            throw new RequestParamWrong("page number", page, "cant be negative");

        int fromIndex = Math.min(integerPage * integerItemPerPage, totalItems);
        int toIndex = Math.min(fromIndex + integerItemPerPage, totalItems);

        if (fromIndex >= totalItems) {
            throw new PaginatedViewOutOfBoundsException("Can't provide this many discipline");
        }

        List<ProfesorDTO> paginatedProfesori = profesori.subList(fromIndex, toIndex);

        List<EntityModel<ProfesorDTO>> profesorModels = paginatedProfesori.stream()
                .map(profesor -> EntityModel.of(profesor,
                        linkTo(methodOn(ProfesorController.class)
                                .findProfesorById(profesor.getId(), null))
                                .withSelfRel()
                                .withType("GET"),
                        linkTo(methodOn(ProfesorController.class)
                                .findDisciplinaByProfesorId(null,null))
                                .withRel("lectures")
                                .withType("GET")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ProfesorController.class)
                .findAllProfesori(acad_rank, nume, an_studiu, page, items_per_page, null))
                .withSelfRel()
                .withType("GET");
        CollectionModel<EntityModel<ProfesorDTO>> collectionModel = CollectionModel.of(profesorModels, selfLink);

        collectionModel.add(
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage), Integer.toString(integerItemPerPage), null))
                        .withRel("current_page")
                        .withType("GET")
        );
        if (fromIndex > 0) {
            collectionModel.add(
                    linkTo(methodOn(ProfesorController.class)
                            .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage - 1), Integer.toString(integerItemPerPage), null))
                            .withRel("previous_page")
                            .withType("GET")
            );
        }
        if (toIndex < totalItems) {
            collectionModel.add(
                    linkTo(methodOn(ProfesorController.class)
                            .findAllProfesori(acad_rank, nume, an_studiu, Integer.toString(integerPage + 1), Integer.toString(integerItemPerPage), null))
                            .withRel("next_page")
                            .withType("GET")
            );
        }

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = "application/JSON")
    public ResponseEntity<EntityModel<ProfesorDTO>> findProfesorById(@PathVariable int id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        UserDetailDTO userDetailDTO = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));


        ProfesorDTO profesor = profesorService.findProfesorById(id);

        EntityModel<ProfesorDTO> profesorJSON = EntityModel.of(profesor);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class)
                        .findProfesorById(id, null))
                .withSelfRel()
                .withType("GET");
        Link findDisciplinaLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class)
                        .findDisciplinaByProfesorId(null,null))
                .withRel("findDisciplinaByProfesorId")
                .withType("GET");

        profesorJSON.add(selfLink);
        profesorJSON.add(findDisciplinaLink);

        if(profesorService.existByEmailAndId(userDetailDTO.getEmail(), profesor.getId()))
        {
            Link patchProfesor = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesorController.class)
                            .updateProfesor(null,null,null))
                    .withRel("patchProfesor")
                    .withType("PATCH");
            profesorJSON.add(patchProfesor);
        }

        return ResponseEntity.ok(profesorJSON);
    }

    @GetMapping(value = "/lectures", produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> findDisciplinaByProfesorId(@RequestParam(required = false) String userId, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        UserDetailDTO userDetailDTO = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));

        int id;
        if (userDetailDTO.getRole() == ADMIN) {
            if (userId == null || userId.trim().isEmpty())
                throw new RequestParamWrong("", "profId", "no professor selected");
            else
                id = HelperFunctions.stringToInt(userId, "profesor id");
        } else
            id = profesorService.findProfesorByEmail(userDetailDTO.getEmail()).getId();

        List<DisciplinaDTO> discipline = disciplinaService.findDisciplinaByProfesorId(id);

        List<EntityModel<DisciplinaDTO>> disciplinaModels = discipline.stream()
                .map(disciplinaDTO -> EntityModel.of(disciplinaDTO,
                        linkTo(methodOn(DisciplinaController.class)
                                .findDisciplinaByCod(disciplinaDTO.getCod(), null))
                                .withSelfRel()
                                .withType("GET")))

                .collect(Collectors.toList());

        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(disciplinaModels,
                linkTo(methodOn(ProfesorController.class)
                        .findProfesorById(id, null))
                        .withRel("profesor")
                        .withType("GET"),
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(null, null, null, null, null, null))
                        .withRel("allProfessors")
                        .withType("GET"));

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<ProfesorDTO>> addProfesor(
            @RequestBody @Valid ProfesorDTO profesorDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {


        authService.verifyRequest(authorizationHeader, List.of(ADMIN));

        ProfesorDTO savedProfesor = profesorService.addProfesor(profesorDTO);
        authService.registerUser(savedProfesor.getEmail(), profesorDTO.getPassword(), "profesor");

        EntityModel<ProfesorDTO> profesorModel = EntityModel.of(savedProfesor,
                linkTo(methodOn(ProfesorController.class)
                        .findProfesorById(savedProfesor.getId(), null))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(null, null, null, null, null, null))
                        .withRel("all-professors")
                        .withType("GET"));

        return ResponseEntity.status(HttpStatus.CREATED).body(profesorModel);
    }


    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<EntityModel<ProfesorDTO>> updateProfesor(
            @RequestParam(required = false) String userId,
            @RequestBody @Valid ProfesorPatchDTO profesorPatchDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        UserDetailDTO userDetailDTO = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR));

        int id;

        if (userDetailDTO.getRole() == ADMIN) {
            if (userId == null || userId.trim().isEmpty()) {
                throw new RequestParamWrong("userId", "", "no profesor selected");
            } else {
                id = HelperFunctions.stringToInt(userId, "user id");
            }
        } else {
            id = profesorService.findProfesorByEmail(userDetailDTO.getEmail()).getId();
        }

        ProfesorDTO updatedProfesor = profesorService.patchProfesor(id, profesorPatchDTO);

        EntityModel<ProfesorDTO> profesorModel = EntityModel.of(updatedProfesor,
                linkTo(methodOn(ProfesorController.class)
                        .findProfesorById(id, null))
                        .withSelfRel()
                        .withType("PATCH"),
                linkTo(methodOn(ProfesorController.class)
                        .findAllProfesori(null, null, null, null, null, null))
                        .withRel("all-professors")
                        .withType("GET"),
                linkTo(methodOn(ProfesorController.class)
                        .findDisciplinaByProfesorId(Integer.toString(id), null))
                        .withRel("lectures")
                        .withType("GET"));

        if (userDetailDTO.getRole() == ADMIN) {
            profesorModel.add(linkTo(methodOn(ProfesorController.class)
                    .addProfesor(null, null))
                    .withRel("add-profesor")
                    .withType("POST"));
        }

        return ResponseEntity.ok(profesorModel);
    }


}
