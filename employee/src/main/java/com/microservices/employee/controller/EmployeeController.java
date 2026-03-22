package com.microservices.employee.controller;

import com.microservices.employee.model.dto.EmployeeDto;
import com.microservices.employee.service.EmployeeService;
import com.microservices.employee.service.KafkaMessageProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final KafkaMessageProducer producer;

    @Value("${spring.kafka.topic-employee}")
    private String employeeTopic;

    public EmployeeController(EmployeeService employeeService,
                              KafkaMessageProducer producer) {
        this.employeeService = employeeService;
        this.producer = producer;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List<EmployeeDto> employeeDtoList = employeeService.getAllEmployees();
        producer.sendMessage(employeeTopic, employeeDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployees(@PathVariable Long id){
        EmployeeDto employeeDto = employeeService.getEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDto);
    }

    @PostMapping()
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employeeDto));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployee(id, employeeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
