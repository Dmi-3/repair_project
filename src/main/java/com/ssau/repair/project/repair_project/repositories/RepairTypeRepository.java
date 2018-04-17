package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.RepairType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairTypeRepository extends JpaRepository<RepairType, Long>
{
    List<RepairType> findByName(String name);

    RepairType getById(Long id);
}
