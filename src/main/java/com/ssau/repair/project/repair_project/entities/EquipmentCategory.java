package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Equipment_category")
public class EquipmentCategory implements Serializable
{
    @Id
    @SequenceGenerator(name = "equipment_categories_sequence", sequenceName = "equipment_categories_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "equipment_categories_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "equipmentCategory")
    private Set<Equipment> equipments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "repairStandards")
    private Set<RepairStandard> repairStandards;

    public EquipmentCategory()
    {
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

    public Set<Equipment> getEquipments()
    {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments)
    {
        this.equipments = equipments;
    }

    @Override
    public String toString()
    {
        return "id: " + this.getId() + " name: " + this.getName();
    }

    public Set<RepairStandard> getRepairStandards()
    {
        return repairStandards;
    }

    public void setRepairStandards(Set<RepairStandard> repairStandards)
    {
        this.repairStandards = repairStandards;
    }
}
