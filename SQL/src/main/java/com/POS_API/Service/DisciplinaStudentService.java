package com.POS_API.Service;

import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Student;
import com.POS_API.Repository.DisciplinaDAO;
import com.POS_API.Repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisciplinaStudentService {
    private final StudentDAO studentRepo;
    private final DisciplinaDAO disciplinaRepo;


    @Autowired
    public DisciplinaStudentService(StudentDAO studentRepo, DisciplinaDAO disciplinaRepo) {
        this.studentRepo = studentRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

    public void verifyData(List<Integer> studentIds, String codMaterie) {
        Disciplina disciplina = disciplinaRepo.findById(codMaterie)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "cod", codMaterie));

        for (Integer studentId : studentIds) {
            Student existingStudent = studentRepo.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "ID", studentId));

            if (existingStudent.getDiscipline().contains(disciplina)) {
                throw new IllegalStateException(String.format(
                        "Studentul cu ID-ul %d este deja inscris la disciplina cu codul %s.", studentId, codMaterie));
            }
        }
    }

    @Transactional
    public void saveData(List<Integer> studentIds, String codMaterie) {
        Disciplina disciplina = disciplinaRepo.findById(codMaterie).get();


        List<Student> students = studentIds.stream()
                .map(studentId -> studentRepo.findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Student", "ID", studentId)))
                .toList();

        disciplina.getStudenti().addAll(students);
        disciplinaRepo.save(disciplina);

        for (Student student : students) {
            student.getDiscipline().add(disciplina);
            studentRepo.save(student);
        }
    }
}
