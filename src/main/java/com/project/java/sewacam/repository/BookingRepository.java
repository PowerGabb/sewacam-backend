package com.project.java.sewacam.repository;

import com.project.java.sewacam.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByRenterId(Integer renterId);

    List<Booking> findAllByOrderByCreatedAtDesc();
}
