package com.microservices.address.repository;

import com.microservices.address.model.enums.AddressType;
import com.microservices.address.model.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("AddressRepository Data Tests")
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private Address testAddress;

    @BeforeEach
    void setUp() {
        testAddress = new Address();
        testAddress.setEmpId(100L);
        testAddress.setCity("New York");
        testAddress.setState("NY");
        testAddress.setPincode(10001L);
        testAddress.setType(AddressType.PERMANENT);
    }

    @Test
    @DisplayName("Should save address successfully")
    void testSaveAddress() {
        // Act
        Address savedAddress = addressRepository.save(testAddress);

        // Assert
        assertNotNull(savedAddress);
        assertNotNull(savedAddress.getId());
        assertEquals("New York", savedAddress.getCity());
        assertEquals(AddressType.PERMANENT, savedAddress.getType());
    }

    @Test
    @DisplayName("Should find address by ID")
    void testFindAddressById() {
        // Arrange
        Address savedAddress = addressRepository.save(testAddress);

        // Act
        Optional<Address> foundAddress = addressRepository.findById(savedAddress.getId());

        // Assert
        assertTrue(foundAddress.isPresent());
        assertEquals(savedAddress.getId(), foundAddress.get().getId());
        assertEquals("New York", foundAddress.get().getCity());
    }

    @Test
    @DisplayName("Should return empty when address not found")
    void testFindAddressById_NotFound() {
        // Act
        Optional<Address> foundAddress = addressRepository.findById(999L);

        // Assert
        assertTrue(foundAddress.isEmpty());
    }

    @Test
    @DisplayName("Should find all addresses")
    void testFindAllAddresses() {
        // Arrange
        addressRepository.save(testAddress);

        Address address2 = new Address();
        address2.setEmpId(101L);
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setPincode(02101L);
        address2.setType(AddressType.TEMPORARY);
        addressRepository.save(address2);

        // Act
        List<Address> allAddresses = addressRepository.findAll();

        // Assert
        assertNotNull(allAddresses);
        assertTrue(allAddresses.size() >= 2);
    }

    @Test
    @DisplayName("Should update address successfully")
    void testUpdateAddress() {
        // Arrange
        Address savedAddress = addressRepository.save(testAddress);
        Long addressId = savedAddress.getId();

        // Act
        savedAddress.setCity("Los Angeles");
        savedAddress.setState("CA");
        Address updatedAddress = addressRepository.save(savedAddress);

        // Assert
        assertEquals(addressId, updatedAddress.getId());
        assertEquals("Los Angeles", updatedAddress.getCity());
        assertEquals("CA", updatedAddress.getState());
    }

    @Test
    @DisplayName("Should delete address successfully")
    void testDeleteAddress() {
        // Arrange
        Address savedAddress = addressRepository.save(testAddress);
        Long addressId = savedAddress.getId();

        // Act
        addressRepository.deleteById(addressId);
        Optional<Address> deletedAddress = addressRepository.findById(addressId);

        // Assert
        assertTrue(deletedAddress.isEmpty());
    }

    @Test
    @DisplayName("Should save address with TEMPORARY type")
    void testSaveAddress_WithTemporaryType() {
        // Arrange
        testAddress.setType(AddressType.TEMPORARY);

        // Act
        Address savedAddress = addressRepository.save(testAddress);

        // Assert
        assertNotNull(savedAddress.getId());
        assertEquals(AddressType.TEMPORARY, savedAddress.getType());
    }

    @Test
    @DisplayName("Should save multiple addresses for same employee")
    void testSaveMultipleAddressesForSameEmployee() {
        // Arrange
        Address address1 = new Address();
        address1.setEmpId(100L);
        address1.setCity("New York");
        address1.setState("NY");
        address1.setPincode(10001L);
        address1.setType(AddressType.PERMANENT);

        Address address2 = new Address();
        address2.setEmpId(100L);
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setPincode(02101L);
        address2.setType(AddressType.TEMPORARY);

        // Act
        addressRepository.save(address1);
        addressRepository.save(address2);
        List<Address> allAddresses = addressRepository.findAll();

        // Assert
        assertTrue(allAddresses.size() >= 2);
    }

    @Test
    @DisplayName("Should handle null city gracefully")
    void testSaveAddress_WithNullCity() {
        // Arrange
        testAddress.setCity(null);

        // Act & Assert
        // This should either throw a validation error or save with null
        // Depending on your JPA configuration
        assertDoesNotThrow(() -> addressRepository.save(testAddress));
    }

    @Test
    @DisplayName("Should persist all address fields correctly")
    void testAddressPersistence() {
        // Arrange
        testAddress.setCity("Chicago");
        testAddress.setState("IL");
        testAddress.setPincode(60601L);
        testAddress.setEmpId(102L);
        testAddress.setType(AddressType.TEMPORARY);

        // Act
        Address savedAddress = addressRepository.save(testAddress);
        Address retrievedAddress = addressRepository.findById(savedAddress.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedAddress);
        assertEquals("Chicago", retrievedAddress.getCity());
        assertEquals("IL", retrievedAddress.getState());
        assertEquals(60601L, retrievedAddress.getPincode());
        assertEquals(102L, retrievedAddress.getEmpId());
        assertEquals(AddressType.TEMPORARY, retrievedAddress.getType());
    }
}

