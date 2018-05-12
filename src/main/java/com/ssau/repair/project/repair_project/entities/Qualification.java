package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Qualification")
public class Qualification
{
    @Id
    @SequenceGenerator(name = "qualification_sequence", sequenceName = "qualification_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "qualification_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "Repair_type_id")
    private RepairType repairType;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "qualifications")
    private Set<Worker> workers;

    public Qualification()
    {

    }

    public Qualification(String name, RepairType repairType)
    {
        this.name = name;
        this.repairType = repairType;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public RepairType getRepairType()
    {
        return repairType;
    }

    public void setRepairType(RepairType repairType)
    {
        this.repairType = repairType;
    }

    public Set<Worker> getWorkers()
    {
        return workers;
    }

    public void setWorkers(Set<Worker> workers)
    {
        this.workers = workers;
    }

}
