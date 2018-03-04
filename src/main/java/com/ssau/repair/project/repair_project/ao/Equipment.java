package com.ssau.repair.project.repair_project.ao;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "Equipment")
public class Equipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    public Equipment()
    {

    }

    public Equipment (Integer id)
    {
        this.id = id;
    }

    public Equipment(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
