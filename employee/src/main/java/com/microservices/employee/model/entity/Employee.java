package com.microservices.employee.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "employee_unique",
                        columnNames = {"email", "code"}
                )
        }
)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String companyName;
    @Column(unique = true)
    private String code;
}
