package com.microservices.address.controller;

import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing address operations in the microservices architecture.
 *
 * This controller handles all HTTP requests related to address CRUD operations.
 * It serves as the entry point for the Address Service and provides endpoints for:
 * - Creating new addresses
 * - Retrieving addresses by employee ID
 * - Updating existing addresses
 * - Deleting addresses
 *
 * All endpoints are prefixed with /api/v1/address and return appropriate HTTP status codes
 * along with ResponseEntity objects containing the operation results.
 *
 * @see AddressService for the business logic implementation
 * @see AddressDto for the data transfer object structure
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private AddressService addressService;

    /**
     * Creates and saves one or more addresses in the system.
     *
     * This endpoint accepts a list of AddressDto objects in the request body,
     * delegates to the service layer for persistence, and returns the saved addresses.
     *
     * @param addresses a list of AddressDto objects to be saved (must not be null)
     * @return a ResponseEntity with HTTP 201 (Created) status and the list of saved addresses
     *
     * @see AddressService#saveAddress(List)
     */
    @PostMapping
    public ResponseEntity<List<AddressDto>> saveAddresses(@RequestBody List<AddressDto> addresses) {
        return ResponseEntity.status(201).body(addressService.saveAddress(addresses));
    }

    /**
     * Retrieves all addresses associated with a specific employee.
     *
     * Given an employee ID, this endpoint queries the service layer to fetch all
     * addresses linked to that employee and returns them as a list.
     *
     * @param id the unique identifier of the employee (path variable, must not be null)
     * @return a ResponseEntity with HTTP 200 (OK) status and the list of addresses for the employee
     * @throws RuntimeException if no addresses are found for the employee (handled by AddressService)
     *
     * @see AddressService#getAddressesByEmployeeId(Long)
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<List<AddressDto>> getAddressesByEmployeeId(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getEmployeeAddressById(id));
    }

    /**
     * Updates an existing address identified by its ID.
     *
     * This endpoint accepts an address ID in the path and an AddressDto object
     * in the request body containing the updated address information. The service
     * layer performs the update and returns the modified address.
     *
     * @param id the unique identifier of the address to update (path variable, must not be null)
     * @param address the AddressDto object containing the updated address details (must not be null)
     * @return a ResponseEntity with HTTP 200 (OK) status and the updated address
     * @throws RuntimeException if the address with the given ID is not found (handled by AddressService)
     *
     * @see AddressService#updateAddress(Long, AddressDto)
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddressById(@PathVariable Long id, @RequestBody AddressDto address) {
        return ResponseEntity.ok(addressService.updateAddress(id, address));
    }

    /**
     * Deletes an address identified by its ID.
     *
     * This endpoint removes an address from the system based on the provided ID.
     * Upon successful deletion, it returns an HTTP 202 (Accepted) status with a
     * confirmation message.
     *
     * @param id the unique identifier of the address to delete (path variable, must not be null)
     * @return a ResponseEntity with HTTP 202 (Accepted) status and a confirmation message
     * @throws RuntimeException if the address with the given ID is not found (handled by AddressService)
     *
     * @see AddressService#deleteAddress(Long)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.accepted().body("record deleted");
    }
}