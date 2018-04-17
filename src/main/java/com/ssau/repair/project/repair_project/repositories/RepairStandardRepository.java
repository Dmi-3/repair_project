package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.RepairStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairStandardRepository extends JpaRepository<RepairStandard, Long>
{
    List<RepairStandard> findByName(String name);

    RepairStandard getById(Long id);
}
