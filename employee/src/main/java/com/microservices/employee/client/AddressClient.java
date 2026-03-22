package com.microservices.employee.client;

import com.microservices.employee.model.dto.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ADDRESS-SERVICE")
public interface AddressClient {
    @GetMapping("/api/v1/addresses/employee/{id}")
    List<AddressDto> getAddressesByEmployeeId(@PathVariable Long id);
}
