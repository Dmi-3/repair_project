package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long>
{
    @Override
    List<MaintenanceSchedule> findAll();
}
