package com.microservices.employee.service.Impl;

import com.microservices.employee.client.AddressClient;
import com.microservices.employee.exception.ResourceAlreadyExistException;
import com.microservices.employee.exception.ResourceNotFoundException;
import com.microservices.employee.model.dto.AddressDto;
import com.microservices.employee.model.dto.EmployeeDto;
import com.microservices.employee.model.entity.Employee;
import com.microservices.employee.repository.EmployeeRepository;
import com.microservices.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private  final AddressClient addressClient;

    @Override
    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        List<AddressDto> addressDtoList = addressClient.getAddressesByEmployeeId(employee.getId());
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        employeeDto.setAddresses(addressDtoList);
        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        if (employeeList.isEmpty()){
            throw new ResourceNotFoundException("no user found");
        }
        return employeeList.parallelStream()
                .map(employee -> {
                    EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
                    List<AddressDto> addressDtoList = addressClient.getAddressesByEmployeeId(employeeDto.getId());
                    employeeDto.setAddresses(addressDtoList);
                    return employeeDto;
                })
                .toList();
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Optional<Employee> isEmployee = employeeRepository.findEmployeeByEmailAndCode(employee.getEmail(), employee.getCode());
        if(isEmployee.isPresent()){
            throw new ResourceAlreadyExistException("user already exist");
        }
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Copy only non-null fields from DTO → existing entity
        modelMapper.map(employeeDto, existing);

        Employee saved = employeeRepository.save(existing);

        return modelMapper.map(saved, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(RuntimeException::new);
        employeeRepository.deleteById(id);
    }
}
