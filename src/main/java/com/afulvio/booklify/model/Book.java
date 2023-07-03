package com.afulvio.booklify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
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

    private BigDecimal price;

    private String note;

    private String imageName;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orders;
}
