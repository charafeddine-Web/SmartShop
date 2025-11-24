package com.smartshop.smartshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Client {

    @Id
    private Long id;

    private String name;

    private String email;

    private String NiveauFidelity;


}
