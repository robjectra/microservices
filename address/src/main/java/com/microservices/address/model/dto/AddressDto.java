package com.microservices.address.model.dto;

import com.microservices.address.constants.AddressType;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private Long empId;
    private String city;
    private String state;
    private Long pincode;
    private AddressType type;
}
