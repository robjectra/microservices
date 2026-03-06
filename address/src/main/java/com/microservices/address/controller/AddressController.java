package com.microservices.address.controller;

import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {

    private AddressService addressService;

    @PostMapping()
    public ResponseEntity<List<AddressDto>> getAddress(@RequestBody List<AddressDto> addressDtoList){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(addressDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AddressDto>> getEmployeeAddress(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(addressService.getEmployeeAddress(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto){
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(id, addressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("record deleted");
    }

}
