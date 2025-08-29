package com.prod.GreenValley.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    

    

}
