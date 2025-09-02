/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.repository;

import com.project.java.sewacam.model.Conversations;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author hilal
 */
public interface ConversationRepo extends JpaRepository<Conversations, Integer> {

    // Ambil 1:1 conversation milik renter tertentu (relasi: Conversation.renter -> User)
    Optional<Conversations> findByRenter_Id(Integer renterId);

    Optional<Conversations> findByRenter_IdAndOwner_Id(Integer renterId, Integer ownerId);

    // Daftar semua conversation urut terbaru (untuk admin)
    List<Conversations> findAllByOrderByLastTimeDesc();

    // (Opsional) versi fetch join agar renter/admin ikut ter-load dalam 1 query
    @Query("""
            SELECT DISTINCT c
            FROM Conversations c
            LEFT JOIN FETCH c.renter r
            LEFT JOIN FETCH c.owner o
            ORDER BY c.lastTime DESC
           """)
    List<Conversations> findAllWithUsersOrderByLastTimeDesc();

}
