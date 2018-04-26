package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WorkerRepository extends JpaRepository<Worker, Long>
{
    List<Worker> findByName(String name);

    Worker getById(Long id);

}
