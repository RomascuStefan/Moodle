package com.POS_API.Controller;

import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.StudentDTO;
import com.POS_API.Model.Disciplina;
import com.POS_API.Service.StudentService;
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
@RequestMapping("/api/academia/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<StudentDTO>>> findAllStudents() {
        List<EntityModel<StudentDTO>> students = studentService.findAllStudenti().stream()
                .map(student -> EntityModel.of(student,
                        linkTo(methodOn(StudentController.class).findStudentById(student.getId())).withSelfRel(),
                        linkTo(methodOn(StudentController.class).getDisciplineForStudent(student.getId())).withRel("lectures")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(StudentController.class).findAllStudents()).withSelfRel();
        CollectionModel<EntityModel<StudentDTO>> collectionModel = CollectionModel.of(students, selfLink);

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}",produces = "application/JSON")
    public ResponseEntity<EntityModel<StudentDTO>> findStudentById(@PathVariable int id) {
        StudentDTO student = studentService.findStudentById(id);

        EntityModel<StudentDTO> studentModel = EntityModel.of(student,
                linkTo(methodOn(StudentController.class).findStudentById(id)).withSelfRel(),
                linkTo(methodOn(StudentController.class).findAllStudents()).withRel("all-students"),
                linkTo(methodOn(StudentController.class).getDisciplineForStudent(id)).withRel("lectures"));

        return ResponseEntity.ok(studentModel);
    }

    @GetMapping(value = "/{id}/lectures",produces = "application/JSON")
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> getDisciplineForStudent(@PathVariable int id) {

        List<EntityModel<DisciplinaDTO>> lectures = studentService.getDisciplineForStudent(id).stream()
                .map(disciplinaDTO -> EntityModel.of(disciplinaDTO,
                        linkTo(methodOn(DisciplinaController.class).findDisciplinaByCod(disciplinaDTO.getCod())).withSelfRel()))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(StudentController.class).getDisciplineForStudent(id)).withSelfRel();
        CollectionModel<EntityModel<DisciplinaDTO>> collectionModel = CollectionModel.of(lectures, selfLink);

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping(produces = "application/JSON", consumes = "application/JSON")
    public ResponseEntity<EntityModel<StudentDTO>> addStudent(@RequestBody @Valid StudentDTO studentDTO) {
        StudentDTO savedStudent = studentService.addStudent(studentDTO);

        EntityModel<StudentDTO> studentModel = EntityModel.of(savedStudent,
                linkTo(methodOn(StudentController.class).findStudentById(savedStudent.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).findAllStudents()).withRel("all-students"));

        return ResponseEntity.status(HttpStatus.CREATED).body(studentModel);
    }
}
