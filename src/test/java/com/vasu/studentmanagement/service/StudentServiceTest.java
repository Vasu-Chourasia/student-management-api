package com.vasu.studentmanagement.service;

import com.vasu.studentmanagement.dto.StudentRequestDTO;
import com.vasu.studentmanagement.dto.StudentResponseDTO;
import com.vasu.studentmanagement.entity.Student;
import com.vasu.studentmanagement.exception.DuplicateStudentException;
import com.vasu.studentmanagement.exception.StudentNotFoundException;
import com.vasu.studentmanagement.repository.StudentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void shouldAddStudentSuccessfully() {

        StudentRequestDTO request = new StudentRequestDTO();

        request.setRollNumber("24BCS001");
        request.setName("Rahul");
        request.setEmail("rahul@gmail.com");
        request.setPhone("9876543210");
        request.setAge(21);
        request.setDepartment("CSE");

        Student savedStudent = new Student(
                "24BCS001",
                "Rahul",
                "rahul@gmail.com",
                "9876543210",
                21,
                "CSE"
        );

        when(studentRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(studentRepository.existsByRollNumber(request.getRollNumber()))
                .thenReturn(false);

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        StudentResponseDTO response =
                studentService.addStudent(request);

        assertNotNull(response);
        assertEquals("Rahul", response.getName());
        assertEquals("rahul@gmail.com", response.getEmail());

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {

        StudentRequestDTO request = new StudentRequestDTO();

        request.setRollNumber("24BCS002");
        request.setName("Rahul");
        request.setEmail("rahul@gmail.com");
        request.setPhone("9876543210");
        request.setAge(21);
        request.setDepartment("CSE");

        when(studentRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        DuplicateStudentException exception =
                assertThrows(
                        DuplicateStudentException.class,
                        () -> studentService.addStudent(request)
                );

        assertEquals(
                "Email already exists",
                exception.getMessage()
        );

        verify(studentRepository, never())
                .save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> studentService.getStudentById(999L)
                );

        assertEquals(
                "Student with ID 999 not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateStudentSuccessfully() {

        Student existingStudent = new Student(
                "24BCS003",
                "Old Name",
                "old@gmail.com",
                "9876543210",
                21,
                "CSE"
        );

        StudentRequestDTO request = new StudentRequestDTO();

        request.setRollNumber("24BCS003");
        request.setName("New Name");
        request.setEmail("new@gmail.com");
        request.setPhone("9999999999");
        request.setAge(22);
        request.setDepartment("CSE");

        when(studentRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existingStudent));

        when(studentRepository.existsByEmailAndIdNot(
                request.getEmail(), 1L))
                .thenReturn(false);

        when(studentRepository.existsByRollNumberAndIdNot(
                request.getRollNumber(), 1L))
                .thenReturn(false);

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        StudentResponseDTO response =
                studentService.updateStudent(1L, request);

        assertEquals("New Name", response.getName());
        assertEquals("new@gmail.com", response.getEmail());
        assertEquals(22, response.getAge());

        verify(studentRepository).save(existingStudent);
    }

    @Test
    void shouldDeleteStudentSuccessfully() {

        Student student = new Student(
                "24BCS004",
                "Test Student",
                "test@gmail.com",
                "9999999999",
                21,
                "CSE"
        );

        when(studentRepository.findById(1L))
                .thenReturn(java.util.Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }
}