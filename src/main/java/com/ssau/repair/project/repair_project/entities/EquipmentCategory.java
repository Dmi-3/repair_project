package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Equipment_category")
public class EquipmentCategory implements Serializable {

    public EquipmentCategory() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
