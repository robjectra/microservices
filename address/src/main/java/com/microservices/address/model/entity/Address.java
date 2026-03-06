package com.microservices.address.model.entity;

import com.microservices.address.constants.AddressType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long empId;
    private String city;
    private String state;
    private Long pincode;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AddressType type;
}
