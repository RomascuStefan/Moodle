package com.POS_API.Service;

import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Advice.Exception.UniqueKeyException;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.StudentDTO;
import com.POS_API.Mapper.DisciplinaMapper;
import com.POS_API.Mapper.ProfesorMapper;
import com.POS_API.Mapper.StudentMapper;
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

    public List<StudentDTO> findAllStudenti() {
        List<Student> studenti = studentRepo.findAll();

        return StudentMapper.listToDTO(studenti);
    }

    public StudentDTO findStudentById(int id) {
        Optional<Student> student= studentRepo.findStudentById(id);

        if(student.isEmpty())
            throw new ResourceNotFoundException("Student","id",id);

        return StudentMapper.toDTO(student.get());
    }


    public List<DisciplinaDTO> getDisciplineForStudent(int id) {
        List<Disciplina> disciplinaList = studentRepo.findById(id)
                .map(Student::getDiscipline)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        return DisciplinaMapper.listToDTO(disciplinaList);
    }

    public StudentDTO addStudent(StudentDTO studentDTO)
    {
        Student student = StudentMapper.toEntity(studentDTO);

        if(studentRepo.existsByEmail(student.getEmail())){
            throw new UniqueKeyException("Studenti", student.getEmail());
        }

        return StudentMapper.toDTO(studentRepo.save(student));
    }

}

