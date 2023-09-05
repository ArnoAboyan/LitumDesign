package com.litumdesign.LitumDesign.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TestClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public String username;
    public String password;
}
