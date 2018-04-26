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

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "worker")
    private Worker worker;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenanceSchedule")
    private Set<WorkerSchedule> workerSchedules;

    public MaintenanceSchedule()
    {

    }

    public MaintenanceSchedule(Equipment equipment, RepairType repairType, Worker worker, Integer laborIntensity, LocalDate date)
    {
        this.equipment = equipment;
        this.repairType = repairType;
        this.worker = worker;
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

    public Worker getWorker()
    {
        return worker;
    }

    public void setWorker(Worker worker)
    {
        this.worker = worker;
    }

    public Set<WorkerSchedule> getWorkerSchedules()
    {
        return workerSchedules;
    }

    public void setWorkerSchedules(Set<WorkerSchedule> workerSchedules)
    {
        this.workerSchedules = workerSchedules;
    }

   /* public Set<Long> getWorkersIds()
    {
        return getWorkers().stream().map(Worker::getId).collect(Collectors.toSet());
    }

    public Set<String> getWorkersNames()
    {
        return getWorkers().stream().map(Worker::getName).collect(Collectors.toSet());
    }*/


}
