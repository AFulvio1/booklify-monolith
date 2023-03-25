package com.afulvio.booklify.dto;

import java.lang.Long;
import java.math.BigDecimal;

import com.afulvio.booklify.model.Category;
import lombok.Data;

@Data
public class BookDTO {

    private Long id;

    private Category category;

    private String author;

    private String title;

    private String subtitle;

    private String volume;

    private Integer yearOfPublication;

    private String publishingHouse;

    private String placeOfPublication;

    private String isbn;

    private BigDecimal price;

    private String note;

    private String imageName;

}
