package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Maintenance_schedule")
public class MaintenanceSchedule implements Serializable
{
    @Id
    @SequenceGenerator(name = "maintenance_schedule_sequence", sequenceName = "maintenance_schedule_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "maintenance_schedule_sequence")
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "repair_type_id")
    private RepairType repairType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "repair_standard_id")
    private RepairStandard repairStandard;

    @Column(name = "date")
    private LocalDateTime date;

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

    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    public RepairStandard getRepairStandard()
    {
        return repairStandard;
    }

    public void setRepairStandard(RepairStandard repairStandard)
    {
        this.repairStandard = repairStandard;
    }
}
