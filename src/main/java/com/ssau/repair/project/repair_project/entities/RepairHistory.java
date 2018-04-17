package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Repair_type_id")
    private RepairType repairType;

    @Column(name = "Date")
    private LocalDateTime date;

    public RepairHistory()
    {
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

    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

}
