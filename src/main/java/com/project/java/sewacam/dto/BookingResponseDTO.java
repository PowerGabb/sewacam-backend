/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.dto;

/**
 *
 * @author hilal
 */
public class BookingResponseDTO {
    
     private Integer cameraId;
     private String message;

    public BookingResponseDTO(Integer cameraId, String message) {
        this.cameraId = cameraId;
        this.message = message;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
     
    
}
