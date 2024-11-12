package com.eyup.challenge.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens") // MongoDB koleksiyon ismi
public class Token {

    @Id
    private String id;

    private String accessToken;

    private String refreshToken;

    private boolean loggedOut;

    @DBRef
    private User user;
}
