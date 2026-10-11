package com.user.authentication.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "modified")
    private LocalDateTime modified;

    @Column(name = "last_login")
    private LocalDateTime lastlogin;

    @Column(length = 512)
    private String token;

    @Column(name = "is_active")
    private boolean isactive;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    @Builder.Default
    private List<PhoneEntity> phones = new ArrayList<>();

    // Alias getters and setters para soportar camelCase
    public LocalDateTime getLastLogin() {
        return lastlogin;
    }

    public LocalDateTime getLastlogin() {
        return lastlogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastlogin = lastLogin;
    }

    public boolean isActive() {
        return isactive;
    }

    @PrePersist
    protected void onCreate() {
        if (created == null) {
            created = LocalDateTime.now();
        }
        if (modified == null) {
            modified = LocalDateTime.now();
        }
        if (lastlogin == null) {
            lastlogin = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modified = LocalDateTime.now();
    }
}
