package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long>
{
    MaintenanceSchedule getById(Long id);

    MaintenanceSchedule getByEquipmentAndRepairType(Equipment equipment, RepairType repairType);

    MaintenanceSchedule getByWorkerAndDate(Worker worker, LocalDate date);
}
