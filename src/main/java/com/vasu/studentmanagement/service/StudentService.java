package com.vasu.studentmanagement.service;

import com.vasu.studentmanagement.entity.Student;
import com.vasu.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.vasu.studentmanagement.exception.StudentNotFoundException;

import java.util.List;

@Service
public class    StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id).orElse(null);

        if (existingStudent == null) {
            return null;
        }

        existingStudent.setRollNumber(student.getRollNumber());
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setAge(student.getAge());
        existingStudent.setDepartment(student.getDepartment());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}