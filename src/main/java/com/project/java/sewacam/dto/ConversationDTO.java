/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.dto;

import com.project.java.sewacam.model.Conversations;
import java.time.Instant;
import lombok.Data;

/**
 *
 * @author hilal
 */
@Data
public class ConversationDTO {
    
    private Integer id;
    private Integer renterId;
    private String  renterName;
    private Integer ownerId;
    private String  ownerName;
    private String  lastMessage;
    private Instant lastTime;

    public static ConversationDTO from(Conversations c) {
        ConversationDTO d = new ConversationDTO();
        d.setId(c.getId());
        if (c.getRenter() != null) {
            d.setRenterId(c.getRenter().getId());
            d.setRenterName(c.getRenter().getUsername());
        }
        if (c.getOwner() != null) {
            d.setOwnerId(c.getOwner().getId());
            d.setOwnerName(c.getOwner().getUsername());
        }
        d.setLastMessage(c.getLastMessage());
        d.setLastTime(c.getLastTime());
        return d;
    }
    
}
