package com.eyup.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate primary key
    private Long id;

    private String accessToken;

    private String refreshToken;

    private boolean loggedOut;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with User
    @JoinColumn(name = "user_id") // Column that holds the foreign key to User
    private User user;

}