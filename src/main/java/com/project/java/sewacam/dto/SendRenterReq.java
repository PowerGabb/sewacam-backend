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
public class SendRenterReq {
    private Integer renterId;      // wajib
    private Integer ownerId;       // opsional: kalau mau lock ke owner tertentu
    private String text;           // wajib kalau imageUrl null
    private String imageUrl;       // opsional
}
