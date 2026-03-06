package com.microservices.employee.service;

import com.microservices.employee.model.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto getEmployee(Long id);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
    void deleteEmployee(Long id);
}
