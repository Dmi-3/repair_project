package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QualificationRepository extends JpaRepository<Qualification, Long>
{
    List<Qualification> findByName(String name);

    Qualification getById(Long id);
}
