package com.microservices.address.service;

import com.microservices.address.model.dto.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {

    AddressDto getAddress(Long id);

    List<AddressDto> getEmployeeAddressById(Long id);

    List<AddressDto> saveAddress(List<AddressDto> addressDtoList);

    AddressDto updateAddress(Long id, AddressDto addressDto);

    Void deleteAddress(Long id);

}
