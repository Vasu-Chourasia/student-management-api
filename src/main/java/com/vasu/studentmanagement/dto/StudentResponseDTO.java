package com.vasu.studentmanagement.dto;

public class StudentResponseDTO {

    private Long id;
    private String rollNumber;
    private String name;
    private String email;
    private String phone;
    private int age;
    private String department;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(
            Long id,
            String rollNumber,
            String name,
            String email,
            String phone,
            int age,
            String department) {

        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }
}