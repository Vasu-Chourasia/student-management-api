package com.vasu.studentmanagement.service;

import com.vasu.studentmanagement.entity.Student;
import com.vasu.studentmanagement.repository.StudentRepository;
import com.vasu.studentmanagement.exception.StudentNotFoundException;
import com.vasu.studentmanagement.exception.DuplicateStudentException;

import com.vasu.studentmanagement.dto.StudentRequestDTO;
import com.vasu.studentmanagement.dto.StudentResponseDTO;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vasu.studentmanagement.specification.StudentSpecification;

@Service
public class    StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponseDTO addStudent(StudentRequestDTO request) {


        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateStudentException(
                    "Email already exists"
            );
        }

        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new DuplicateStudentException(
                    "Roll number already exists"
            );
        }

        Student student = new Student();

        student.setRollNumber(request.getRollNumber());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setAge(request.getAge());
        student.setDepartment(request.getDepartment());

        Student savedStudent = studentRepository.save(student);

        return new StudentResponseDTO(
                savedStudent.getId(),
                savedStudent.getRollNumber(),
                savedStudent.getName(),
                savedStudent.getEmail(),
                savedStudent.getPhone(),
                savedStudent.getAge(),
                savedStudent.getDepartment()
        );
    }

    public Page<StudentResponseDTO> getAllStudents(
            String department,
            String name,
            Integer age,
            Integer minAge,
            Integer maxAge,
            Pageable pageable) {

        Specification<Student> specification = null;

        if (department != null && !department.isBlank()) {
            specification =
                    StudentSpecification.hasDepartment(department);
        }

        if (name != null && !name.isBlank()) {

            Specification<Student> nameSpecification =
                    StudentSpecification.hasName(name);

            if (specification == null) {
                specification = nameSpecification;
            } else {
                specification = specification.and(nameSpecification);
            }
        }

        if (age != null) {

            Specification<Student> ageSpecification =
                    StudentSpecification.hasAge(age);

            if (specification == null) {
                specification = ageSpecification;
            } else {
                specification = specification.and(ageSpecification);
            }
        }

        if (minAge != null) {

            Specification<Student> minAgeSpecification =
                    StudentSpecification.hasMinAge(minAge);

            if (specification == null) {
                specification = minAgeSpecification;
            } else {
                specification = specification.and(minAgeSpecification);
            }
        }

        if (maxAge != null) {

            Specification<Student> maxAgeSpecification =
                    StudentSpecification.hasMaxAge(maxAge);

            if (specification == null) {
                specification = maxAgeSpecification;
            } else {
                specification = specification.and(maxAgeSpecification);
            }
        }

        Page<Student> students;

        if (specification != null) {
            students = studentRepository.findAll(
                    specification,
                    pageable
            );
        } else {
            students = studentRepository.findAll(pageable);
        }

        return students.map(student -> new StudentResponseDTO(
                student.getId(),
                student.getRollNumber(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getAge(),
                student.getDepartment()
        ));
    }

    public StudentResponseDTO getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        return new StudentResponseDTO(
                student.getId(),
                student.getRollNumber(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getAge(),
                student.getDepartment()
        );
    }

    public StudentResponseDTO updateStudent(
            Long id,
            StudentRequestDTO request) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        if (studentRepository.existsByEmailAndIdNot(
                request.getEmail(), id)) {

            throw new DuplicateStudentException(
                    "Email already exists"
            );
        }

        if (studentRepository.existsByRollNumberAndIdNot(
                request.getRollNumber(), id)) {

            throw new DuplicateStudentException(
                    "Roll number already exists"
            );
        }

        existingStudent.setRollNumber(request.getRollNumber());
        existingStudent.setName(request.getName());
        existingStudent.setEmail(request.getEmail());
        existingStudent.setPhone(request.getPhone());
        existingStudent.setAge(request.getAge());
        existingStudent.setDepartment(request.getDepartment());

        Student updatedStudent =
                studentRepository.save(existingStudent);

        return new StudentResponseDTO(
                updatedStudent.getId(),
                updatedStudent.getRollNumber(),
                updatedStudent.getName(),
                updatedStudent.getEmail(),
                updatedStudent.getPhone(),
                updatedStudent.getAge(),
                updatedStudent.getDepartment()
        );
    }

    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        studentRepository.delete(student);
    }
}