package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Repair_periodically_standards")
public class RepairStandard implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id")
    private EquipmentCategory equipmentCategory;

    @Column(name = "Name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Repair_type_id")
    private RepairType repairType;

    @Column(name = "Repair_periodicity")
    private Integer repairPeriodicity;

    @Column(name = "Equipment_downtime")
    private Integer equipmentDowntime;

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    public RepairStandard()
    {
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

    public Integer getRepairPeriodicity()
    {
        return repairPeriodicity;
    }

    public void setRepairPeriodicity(Integer repairPeriodicity)
    {
        this.repairPeriodicity = repairPeriodicity;
    }

    public Integer getEquipmentDowntime()
    {
        return equipmentDowntime;
    }

    public void setEquipmentDowntime(Integer equipmentDowntime)
    {
        this.equipmentDowntime = equipmentDowntime;
    }

    public Integer getLaborIntensity()
    {
        return laborIntensity;
    }

    public void setLaborIntensity(Integer laborIntensity)
    {
        this.laborIntensity = laborIntensity;
    }
}
