package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Worker")
public class Worker
{
    @Id
    @SequenceGenerator(name = "worker_sequence", sequenceName = "worker_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "worker_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @ManyToMany
    @Column(name = "Qualification")
    private Set<Qualification> qualifications;

    @ManyToMany
    private Set<MaintenanceSchedule> maintenanceSchedules;

    public Worker()
    {

    }

    public Worker(String name, Set<Qualification> qualifications)
    {
        this.name = name;
        this.qualifications = qualifications;
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

    public Set<Qualification> getQualifications()
    {
        return qualifications;
    }

    public void setQualifications(Set<Qualification> qualifications)
    {
        this.qualifications = qualifications;
    }

    public Set<MaintenanceSchedule> getMaintenanceSchedules()
    {
        return maintenanceSchedules;
    }

    public void setMaintenanceSchedules(Set<MaintenanceSchedule> maintenanceSchedules)
    {
        this.maintenanceSchedules = maintenanceSchedules;
    }

    public Set<Long> getQualificationsIds()
    {
        return getQualifications().stream().map(Qualification::getId).collect(Collectors.toSet());
    }

    public Set<String> getQualificationsNames()
    {
        return getQualifications().stream().map(Qualification::getName).collect(Collectors.toSet());
    }
}
