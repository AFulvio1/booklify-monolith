package com.afulvio.booklify.model;

import jakarta.persistence.*;
import lombok.Data;

import java.lang.Long;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private String author;

    private String title;

    private String subtitle;

    private String volume;

    private Integer yearOfPublication;

    private String publishingHouse;

    private String placeOfPublication;

    private String isbn;

    private double price;

    private String note;

    private String imageName;

}
