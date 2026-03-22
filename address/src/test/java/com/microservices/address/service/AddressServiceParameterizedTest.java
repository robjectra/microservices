package com.microservices.address.service;

import com.microservices.address.model.enums.AddressType;
import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.model.entity.Address;
import com.microservices.address.repository.AddressRepository;
import com.microservices.address.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AddressService Parameterized Tests")
class AddressServiceParameterizedTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== @ValueSource Tests ====================
    /**
     * Tests address retrieval with different valid IDs
     */
    @ParameterizedTest(name = "Should retrieve address with ID: {0}")
    @ValueSource(longs = {1L, 100L, 999L, 1000000L})
    @DisplayName("Get address by various ID values")
    void testGetAddressById_WithVariousIds(Long addressId) {
        // Arrange
        Address address = new Address();
        address.setId(addressId);
        address.setCity("Test City");
        address.setState("TC");

        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressId);
        addressDto.setCity("Test City");

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(modelMapper.map(address, AddressDto.class)).thenReturn(addressDto);

        // Act
        AddressDto result = addressService.getAddress(addressId);

        // Assert
        assertNotNull(result);
        assertEquals(addressId, result.getId());
        verify(addressRepository, times(1)).findById(addressId);
    }

    // ==================== @CsvSource Tests ====================
    /**
     * Tests address saving with different city and state combinations
     */
    @ParameterizedTest(name = "Save address: {0}, {1}")
    @CsvSource({
            "New York, NY",
            "Los Angeles, CA",
            "Chicago, IL",
            "Houston, TX",
            "Phoenix, AZ",
            "Philadelphia, PA",
            "San Antonio, TX",
            "San Diego, CA"
    })
    @DisplayName("Save addresses with different cities and states")
    void testSaveAddress_WithDifferentLocations(String city, String state) {
        // Arrange
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(city);
        addressDto.setState(state);
        addressDto.setEmpId(100L);
        addressDto.setPincode(10001L);
        addressDto.setType(AddressType.PERMANENT);

        Address address = new Address();
        address.setId(1L);
        address.setCity(city);
        address.setState(state);
        address.setEmpId(100L);

        when(modelMapper.map(any(AddressDto.class), any())).thenReturn(address);
        when(addressRepository.saveAll(any(List.class))).thenReturn(List.of(address));
        when(modelMapper.map(address, AddressDto.class)).thenReturn(addressDto);

        // Act
        List<AddressDto> result = addressService.saveAddress(List.of(addressDto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(city, result.get(0).getCity());
        assertEquals(state, result.get(0).getState());
        verify(addressRepository, times(1)).saveAll(any(List.class));
    }

    /**
     * Tests with different employee IDs and pin codes
     */
    @ParameterizedTest(name = "Employee ID: {0}, Pin Code: {1}")
    @CsvSource({
            "100, 10001",
            "200, 90210",
            "300, 60601",
            "400, 75001",
            "500, 85001"
    })
    @DisplayName("Save addresses with different employee IDs and pin codes")
    void testSaveAddress_WithDifferentEmployeeAndPins(Long empId, Long pincode) {
        // Arrange
        AddressDto addressDto = new AddressDto();
        addressDto.setEmpId(empId);
        addressDto.setPincode(pincode);
        addressDto.setCity("Test City");
        addressDto.setState("TS");
        addressDto.setType(AddressType.PERMANENT);

        Address address = new Address();
        address.setId(1L);
        address.setEmpId(empId);
        address.setPincode(pincode);
        address.setCity("Test City");

        when(modelMapper.map(any(AddressDto.class), any())).thenReturn(address);
        when(addressRepository.saveAll(any(List.class))).thenReturn(List.of(address));
        when(modelMapper.map(address, AddressDto.class)).thenReturn(addressDto);

        // Act
        List<AddressDto> result = addressService.saveAddress(List.of(addressDto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(empId, result.get(0).getEmpId());
        assertEquals(pincode, result.get(0).getPincode());
    }

    // ==================== @EnumSource Tests ====================
    /**
     * Tests address saving with all address types
     */
    @ParameterizedTest(name = "Address type: {0}")
    @EnumSource(AddressType.class)
    @DisplayName("Save addresses with all address types")
    void testSaveAddress_WithAllAddressTypes(AddressType addressType) {
        // Arrange
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setState("TS");
        addressDto.setEmpId(100L);
        addressDto.setPincode(10001L);
        addressDto.setType(addressType);

        Address address = new Address();
        address.setId(1L);
        address.setCity("Test City");
        address.setType(addressType);

        when(modelMapper.map(any(AddressDto.class), any())).thenReturn(address);
        when(addressRepository.saveAll(any(List.class))).thenReturn(List.of(address));
        when(modelMapper.map(address, AddressDto.class)).thenReturn(addressDto);

        // Act
        List<AddressDto> result = addressService.saveAddress(List.of(addressDto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressType, result.get(0).getType());
    }

    /**
     * Tests update address with different address types
     */
    @ParameterizedTest(name = "Update to type: {0}")
    @EnumSource(value = AddressType.class, names = {"PERMANENT", "TEMPORARY"})
    @DisplayName("Update address with different address types")
    void testUpdateAddress_WithDifferentTypes(AddressType newType) {
        // Arrange
        Address address = new Address();
        address.setId(1L);
        address.setCity("Original City");
        address.setType(AddressType.PERMANENT);

        AddressDto updateDto = new AddressDto();
        updateDto.setCity("Updated City");
        updateDto.setType(newType);

        AddressDto resultDto = new AddressDto();
        resultDto.setId(1L);
        resultDto.setCity("Updated City");
        resultDto.setType(newType);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(modelMapper).map(updateDto, address);
        when(addressRepository.save(address)).thenReturn(address);
        when(modelMapper.map(address, AddressDto.class)).thenReturn(resultDto);

        // Act
        AddressDto result = addressService.updateAddress(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(newType, result.getType());
        verify(addressRepository, times(1)).findById(1L);
    }

    // ==================== @MethodSource Tests ====================
    /**
     * Tests delete address with different valid ID values
     */
    @ParameterizedTest(name = "Delete address with ID: {0}")
    @MethodSource("provideValidAddressIds")
    @DisplayName("Delete addresses with various valid IDs")
    void testDeleteAddress_WithVariousIds(Long addressId) {
        // Arrange
        Address address = new Address();
        address.setId(addressId);
        address.setCity("City");

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // Act
        Void result = addressService.deleteAddress(addressId);

        // Assert
        assertNull(result);
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).deleteById(addressId);
    }

    /**
     * Tests save multiple addresses with different data sets
     */
    @ParameterizedTest(name = "Save address list: {0}")
    @MethodSource("provideAddressDtoLists")
    @DisplayName("Save multiple addresses with various data combinations")
    void testSaveAddress_WithMultipleDataSets(List<AddressDto> addressDtoList, int expectedSize) {
        // Arrange
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < expectedSize; i++) {
            Address address = new Address();
            address.setId((long) (i + 1));
            address.setCity("City" + i);
            addressList.add(address);
        }

        when(modelMapper.map(any(AddressDto.class), any())).thenAnswer(invocation -> {
            Address addr = new Address();
            AddressDto dto = invocation.getArgument(0);
            addr.setCity(dto.getCity());
            addr.setEmpId(dto.getEmpId());
            return addr;
        });

        when(addressRepository.saveAll(any(List.class))).thenReturn(addressList);
        when(modelMapper.map(any(Address.class), any())).thenAnswer(invocation -> {
            Address addr = invocation.getArgument(0);
            AddressDto dto = new AddressDto();
            dto.setCity(addr.getCity());
            return dto;
        });

        // Act
        List<AddressDto> result = addressService.saveAddress(addressDtoList);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSize, result.size());
        verify(addressRepository, times(1)).saveAll(any(List.class));
    }

    /**
     * Tests get employee address with different address lists
     */
    @ParameterizedTest(name = "Employee {0} has {1} addresses")
    @MethodSource("provideEmployeeAddressScenarios")
    @DisplayName("Get employee addresses with various address counts")
    void testGetEmployeeAddress_WithMultipleScenarios(Long empId, int addressCount) {
        // Arrange
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < addressCount; i++) {
            Address address = new Address();
            address.setId((long) (i + 1));
            address.setEmpId(empId);
            address.setCity("City" + i);
            address.setType(i % 2 == 0 ? AddressType.PERMANENT : AddressType.TEMPORARY);
            addressList.add(address);
        }

        when(addressRepository.findAll()).thenReturn(addressList);
        when(modelMapper.map(any(Address.class), any())).thenAnswer(invocation -> {
            Address addr = invocation.getArgument(0);
            AddressDto dto = new AddressDto();
            dto.setId(addr.getId());
            dto.setEmpId(addr.getEmpId());
            dto.setCity(addr.getCity());
            dto.setType(addr.getType());
            return dto;
        });

        // Act
        List<AddressDto> result = addressService.getEmployeeAddress(empId);

        // Assert
        assertNotNull(result);
        assertEquals(addressCount, result.size());
        result.forEach(address -> assertEquals(empId, address.getEmpId()));
    }

    /**
     * Tests update address with different city and state combinations
     */
    @ParameterizedTest(name = "Update address to: {0}, {1}")
    @MethodSource("provideAddressUpdateData")
    @DisplayName("Update address with different locations")
    void testUpdateAddress_WithDifferentLocations(String newCity, String newState, AddressType newType) {
        // Arrange
        Address address = new Address();
        address.setId(1L);
        address.setCity("Old City");
        address.setState("OC");

        AddressDto updateDto = new AddressDto();
        updateDto.setCity(newCity);
        updateDto.setState(newState);
        updateDto.setType(newType);

        AddressDto resultDto = new AddressDto();
        resultDto.setId(1L);
        resultDto.setCity(newCity);
        resultDto.setState(newState);
        resultDto.setType(newType);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(modelMapper).map(updateDto, address);
        when(addressRepository.save(address)).thenReturn(address);
        when(modelMapper.map(address, AddressDto.class)).thenReturn(resultDto);

        // Act
        AddressDto result = addressService.updateAddress(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(newCity, result.getCity());
        assertEquals(newState, result.getState());
        assertEquals(newType, result.getType());
    }

    // ==================== Provider Methods ====================

    /**
     * Provides valid address IDs for testing
     */
    static Stream<Long> provideValidAddressIds() {
        return Stream.of(1L, 10L, 100L, 1000L, Long.MAX_VALUE);
    }

    /**
     * Provides different address DTO lists for bulk operations
     */
    static Stream<org.junit.jupiter.params.provider.Arguments> provideAddressDtoLists() {
        AddressDto address1 = new AddressDto();
        address1.setCity("City1");
        address1.setEmpId(100L);

        AddressDto address2 = new AddressDto();
        address2.setCity("City2");
        address2.setEmpId(100L);

        AddressDto address3 = new AddressDto();
        address3.setCity("City3");
        address3.setEmpId(100L);

        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(List.of(address1), 1),
                org.junit.jupiter.params.provider.Arguments.of(List.of(address1, address2), 2),
                org.junit.jupiter.params.provider.Arguments.of(List.of(address1, address2, address3), 3),
                org.junit.jupiter.params.provider.Arguments.of(new ArrayList<>(), 0)
        );
    }

    /**
     * Provides employee ID with different address counts
     */
    static Stream<org.junit.jupiter.params.provider.Arguments> provideEmployeeAddressScenarios() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(100L, 1),
                org.junit.jupiter.params.provider.Arguments.of(101L, 2),
                org.junit.jupiter.params.provider.Arguments.of(102L, 3),
                org.junit.jupiter.params.provider.Arguments.of(103L, 5)
        );
    }

    /**
     * Provides address update data with different combinations
     */
    static Stream<org.junit.jupiter.params.provider.Arguments> provideAddressUpdateData() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("New York", "NY", AddressType.PERMANENT),
                org.junit.jupiter.params.provider.Arguments.of("Los Angeles", "CA", AddressType.TEMPORARY),
                org.junit.jupiter.params.provider.Arguments.of("Chicago", "IL", AddressType.PERMANENT),
                org.junit.jupiter.params.provider.Arguments.of("Houston", "TX", AddressType.TEMPORARY),
                org.junit.jupiter.params.provider.Arguments.of("Phoenix", "AZ", AddressType.PERMANENT)
        );
    }
}

