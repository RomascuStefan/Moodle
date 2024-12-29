package com.POS_API.Mapper;

import com.POS_API.Advice.Exception.EnumException;
import com.POS_API.DTO.StudentDTO;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Model.Enums.CicluStudii;
import com.POS_API.Model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentMapper {
    public static Student toEntity(StudentDTO studentDTO) {
        CicluStudii cicluStudii;
        try {
            cicluStudii = CicluStudii.valueOf(studentDTO.getCicluStudii());
        } catch (IllegalArgumentException e) {
            throw new EnumException("ciclu studii", studentDTO.getCicluStudii());
        }

        int anStudiuInt = HelperFunctions.stringToInt(studentDTO.getAnStudiu(),"an studiu");

        Student student = new Student();

        student.setId(studentDTO.getId());
        student.setNume(studentDTO.getNume());
        student.setPrenume(studentDTO.getPrenume());
        student.setEmail(studentDTO.getEmail());
        student.setCicluStudii(cicluStudii);
        student.setAnStudiu(anStudiuInt);
        student.setGrupa(studentDTO.getGrupa());
        student.setDiscipline(studentDTO.getDiscipline());

        return student;
    }

    public static StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setId(student.getId());
        studentDTO.setNume(student.getNume());
        studentDTO.setPrenume(student.getPrenume());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setCicluStudii(student.getCicluStudii().toString());
        studentDTO.setAnStudiu(Integer.toString(student.getAnStudiu()));
        studentDTO.setGrupa(student.getGrupa());
        studentDTO.setDiscipline(student.getDiscipline());

        return studentDTO;
    }

    public static List<Student> listToEntity(List<StudentDTO> studentDTOList) {
        List<Student> studentsList = new ArrayList<>();

        for (StudentDTO studentDTO : studentDTOList) {
            studentsList.add(toEntity(studentDTO));
        }

        return studentsList;
    }

    public static List<StudentDTO> listToDTO(List<Student> studentEntityList) {
        List<StudentDTO> studentsDTOList = new ArrayList<>();

        for (Student student : studentEntityList) {
            studentsDTOList.add(toDTO(student));
        }

        return studentsDTOList;
    }

}
