package com.POS_API.Service;

import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Student;
import com.POS_API.Repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentDAO studentRepo;

    @Autowired
    public StudentService(StudentDAO studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<Student> findAllStudenti() {
        return studentRepo.findAll();
    }

    public Optional<Student> findStudentById(int id) {
        return studentRepo.findStudentById(id);
    }

    public List<Disciplina> getDisciplineForStudent(int id) {
        return studentRepo.findById(id)
                .map(Student::getDiscipline) // Obține lista de discipline ale studentului
                .orElse(Collections.emptyList()); // Dacă studentul nu există, returnează o listă goală
    }

}

