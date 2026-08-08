package com.vasu.studentmanagement.controller;

import tools.jackson.databind.json.JsonMapper;

import com.vasu.studentmanagement.dto.StudentRequestDTO;
import com.vasu.studentmanagement.dto.StudentResponseDTO;
import com.vasu.studentmanagement.exception.DuplicateStudentException;
import com.vasu.studentmanagement.exception.StudentNotFoundException;
import com.vasu.studentmanagement.service.StudentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void shouldGetStudentById() throws Exception {

        StudentResponseDTO response =
                new StudentResponseDTO(
                        1L,
                        "24BCS001",
                        "Vasu",
                        "vasu@gmail.com",
                        "9876543210",
                        21,
                        "CSE"
                );

        when(studentService.getStudentById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/students/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vasu"))
                .andExpect(jsonPath("$.email")
                        .value("vasu@gmail.com"));
    }

    @Test
    void shouldAddStudent() throws Exception {

        StudentRequestDTO request =
                new StudentRequestDTO();

        request.setRollNumber("24BCS001");
        request.setName("Vasu");
        request.setEmail("vasu@gmail.com");
        request.setPhone("9876543210");
        request.setAge(21);
        request.setDepartment("CSE");

        StudentResponseDTO response =
                new StudentResponseDTO(
                        1L,
                        "24BCS001",
                        "Vasu",
                        "vasu@gmail.com",
                        "9876543210",
                        21,
                        "CSE"
                );

        when(studentService.addStudent(
                any(StudentRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vasu"))
                .andExpect(jsonPath("$.department").value("CSE"));
    }

    @Test
    void shouldRejectInvalidStudent() throws Exception {

        StudentRequestDTO request =
                new StudentRequestDTO();

        request.setRollNumber("");
        request.setName("");
        request.setEmail("invalid-email");
        request.setPhone("9876543210");
        request.setAge(10);
        request.setDepartment("");

        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateStudent() throws Exception {

        StudentRequestDTO request =
                new StudentRequestDTO();

        request.setRollNumber("24BCS001");
        request.setName("Vasu Updated");
        request.setEmail("vasu.updated@gmail.com");
        request.setPhone("9876543210");
        request.setAge(22);
        request.setDepartment("CSE");

        StudentResponseDTO response =
                new StudentResponseDTO(
                        1L,
                        "24BCS001",
                        "Vasu Updated",
                        "vasu.updated@gmail.com",
                        "9876543210",
                        22,
                        "CSE"
                );

        when(studentService.updateStudent(
                eq(1L),
                any(StudentRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/students/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vasu Updated"))
                .andExpect(jsonPath("$.email")
                        .value("vasu.updated@gmail.com"))
                .andExpect(jsonPath("$.age").value(22));
    }

    @Test
    void shouldDeleteStudent() throws Exception {

        mockMvc.perform(
                        delete("/students/1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {

        when(studentService.getStudentById(999L))
                .thenThrow(
                        new StudentNotFoundException(
                                "Student with ID 999 not found"
                        )
                );

        mockMvc.perform(
                        get("/students/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Student with ID 999 not found"));
    }

    @Test
    void shouldReturn409ForDuplicateEmail() throws Exception {

        StudentRequestDTO request =
                new StudentRequestDTO();

        request.setRollNumber("24BCS002");
        request.setName("Another Student");
        request.setEmail("vasu@gmail.com");
        request.setPhone("9999999999");
        request.setAge(21);
        request.setDepartment("CSE");

        when(studentService.addStudent(
                any(StudentRequestDTO.class)
        )).thenThrow(
                new DuplicateStudentException(
                        "Email already exists"
                )
        );

        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Email already exists"));
    }
}