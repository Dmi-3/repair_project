package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Worker_Schedule")
public class WorkerSchedule
{
    @Id
    @SequenceGenerator(name = "worker_schedule_sequence", sequenceName = "worker_schedule_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "worker_schedule_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    @ManyToOne
    @JoinColumn(name = "maintenanceSchedule")
    private MaintenanceSchedule maintenanceSchedule;


    @ManyToOne
    @JoinColumn(name = "worker")
    private Worker worker;

    public WorkerSchedule()
    {

    }

    public WorkerSchedule(Worker worker, LocalDate date, Integer laborIntensity, MaintenanceSchedule maintenanceSchedule)
    {
        this.worker = worker;
        this.date = date;
        this.laborIntensity = laborIntensity;
        this.maintenanceSchedule = maintenanceSchedule;
    }

    public Long getId()
    {
        return id;
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

    public MaintenanceSchedule getMaintenanceSchedule()
    {
        return maintenanceSchedule;
    }

    public void setMaintenanceSchedule(MaintenanceSchedule maintenanceSchedule)
    {
        this.maintenanceSchedule = maintenanceSchedule;
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
