package com.project.java.sewacam.dto;

public class UploadResponseDTO<T> {

    private String message;
    private String imageUrl;
    private T data;

    public UploadResponseDTO(String message, String imageUrl, T data) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public T getData() {
        return data;
    }
}
