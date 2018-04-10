package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long>
{
}
