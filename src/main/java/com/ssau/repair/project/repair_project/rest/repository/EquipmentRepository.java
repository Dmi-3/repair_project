package com.ssau.repair.project.repair_project.rest.repository;

import com.ssau.repair.project.repair_project.ao.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment,Integer>
{
    @Override
    List<Equipment> findAll();

    List<Equipment> findByName(String name);

}
