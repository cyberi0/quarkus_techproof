package com.techproof.entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "GENDERS")
public class Gender extends PanacheEntity {
    @Column(name = "NAME", nullable = false)
    public String name;

    public Gender(){}

    public Gender(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
