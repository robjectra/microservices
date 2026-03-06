package com.microservices.address.service.impl;

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
        Address address = addressRepository.findById(id).orElseThrow(RuntimeException::new);
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getEmployeeAddress(Long id) {
        List<Address> addressList = addressRepository.findAll();
        if(addressList.isEmpty()){
            throw new RuntimeException("address not found");
        }
        return addressList.parallelStream()
                .map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> saveAddress(List<AddressDto> addressDtoList) {
        // TO DO - check if employee exist or not
        List<Address> addressList = addressDtoList.stream()
                .map(addressDto -> modelMapper.map(addressDto, Address.class))
                .toList();

        addressList = addressRepository.saveAll(addressList);

        return addressList.stream()
                .map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        // TO DO - check if employee exist or not
        Address address = addressRepository.findById(id).orElseThrow(RuntimeException::new);
        modelMapper.map(addressDto, address);

        address = addressRepository.save(address);

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public Void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(RuntimeException::new);
        addressRepository.deleteById(id);
        return null;
    }
}
