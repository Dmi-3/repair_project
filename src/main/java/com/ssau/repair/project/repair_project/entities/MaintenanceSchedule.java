package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToOne
    @JoinColumn(name = "repair_type_id")
    private RepairType repairType;

    @ManyToMany
    @JoinColumn(name = "workers_ids")
    private Set<Worker> workers;

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    @Column(name = "date")
    private LocalDate date;

    public MaintenanceSchedule()
    {

    }

    public MaintenanceSchedule(Equipment equipment, RepairType repairType, Set<Worker> workers, Integer laborIntensity, LocalDate date)
    {
        this.equipment = equipment;
        this.repairType = repairType;
        this.workers = workers;
        this.laborIntensity = laborIntensity;
        this.date = date;
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


    public Set<Worker> getWorkers()
    {
        return workers;
    }

    public void setWorkers(Set<Worker> workers)
    {
        this.workers = workers;
    }

    public Integer getLaborIntensity()
    {
        return laborIntensity;
    }

    public void setLaborIntensity(Integer laborIntensity)
    {
        this.laborIntensity = laborIntensity;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Set<Long> getWorkersIds()
    {
        return getWorkers().stream().map(Worker::getId).collect(Collectors.toSet());
    }

    public Set<String> getWorkersNames()
    {
        return getWorkers().stream().map(Worker::getName).collect(Collectors.toSet());
    }
}
