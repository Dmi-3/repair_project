package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Repair_type")
public class RepairType implements Serializable
{
    @Id
    @SequenceGenerator(name = "repair_types_sequence", sequenceName = "repair_types_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "repair_types_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "repairType")
    private Set<RepairStandard> repairStandards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "repairType")
    private Set<RepairHistory> repairHistory;

    public RepairType()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public Set<RepairStandard> getRepairStandards()
    {
        return repairStandards;
    }

    public void setRepairStandards(Set<RepairStandard> repairStandards)
    {
        this.repairStandards = repairStandards;
    }

    public Set<RepairHistory> getRepairHistory()
    {
        return repairHistory;
    }

    public void setRepairHistory(Set<RepairHistory> repairHistory)
    {
        this.repairHistory = repairHistory;
    }
}
