package com.project.java.sewacam.dto;

public class LoginResponseDTO {
    private String message;
    private String token; 
    private String username;
    private String email;
    private String noHp;
    private String address;
    private String role;
    private Integer id;
    
    public LoginResponseDTO(String message, String token, String username, String email, String noHp, String address,
			String role, Integer id) {
		this.message = message;
		this.token = token;
		this.username = username;
		this.email = email;
		this.noHp = noHp;
		this.address = address;
		this.role = role;
                this.id = id;
	}

	// Getter & Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}