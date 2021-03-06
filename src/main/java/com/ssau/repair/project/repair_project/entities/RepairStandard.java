package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Repair_periodically_standards")
public class RepairStandard implements Serializable
{
    @Id
    @SequenceGenerator(name = "repair_standards_sequence", sequenceName = "repair_standards_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "repair_standards_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id")
    private EquipmentCategory equipmentCategory;

    @ManyToOne
    @JoinColumn(name = "Repair_type_id")
    private RepairType repairType;

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    @Column(name = "Repair_periodicity")
    private Integer repairPeriodicity;

    public RepairStandard()
    {

    }

    public RepairStandard(String name, EquipmentCategory equipmentCategory, RepairType repairType, Integer laborIntensity, Integer repairPeriodicity)
    {
        this.name = name;
        this.equipmentCategory = equipmentCategory;
        this.repairType = repairType;
        this.laborIntensity = laborIntensity;
        this.repairPeriodicity = repairPeriodicity;
    }

    public Long getId()
    {
        return id;
    }

    public EquipmentCategory getEquipmentCategory()
    {
        return equipmentCategory;
    }

    public void setEquipmentCategory(EquipmentCategory equipmentCategory)
    {
        this.equipmentCategory = equipmentCategory;
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

    public Integer getLaborIntensity()
    {
        return laborIntensity;
    }

    public void setLaborIntensity(Integer laborIntensity)
    {
        this.laborIntensity = laborIntensity;
    }

    public Integer getRepairPeriodicity()
    {
        return repairPeriodicity;
    }

    public void setRepairPeriodicity(Integer repairPeriodicity)
    {
        this.repairPeriodicity = repairPeriodicity;
    }
}
