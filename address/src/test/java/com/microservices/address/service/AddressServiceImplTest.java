package com.microservices.address.service;

import com.microservices.address.model.enums.AddressType;
import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.model.entity.Address;
import com.microservices.address.repository.AddressRepository;
import com.microservices.address.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

@DisplayName("AddressService Unit Tests")
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address testAddress;
    private AddressDto testAddressDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setEmpId(100L);
        testAddress.setCity("New York");
        testAddress.setState("NY");
        testAddress.setPincode(10001L);
        testAddress.setType(AddressType.PERMANENT);

        testAddressDto = new AddressDto();
        testAddressDto.setId(1L);
        testAddressDto.setEmpId(100L);
        testAddressDto.setCity("New York");
        testAddressDto.setState("NY");
        testAddressDto.setPincode(10001L);
        testAddressDto.setType(AddressType.PERMANENT);
    }

    @Test
    @DisplayName("Should get address by ID successfully")
    void testGetAddressById_Success() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(modelMapper.map(testAddress, AddressDto.class)).thenReturn(testAddressDto);

        // Act
        AddressDto result = addressService.getAddress(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New York", result.getCity());
        assertEquals(AddressType.PERMANENT, result.getType());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when address ID not found")
    void testGetAddressById_NotFound() {
        // Arrange
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> addressService.getAddress(999L));
        verify(addressRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get all addresses for an employee")
    void testGetEmployeeAddress_Success() {
        // Arrange
        Address address2 = new Address();
        address2.setId(2L);
        address2.setEmpId(100L);
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setPincode(02101L);
        address2.setType(AddressType.TEMPORARY);

        List<Address> addressList = new ArrayList<>();
        addressList.add(testAddress);
        addressList.add(address2);

        AddressDto addressDto2 = new AddressDto();
        addressDto2.setId(2L);
        addressDto2.setEmpId(100L);
        addressDto2.setCity("Boston");

        when(addressRepository.findAll()).thenReturn(addressList);
        when(modelMapper.map(testAddress, AddressDto.class)).thenReturn(testAddressDto);
        when(modelMapper.map(address2, AddressDto.class)).thenReturn(addressDto2);

        // Act
        List<AddressDto> result = addressService.getEmployeeAddress(100L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw RuntimeException when no addresses found")
    void testGetEmployeeAddress_Empty() {
        // Arrange
        when(addressRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> addressService.getEmployeeAddress(100L));
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save multiple addresses successfully")
    void testSaveAddress_Success() {
        // Arrange
        List<AddressDto> dtoList = new ArrayList<>();
        dtoList.add(testAddressDto);

        List<Address> entityList = new ArrayList<>();
        entityList.add(testAddress);

        when(modelMapper.map(testAddressDto, Address.class)).thenReturn(testAddress);
        when(addressRepository.saveAll(any(List.class))).thenReturn(entityList);
        when(modelMapper.map(testAddress, AddressDto.class)).thenReturn(testAddressDto);

        // Act
        List<AddressDto> result = addressService.saveAddress(dtoList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("New York", result.get(0).getCity());
        verify(addressRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    @DisplayName("Should save multiple addresses with different types")
    void testSaveAddress_MultipleWithDifferentTypes() {
        // Arrange
        AddressDto tempAddressDto = new AddressDto();
        tempAddressDto.setEmpId(100L);
        tempAddressDto.setCity("Boston");
        tempAddressDto.setState("MA");
        tempAddressDto.setPincode(02101L);
        tempAddressDto.setType(AddressType.TEMPORARY);

        Address tempAddress = new Address();
        tempAddress.setEmpId(100L);
        tempAddress.setCity("Boston");
        tempAddress.setType(AddressType.TEMPORARY);

        List<AddressDto> dtoList = new ArrayList<>();
        dtoList.add(testAddressDto);
        dtoList.add(tempAddressDto);

        List<Address> entityList = new ArrayList<>();
        entityList.add(testAddress);
        entityList.add(tempAddress);

        when(modelMapper.map(any(AddressDto.class), any()))
                .thenReturn(testAddress)
                .thenReturn(tempAddress);
        when(addressRepository.saveAll(any(List.class))).thenReturn(entityList);
        when(modelMapper.map(testAddress, AddressDto.class)).thenReturn(testAddressDto);
        when(modelMapper.map(tempAddress, AddressDto.class)).thenReturn(tempAddressDto);

        // Act
        List<AddressDto> result = addressService.saveAddress(dtoList);

        // Assert
        assertEquals(2, result.size());
        verify(addressRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    @DisplayName("Should update address successfully")
    void testUpdateAddress_Success() {
        // Arrange
        AddressDto updateDto = new AddressDto();
        updateDto.setCity("Los Angeles");
        updateDto.setState("CA");
        updateDto.setPincode(90001L);
        updateDto.setType(AddressType.TEMPORARY);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        doNothing().when(modelMapper).map(updateDto, testAddress);
        when(addressRepository.save(testAddress)).thenReturn(testAddress);
        when(modelMapper.map(testAddress, AddressDto.class)).thenReturn(testAddressDto);

        // Act
        AddressDto result = addressService.updateAddress(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(testAddress);
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating non-existent address")
    void testUpdateAddress_NotFound() {
        // Arrange
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            addressService.updateAddress(999L, testAddressDto)
        );
        verify(addressRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should delete address successfully")
    void testDeleteAddress_Success() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));

        // Act
        Void result = addressService.deleteAddress(1L);

        // Assert
        assertNull(result);
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when deleting non-existent address")
    void testDeleteAddress_NotFound() {
        // Arrange
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            addressService.deleteAddress(999L)
        );
        verify(addressRepository, times(1)).findById(999L);
        verify(addressRepository, never()).deleteById(999L);
    }
}

