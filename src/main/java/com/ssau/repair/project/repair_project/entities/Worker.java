package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Worker")
public class Worker implements Comparable<Worker>
{
    @Id
    @SequenceGenerator(name = "worker_sequence", sequenceName = "worker_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "worker_sequence")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "TariffRate")
    private Integer tariffRate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "worker_qualifications", joinColumns = @JoinColumn(name = "worker_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "qualification_id", referencedColumnName = "id"))
    private Set<Qualification> qualifications;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker")
    private Set<MaintenanceSchedule> maintenanceSchedules;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker")
    private Set<WorkerSchedule> workerSchedules;

    public Worker()
    {

    }

    public Worker(String name, Integer tariffRate,Set<Qualification> qualifications)
    {
        this.name = name;
        this.tariffRate = tariffRate;
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

    public Integer getTariffRate()
    {
        return tariffRate;
    }

    public void setTariffRate(Integer tariffRate)
    {
        this.tariffRate = tariffRate;
    }

    public Set<WorkerSchedule> getWorkerSchedules()
    {
        return workerSchedules;
    }

    public void setWorkerSchedules(Set<WorkerSchedule> workerSchedules)
    {
        this.workerSchedules = workerSchedules;
    }

    @Override
    public int compareTo(Worker o)
    {
        return tariffRate.compareTo(o.getTariffRate());
    }
}
