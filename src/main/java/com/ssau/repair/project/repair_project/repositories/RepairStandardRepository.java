package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.entities.RepairStandard;
import com.ssau.repair.project.repair_project.entities.RepairType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairStandardRepository extends JpaRepository<RepairStandard, Long>
{
    List<RepairStandard> findByName(String name);

    RepairStandard getById(Long id);

    RepairStandard getRepairStandardByEquipmentCategoryAndRepairType(EquipmentCategory equipmentCategory, RepairType repairType);
}
