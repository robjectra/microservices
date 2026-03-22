package com.microservices.address.service.impl;

import com.microservices.address.exception.ResourceNotFoundException;
import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.model.entity.Address;
import com.microservices.address.repository.AddressRepository;
import com.microservices.address.service.AddressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    private ModelMapper modelMapper;

    @Override
    public AddressDto getAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getEmployeeAddressById(Long id) {
        List<Address> addresses = addressRepository.findByEmpId(id);
        return addresses.stream()
                .map(adder -> modelMapper.map(adder, AddressDto.class))
                .toList();
    }

    @Override
    public List<AddressDto> saveAddress(List<AddressDto> addressDtos) {
        List<Address> addresses = addressDtos.stream()
                .map(dto -> modelMapper.map(dto, Address.class))
                .toList();

        return addressRepository.saveAll(addresses).stream()
                .map(addr -> modelMapper.map(addr, AddressDto.class))
                .toList();
    }

    @Override
    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        modelMapper.map(addressDto, address);
        return modelMapper.map(addressRepository.save(address), AddressDto.class);
    }

    @Override
    public Void deleteAddress(Long id) {
        addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        addressRepository.deleteById(id);
        return null;
    }
}
