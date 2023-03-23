package com.afulvio.booklify.dto;

import com.afulvio.booklify.model.User;
import lombok.Data;

@Data
public class OrderDTO {

    private Long id;

    private User user;

    private String firstname;

    private String lastname;

    private String firstAddress;

    private String secondAddress;

    private String postCode;

    private String city;

    private String nation;

    private Long telephone;

    private String email;

    private String note;

    private double total;

}
