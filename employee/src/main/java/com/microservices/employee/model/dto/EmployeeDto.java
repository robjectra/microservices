package com.microservices.employee.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private String companyName;
    private String code;
    private List<AddressDto> addresses = new ArrayList<>();
}
