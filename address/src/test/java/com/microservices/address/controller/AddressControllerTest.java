package com.microservices.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.address.model.enums.AddressType;
import com.microservices.address.model.dto.AddressDto;
import com.microservices.address.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AddressController.class)
@DisplayName("AddressController Integration Tests")
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddressDto testAddressDto;
    private List<AddressDto> addressDtoList;

    @BeforeEach
    void setUp() {
        testAddressDto = new AddressDto();
        testAddressDto.setId(1L);
        testAddressDto.setEmpId(100L);
        testAddressDto.setCity("New York");
        testAddressDto.setState("NY");
        testAddressDto.setPincode(10001L);
        testAddressDto.setType(AddressType.PERMANENT);

        addressDtoList = new ArrayList<>();
        addressDtoList.add(testAddressDto);
    }

    @Test
    @DisplayName("POST /api/v1/address - Should save addresses successfully")
    void testSaveAddress_Success() throws Exception {
        // Arrange
        when(addressService.saveAddress(any(List.class))).thenReturn(addressDtoList);

        // Act & Assert
        mockMvc.perform(post("/api/v1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDtoList)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city", is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].state", is("NY")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", is("PERMANENT")));

        verify(addressService, times(1)).saveAddress(any(List.class));
    }

    @Test
    @DisplayName("POST /api/v1/address - Should handle empty list")
    void testSaveAddress_EmptyList() throws Exception {
        // Arrange
        List<AddressDto> emptyList = new ArrayList<>();
        when(addressService.saveAddress(emptyList)).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(post("/api/v1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyList)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));

        verify(addressService, times(1)).saveAddress(emptyList);
    }

    @Test
    @DisplayName("POST /api/v1/address - Should save multiple addresses")
    void testSaveAddress_Multiple() throws Exception {
        // Arrange
        AddressDto address2 = new AddressDto();
        address2.setId(2L);
        address2.setEmpId(100L);
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setPincode(02101L);
        address2.setType(AddressType.TEMPORARY);

        List<AddressDto> multipleAddresses = new ArrayList<>();
        multipleAddresses.add(testAddressDto);
        multipleAddresses.add(address2);

        when(addressService.saveAddress(any(List.class))).thenReturn(multipleAddresses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(multipleAddresses)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city", is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city", is("Boston")));

        verify(addressService, times(1)).saveAddress(any(List.class));
    }

    @Test
    @DisplayName("GET /api/v1/address/{id} - Should get employee addresses successfully")
    void testGetEmployeeAddress_Success() throws Exception {
        // Arrange
        when(addressService.getEmployeeAddress(100L)).thenReturn(addressDtoList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/address/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empId", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city", is("New York")));

        verify(addressService, times(1)).getEmployeeAddress(100L);
    }

    @Test
    @DisplayName("GET /api/v1/address/{id} - Should return multiple addresses for employee")
    void testGetEmployeeAddress_Multiple() throws Exception {
        // Arrange
        AddressDto address2 = new AddressDto();
        address2.setId(2L);
        address2.setEmpId(100L);
        address2.setCity("Boston");
        address2.setState("MA");
        address2.setType(AddressType.TEMPORARY);

        List<AddressDto> multipleAddresses = new ArrayList<>();
        multipleAddresses.add(testAddressDto);
        multipleAddresses.add(address2);

        when(addressService.getEmployeeAddress(100L)).thenReturn(multipleAddresses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/address/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city", is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city", is("Boston")));

        verify(addressService, times(1)).getEmployeeAddress(100L);
    }


    @Test
    @DisplayName("PUT /api/v1/address/{id} - Should update address successfully")
    void testUpdateAddress_Success() throws Exception {
        // Arrange
        AddressDto updateDto = new AddressDto();
        updateDto.setCity("Los Angeles");
        updateDto.setState("CA");
        updateDto.setPincode(90001L);
        updateDto.setType(AddressType.TEMPORARY);

        AddressDto updatedAddress = new AddressDto();
        updatedAddress.setId(1L);
        updatedAddress.setEmpId(100L);
        updatedAddress.setCity("Los Angeles");
        updatedAddress.setState("CA");
        updatedAddress.setPincode(90001L);
        updatedAddress.setType(AddressType.TEMPORARY);

        when(addressService.updateAddress(eq(1L), any(AddressDto.class))).thenReturn(updatedAddress);

        // Act & Assert
        mockMvc.perform(put("/api/v1/address/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", is("Los Angeles")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state", is("CA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", is("TEMPORARY")));

        verify(addressService, times(1)).updateAddress(eq(1L), any(AddressDto.class));
    }


    @Test
    @DisplayName("DELETE /api/v1/address/{id} - Should delete address successfully")
    void testDeleteAddress_Success() throws Exception {
        // Arrange
        doNothing().when(addressService).deleteAddress(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/address/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is("record deleted")));

        verify(addressService, times(1)).deleteAddress(1L);
    }


    @Test
    @DisplayName("DELETE /api/v1/address/{id} - Should delete address and return success message")
    void testDeleteAddress_VerifyMessage() throws Exception {
        // Arrange
        doNothing().when(addressService).deleteAddress(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/address/1"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("record deleted"));

        verify(addressService, times(1)).deleteAddress(1L);
    }
}

