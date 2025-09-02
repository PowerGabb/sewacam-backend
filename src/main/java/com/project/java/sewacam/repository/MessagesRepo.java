/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.project.java.sewacam.repository;

import com.project.java.sewacam.model.Messages;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hilal
 */
public interface MessagesRepo extends JpaRepository<Messages, Integer> {

    // Semua pesan dalam sebuah conversation (urut naik berdasarkan waktu)
    List<Messages> findByConversation_IdOrderByCreatedAtAsc(Integer conversationId);

    // Pesan baru setelah timestamp tertentu (untuk polling incremental)
    List<Messages> findByConversation_IdAndCreatedAtAfterOrderByCreatedAtAsc(
            Integer conversationId, Instant createdAt);

    // (Opsional) untuk paginasi bila diperlukan
    // Page<Message> findByConversation_Id(Integer conversationId, Pageable pageable);
}
