package com.POS_API.Controller;

import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Student;
import com.POS_API.Service.DisciplinaService;
import com.POS_API.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/academia/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> findAllStudents() {
        return studentService.findAllStudenti();
    }

    @GetMapping("/{id}")
    public Optional<Student> findStudentById(@PathVariable int id) {
        return studentService.findStudentById(id);
    }

    @GetMapping("/{id}/lectures")
    public List<Disciplina> getDisciplineForStudent(@PathVariable int id){
        return studentService.getDisciplineForStudent(id);
    }

}

