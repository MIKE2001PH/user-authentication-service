package com.user.authentication.service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {

    private UUID id;
    private String name;
    private String email;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastlogin;
    private String token;
    private boolean isactive;
    private List<PhoneDto> phones;

    public boolean isActive() {
        return isactive;
    }

    public void setActive(boolean active) {
        this.isactive = active;
    }

    public LocalDateTime getLastLogin() {
        return lastlogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastlogin = lastLogin;
    }
}
