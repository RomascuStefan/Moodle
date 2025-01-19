package com.POS_API.Service;

import com.POS_API.Advice.Exception.*;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.StudentDTO;
import com.POS_API.DTO.StudentPatchDTO;
import com.POS_API.Mapper.DisciplinaMapper;
import com.POS_API.Mapper.StudentMapper;
import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Enums.CicluStudii;
import com.POS_API.Model.Student;
import com.POS_API.Repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        Optional<Student> student = studentRepo.findStudentById(id);

        if (student.isEmpty())
            throw new ResourceNotFoundException("Student", "id", id);

        return StudentMapper.toDTO(student.get());
    }


    public List<DisciplinaDTO> getDisciplineForStudent(int id) {
        List<Disciplina> disciplinaList = studentRepo.findById(id)
                .map(Student::getDiscipline)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        return DisciplinaMapper.listToDTO(disciplinaList);
    }

    public List<DisciplinaDTO> getDisciplineForStudentByProfessor(int studentId, String profEmail) {
        List<Disciplina> studentDiscipline = studentRepo.findById(studentId)
                .map(Student::getDiscipline)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        List<Disciplina> filteredDiscipline = studentDiscipline.stream()
                .filter(d -> d.getTitular() != null && d.getTitular().getEmail().equals(profEmail))
                .toList();

        return DisciplinaMapper.listToDTO(filteredDiscipline);
    }

    public StudentDTO addStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);

        if (studentRepo.existsByEmail(student.getEmail())) {
            throw new UniqueKeyException("Studenti", student.getEmail());
        }

        return StudentMapper.toDTO(studentRepo.save(student));
    }

    public boolean isSelfReq(int id, String email) {
        if (studentRepo.existsByIdAndEmail(id, email))
            return true;

        throw new IdmServiceException(HttpStatus.FORBIDDEN, "FORBIDDEN");
    }

    public int findStudentIdByEmail(String email) {
        Optional<Student> student= studentRepo.findStudentByEmail(email);
        if(student.isEmpty())
            throw new UnprocesableEntityException("Not a student");


        return studentRepo.findStudentByEmail(email).get().getId();
    }

    public StudentDTO patchStudent(int id, StudentPatchDTO studentPatchDTO) {
        Optional<Student> optionalStudent = studentRepo.findById(id);

        if (optionalStudent.isEmpty()) {
            throw new ResourceNotFoundException("Student", "id", id);
        }

        Student student = optionalStudent.get();

        if (studentPatchDTO.getNume() != null && !studentPatchDTO.getNume().isEmpty()) {
            student.setNume(studentPatchDTO.getNume());
        }

        if (studentPatchDTO.getPrenume() != null && !studentPatchDTO.getPrenume().isEmpty()) {
            student.setPrenume(studentPatchDTO.getPrenume());
        }

        if (studentPatchDTO.getCicluStudii() != null && !studentPatchDTO.getCicluStudii().isEmpty()) {
            CicluStudii cicluStudii;
            try {
                cicluStudii = CicluStudii.valueOf(studentPatchDTO.getCicluStudii());
                student.setCicluStudii(cicluStudii);
            } catch (IllegalArgumentException e) {
                throw new EnumException("ciclu studii", studentPatchDTO.getCicluStudii());
            }
        }

        if (studentPatchDTO.getAnStudiu() != null && !studentPatchDTO.getAnStudiu().isEmpty()) {
            student.setAnStudiu(Integer.parseInt(studentPatchDTO.getAnStudiu()));
        }

        if (studentPatchDTO.getGrupa() != null && !studentPatchDTO.getGrupa().isEmpty()) {
            student.setGrupa(studentPatchDTO.getGrupa());
        }

        Student updatedStudent = studentRepo.save(student);

        return StudentMapper.toDTO(updatedStudent);
    }
}

