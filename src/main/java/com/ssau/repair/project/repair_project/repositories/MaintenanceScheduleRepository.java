package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long>
{
}
