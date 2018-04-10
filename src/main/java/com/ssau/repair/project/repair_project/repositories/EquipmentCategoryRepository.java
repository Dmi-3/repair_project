package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {

    @Override
    List<EquipmentCategory> findAll();

    List<EquipmentCategory> findByName(String name);
}
