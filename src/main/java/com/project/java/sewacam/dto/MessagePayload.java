/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.dto;

import lombok.Data;

/**
 *
 * @author hilal
 */
@Data
public class MessagePayload {
    private Integer senderId;  // wajib
    private String  content;   // wajib (teks)
}
