/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.dto;

import com.project.java.sewacam.model.Messages;
import java.time.Instant;
import lombok.Data;

/**
 *
 * @author hilal
 */
@Data
public class MessageDTO {
    private Integer id;
    private Integer conversationId;
    private Integer senderId;
    private String  senderName;
    private String  content;
    private Instant createdAt;

    public static MessageDTO from(Messages m) {
        MessageDTO d = new MessageDTO();
        d.setId(m.getId());
        d.setConversationId(m.getConversation() != null ? m.getConversation().getId() : null);
        if (m.getSender() != null) {
            d.setSenderId(m.getSender().getId());
            d.setSenderName(m.getSender().getUsername());
        }
        d.setContent(m.getContent());
        d.setCreatedAt(m.getCreatedAt());
        return d;
    }
}