/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hilal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversations")
public class Conversations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // relasi ke penyewa (renter)
    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    // relasi ke admin (opsional: bisa 1 admin yg handle)
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // pesan terakhir (opsional: untuk menampilkan preview list chat)
    private String lastMessage;

    // waktu terakhir update chat
    private Instant lastTime;
    
    

}
