package com.afulvio.booklify.dto;

import com.afulvio.booklify.model.Book;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private String username;
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
    private BigDecimal total;
    private List<Book> books;
}
