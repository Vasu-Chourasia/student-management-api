package com.vasu.studentmanagement.repository;

import com.vasu.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository
        extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {

    boolean existsByEmail(String email);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRollNumberAndIdNot(String rollNumber, Long id);
}