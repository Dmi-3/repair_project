package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Equipment")
public class Equipment implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_category_id", referencedColumnName = "id", nullable = false)
    private EquipmentCategory equipmentCategory;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private Set<MaintenanceSchedule> maintenanceSchedules;

    public Equipment()
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

    public EquipmentCategory getEquipmentCategory()
    {
        return equipmentCategory;
    }

    public void setEquipmentCategory(EquipmentCategory equipmentCategory)
    {
        this.equipmentCategory = equipmentCategory;
    }

    public Set<MaintenanceSchedule> getMaintenanceSchedules()
    {
        return maintenanceSchedules;
    }

    public void setMaintenanceSchedules(Set<MaintenanceSchedule> maintenanceSchedules)
    {
        this.maintenanceSchedules = maintenanceSchedules;
    }

    @Override
    public String toString()
    {
        return "id: " + this.getId() + " name: " + this.getName() + "category: " + this.getEquipmentCategory().getName();
    }
}
