package com.POS_API.Controller;

import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.PaginatedViewOutOfBoundsException;
import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.StudentDTO;
import com.POS_API.DTO.StudentPatchDTO;
import com.POS_API.DTO.UserDetailDTO;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Service.AuthService;
import com.POS_API.Service.StudentService;
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
@RequestMapping("/api/academia/student")
@CrossOrigin(origins = "${frontend.origin}", exposedHeaders = "Authorization")
public class StudentController {

    private final StudentService studentService;
    private final AuthService authService;

    @Autowired
    public StudentController(StudentService studentService, AuthService authService) {
        this.studentService = studentService;
        this.authService = authService;
    }

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<StudentDTO>>> findAllStudents(
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "10") String items_per_page,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        AuthServiceOuterClass.Role role = authService.verifyRequest(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));

        List<StudentDTO> studentsList = studentService.findAllStudenti();

        int totalItems = studentsList.size();
        int integerItemPerPage = HelperFunctions.stringToInt(items_per_page, "items_per_page");
        int integerPage = HelperFunctions.stringToInt(page, "page");

        if (integerItemPerPage < 0)
            throw new RequestParamWrong("items per page", items_per_page, "cant be negative");
        else if (integerItemPerPage == 0)
            throw new PaginatedViewOutOfBoundsException("Cant display 0 items per page");

        if (integerPage < 0)
            throw new RequestParamWrong("page number", page, "cant be negative");

        int fromIndex = integerPage * integerItemPerPage;
        int toIndex = Math.min(fromIndex + integerItemPerPage, totalItems);

        if (fromIndex >= totalItems) {
            throw new PaginatedViewOutOfBoundsException("Can't provide this many students");
        }

        List<StudentDTO> paginatedStudents = studentsList.subList(fromIndex, toIndex);

        List<EntityModel<StudentDTO>> students = paginatedStudents.stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class)
                                .findStudentById(student.getId(), null))
                                .withSelfRel()
                                .withType("GET"),
                        linkTo(methodOn(StudentController.class)
                                .getDisciplineForStudent(null, null))
                                .withRel("lectures")
                                .withType("GET")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(StudentController.class)
                .findAllStudents(null, null, authorizationHeader))
                .withSelfRel()
                .withType("GET");

        CollectionModel<EntityModel<StudentDTO>> collectionModel = CollectionModel.of(students, selfLink);

        collectionModel.add(
                linkTo(methodOn(StudentController.class)
                        .findAllStudents(Integer.toString(integerPage), Integer.toString(integerItemPerPage), authorizationHeader))
                        .withRel("current_page")
                        .withType("GET")
        );

        if (integerPage > 0) {
            collectionModel.add(
                    linkTo(methodOn(StudentController.class)
                            .findAllStudents(Integer.toString(integerPage - 1), Integer.toString(integerItemPerPage), authorizationHeader))
                            .withRel("previous_page")
                            .withType("GET")
            );
        }

        if (toIndex < totalItems) {
            collectionModel.add(
                    linkTo(methodOn(StudentController.class)
                            .findAllStudents(Integer.toString(integerPage + 1), Integer.toString(integerItemPerPage), authorizationHeader))
                            .withRel("next_page")
                            .withType("GET")
            );
        }

        if (role == ADMIN) {
            collectionModel.add(linkTo(methodOn(StudentController.class)
                    .addStudent(null, null))
                    .withRel("add-student")
                    .withType("POST"));
        }

        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping(value = "/{id}", produces = "application/JSON")
    public ResponseEntity<EntityModel<StudentDTO>> findStudentById(@PathVariable int id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        UserDetailDTO userDetailDTO = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));

        StudentDTO student = studentService.findStudentById(id);

        EntityModel<StudentDTO> studentModel = EntityModel.of(student,
                linkTo(methodOn(StudentController.class)
                        .findStudentById(id, null))
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(StudentController.class)
                        .findAllStudents(null, null, null))
                        .withRel("all-students")
                        .withType("GET"),
                linkTo(methodOn(StudentController.class)
                        .getDisciplineForStudent(null, null))
                        .withRel("lectures")
                        .withType("GET"));

        if (studentService.isSelfReq(id, userDetailDTO.getEmail())) {
            studentModel.add(
                    linkTo(methodOn(StudentController.class)
                            .updateStudent(null, null, null))
                            .withRel("update-student")
                            .withType("PATCH")
            );
        }

        return ResponseEntity.ok(studentModel);
    }

    @GetMapping(value = "/lectures", produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> getDisciplineForStudent
            (@RequestParam(required = false) String userId,
             @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {


        int id;
        UserDetailDTO userDetail = authService.getUserDetail(authorizationHeader, List.of(ADMIN, PROFESOR, STUDENT));
        if (userDetail.getRole() == ADMIN) {
            if (userId == null || userId.trim().isEmpty())
                throw new RequestParamWrong("studentId", "", "no student selected");
            else
                id = HelperFunctions.stringToInt(userId, "student id");
        } else
            id = studentService.findStudentIdByEmail(userDetail.getEmail());

        List<EntityModel<DisciplinaDTO>> lectures = new ArrayList<>();

        if (userDetail.getRole() == ADMIN || (userDetail.getRole() == STUDENT && studentService.isSelfReq(id, userDetail.getEmail()))) {
            lectures = studentService.getDisciplineForStudent(id).stream()
                    .map(disciplinaDTO -> EntityModel.of(disciplinaDTO,
                            linkTo(methodOn(DisciplinaController.class)
                                    .findDisciplinaByCod(disciplinaDTO.getCod(), null))
                                    .withSelfRel()
                                    .withType("GET")))
                    .collect(Collectors.toList());
        }

        if (userDetail.getRole() == PROFESOR) {
            lectures = studentService.getDisciplineForStudentByProfessor(id, userDetail.getEmail()).stream()
                    .map(disciplinaDTO -> EntityModel.of(disciplinaDTO,
                            linkTo(methodOn(DisciplinaController.class)
                                    .findDisciplinaByCod(disciplinaDTO.getCod(), null))
                                    .withSelfRel()
                                    .withType("GET")))
                    .collect(Collectors.toList());
        }


        Link selfLink = linkTo(methodOn(StudentController.class)
                .getDisciplineForStudent(null, null))
                .withSelfRel()
                .withType("GET");
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(lectures, selfLink);

        System.out.println(authorizationHeader);
        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<StudentDTO>> addStudent(
            @RequestBody @Valid StudentDTO studentDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        authService.verifyRequest(authorizationHeader, List.of(ADMIN));

        StudentDTO savedStudent = studentService.addStudent(studentDTO);
        authService.registerUser(savedStudent.getEmail(), studentDTO.getPassword(), "student");


        EntityModel<StudentDTO> studentModel = EntityModel.of(savedStudent,
                linkTo(methodOn(StudentController.class)
                        .findStudentById(savedStudent.getId(), null))
                        .withSelfRel()
                        .withType("POST"),
                linkTo(methodOn(StudentController.class)
                        .findAllStudents(null, null, null))
                        .withRel("all-students")
                        .withType("GET"));
        return ResponseEntity.status(HttpStatus.CREATED).body(studentModel);
    }

    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<EntityModel<StudentDTO>> updateStudent(
            @RequestParam(required = false) String userId,
            @RequestBody @Valid StudentPatchDTO studentPatchDTO,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        UserDetailDTO userDetailDTO = authService.getUserDetail(authorizationHeader, List.of(ADMIN, STUDENT));
        int id;

        if (userDetailDTO.getRole() == ADMIN) {
            if (userId == null || userId.trim().isEmpty()) {
                throw new RequestParamWrong("userId", "", "no student selected");
            } else {
                id = HelperFunctions.stringToInt(userId, "user id");
            }
        } else {
            id = studentService.findStudentIdByEmail(userDetailDTO.getEmail());
        }

        StudentDTO updatedStudent = studentService.patchStudent(id, studentPatchDTO);

        EntityModel<StudentDTO> studentModel = EntityModel.of(
                updatedStudent,
                linkTo(methodOn(StudentController.class)
                        .findStudentById(id, authorizationHeader))
                        .withSelfRel()
                        .withType("PATCH"),
                linkTo(methodOn(StudentController.class)
                        .findAllStudents(null, null, authorizationHeader))
                        .withRel("all-students")
                        .withType("GET")
        );

        if (userDetailDTO.getRole() == ADMIN) {
            studentModel.add(
                    linkTo(methodOn(StudentController.class)
                            .addStudent(null, authorizationHeader))
                            .withRel("add-student")
                            .withType("POST")
            );
        }

        studentModel.add(
                linkTo(methodOn(StudentController.class)
                        .getDisciplineForStudent(String.valueOf(id), authorizationHeader))
                        .withRel("lectures")
                        .withType("GET")
        );

        return ResponseEntity.ok(studentModel);
    }

}
