package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{
    List<Equipment> findByName(String name);
}
