package com.ssau.repair.project.repair_project.repositories;

import com.ssau.repair.project.repair_project.entities.WorkerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface WorkerScheduleRepository extends JpaRepository<WorkerSchedule, Long>
{
    WorkerSchedule getById(Long id);

    Set<WorkerSchedule> getByIdAndDate(Long id, LocalDate localDate);
}
