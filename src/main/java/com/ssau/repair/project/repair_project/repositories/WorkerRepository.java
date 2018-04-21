package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long>
{
    List<Worker> findByName(String name);

    Worker getById(Long id);
}
