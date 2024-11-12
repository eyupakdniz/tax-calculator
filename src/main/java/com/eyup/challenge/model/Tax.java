package com.eyup.challenge.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "taxes")
public class Tax {
    @Id
    private String id;
    private String name;
    private double rate;


    public Tax(String tax1, double v) {
        this.name = name;
        this.rate = rate;
    }
}
