package com.vasu.studentmanagement.specification;

import com.vasu.studentmanagement.entity.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<Student> hasDepartment(String department) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("department"),
                        department
                );
    }

    public static Specification<Student> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Student> hasAge(Integer age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("age"),
                        age
                );
    }

    public static Specification<Student> hasMinAge(Integer minAge) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("age"),
                        minAge
                );
    }

    public static Specification<Student> hasMaxAge(Integer maxAge) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("age"),
                        maxAge
                );
    }
}