package com.project.java.sewacam.repository;

import com.project.java.sewacam.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Metode bawaan seperti save(), findById(), findAll() sudah tersedia
}