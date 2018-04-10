package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.RepairStandards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairStandardsRepository extends JpaRepository<RepairStandards, Long> {
    @Override
    List<RepairStandards> findAll();

    List<RepairStandards> findByName(String name);
}
