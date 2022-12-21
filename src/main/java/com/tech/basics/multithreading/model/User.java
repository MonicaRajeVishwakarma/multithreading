package com.tech.basics.multithreading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
public record User(
        @Id
        @GeneratedValue
        int id,
        String name,
        String email,
        String gender
) {


        public User(String name, String email, String gender) {
                this(0, name, email, gender);
        }
}
