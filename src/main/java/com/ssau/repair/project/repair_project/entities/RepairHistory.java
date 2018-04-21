package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Repair_history")
public class RepairHistory implements Serializable
{
    @Id
    @SequenceGenerator(name = "repair_history_sequence", sequenceName = "repair_history_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "repair_history_sequence")
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "Repair_type_id")
    private RepairType repairType;

    @Column(name = "Equipment_downtime")
    private Integer equipmentDowntime;

    @Column(name = "Date")
    private LocalDate date;

    public RepairHistory()
    {

    }

    public RepairHistory(Equipment equipment, RepairType repairType, Integer equipmentDowntime, LocalDate date)
    {
        this.equipment = equipment;
        this.repairType = repairType;
        this.equipmentDowntime = equipmentDowntime;
        this.date = date;
    }

    public RepairHistory(MaintenanceSchedule maintenanceSchedule)
    {
        this.equipment = maintenanceSchedule.getEquipment();
        this.repairType = maintenanceSchedule.getRepairType();
        this.equipmentDowntime = maintenanceSchedule.getLaborIntensity();
        this.date = maintenanceSchedule.getDate();
    }

    public Long getId()
    {
        return id;
    }

    public Equipment getEquipment()
    {
        return equipment;
    }

    public void setEquipment(Equipment equipment)
    {
        this.equipment = equipment;
    }

    public RepairType getRepairType()
    {
        return repairType;
    }

    public void setRepairType(RepairType repairType)
    {
        this.repairType = repairType;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Integer getEquipmentDowntime()
    {
        return equipmentDowntime;
    }

    public void setEquipmentDowntime(Integer equipmentDowntime)
    {
        this.equipmentDowntime = equipmentDowntime;
    }
}
