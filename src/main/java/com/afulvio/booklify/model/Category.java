package com.afulvio.booklify.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Integer id;

    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

}
