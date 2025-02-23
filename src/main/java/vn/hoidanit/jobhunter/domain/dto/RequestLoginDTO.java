package vn.hoidanit.jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class RequestLoginDTO {
    @NotBlank(message = "username không để trống đươc")
    private String username;
    private String password;

    @NotBlank(message = "password không được để trống")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
