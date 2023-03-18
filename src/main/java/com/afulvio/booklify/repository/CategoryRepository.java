package com.afulvio.booklify.repository;

import java.lang.Long;

import com.afulvio.booklify.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
