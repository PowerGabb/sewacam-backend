package com.project.java.sewacam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // Gunakan LocalDate untuk tipe 'date'
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private User renter;

    private LocalDate startDate; // Tambahan, tipe data LocalDate
    private LocalDate endDate; // Tambahan, tipe data LocalDate
    private Double totalPrice;
    private String status; // Spring akan mengisinya
    private LocalDateTime createdAt;
    private LocalDateTime bookingDate;
    private Integer durationDays;
    private String method;
}