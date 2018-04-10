package com.ssau.repair.project.repair_project.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Equipment")
public class Equipment implements Serializable {

    public Equipment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
