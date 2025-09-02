package com.project.java.sewacam.repository;

import com.project.java.sewacam.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer> {
    List<Camera> findByCategoryId(Integer categoryId);
}
