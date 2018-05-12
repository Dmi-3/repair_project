package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.RepairHistory;
import com.ssau.repair.project.repair_project.entities.RepairType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long>
{
    RepairHistory getById(Long id);

    List<RepairHistory> getByEquipmentAndRepairTypeOrderByDate(Equipment equipment, RepairType repairType);
}
