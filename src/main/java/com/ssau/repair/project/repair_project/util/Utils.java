package com.ssau.repair.project.repair_project.util;

import com.ssau.repair.project.repair_project.entities.*;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.RepairHistoryRepository;
import com.ssau.repair.project.repair_project.repositories.WorkerScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class Utils
{
    private RepairHistoryRepository repairHistoryRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private MaintenanceScheduleRepository maintenanceScheduleRepository;

    public Utils(RepairHistoryRepository repairHistoryRepository, WorkerScheduleRepository workerScheduleRepository,
                 MaintenanceScheduleRepository maintenanceScheduleRepository)
    {
        this.repairHistoryRepository = repairHistoryRepository;
        this.workerScheduleRepository = workerScheduleRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
    }

    public Map<Equipment, Pair<RepairType, Integer>> getRepairMap(List<Equipment> equipments)
    {
        if (equipments == null || equipments.isEmpty())
        {
            return null;
        }

        Map<Equipment, Pair<RepairType, Integer>> repairMap = new HashMap<>();
        for (Equipment equipment : equipments)
        {
            Set<RepairStandard> repairStandards = equipment.getEquipmentCategory().getRepairStandards();

            if (repairStandards == null || repairStandards.isEmpty())
            {
                continue;
            }

            for (RepairStandard repairStandard : repairStandards)
            {
                if (repairStandard == null)
                {
                    continue;
                }

                if (isPlanned(equipment, repairStandard.getRepairType()))
                {
                    continue;
                }

                Integer daysToRepair = getDaysToRepair(equipment, repairStandard);
                repairMap.put(equipment, new Pair<>(repairStandard.getRepairType(), daysToRepair));
            }
        }
        return repairMap;
    }

    private Integer getDaysToRepair(Equipment equipment, RepairStandard repairStandard)
    {
        Integer repairPeriodicity = repairStandard.getRepairPeriodicity();

        if (repairPeriodicity == null || repairPeriodicity <= 0)
        {
            return 0;
        }

        List<RepairHistory> repairHistory = repairHistoryRepository
                .getByEquipmentAndRepairTypeOrderByDate(equipment, repairStandard.getRepairType());

        if (repairHistory == null || repairHistory.isEmpty())
        {
            return 0;
        }

        LocalDate repairDate = repairHistory.get(0).getDate().plusDays(repairPeriodicity);

        return (int) DAYS.between(LocalDate.now(), repairDate);
    }

    public List<Worker> getWorkersByRepairType(RepairType repairType)
    {
        Set<Qualification> qualifications = repairType.getQualifications();

        if (qualifications == null || qualifications.isEmpty())
        {
            return null;
        }

        List<Worker> workers = new ArrayList<>();

        for (Qualification qualification : qualifications)
        {
            if (qualification == null)
            {
                continue;
            }

            workers.addAll(qualification.getWorkers());
        }
        return workers;
    }

    public void saveInWorkerSchedule(MaintenanceSchedule maintenanceSchedule, Integer workDay)
    {
        Worker worker = maintenanceSchedule.getWorker();
        Integer laborIntensity = maintenanceSchedule.getLaborIntensity();
        Integer days = 0;
        while (laborIntensity > 0)
        {
            WorkerSchedule workerSchedule;
            LocalDate date = maintenanceSchedule.getDate().plusDays(days);
            Integer freeTime = workDay - getWorkHours(workerScheduleRepository.getByWorkerAndDate(worker, date));

            if (freeTime <= 0)
            {
                days++;
                continue;
            }

            if (laborIntensity >= freeTime)
            {
                workerSchedule = new WorkerSchedule(worker, date, freeTime, maintenanceSchedule);
                laborIntensity -= freeTime;
                days++;
            }
            else
            {
                workerSchedule = new WorkerSchedule(worker, maintenanceSchedule.getDate().plusDays(days), laborIntensity, maintenanceSchedule);
                laborIntensity = 0;
            }
            workerScheduleRepository.save(workerSchedule);
        }
    }

    private Integer getWorkHours(Set<WorkerSchedule> workerSchedules)
    {
        Integer workHours = 0;

        if (workerSchedules == null || workerSchedules.isEmpty())
        {
            return workHours;
        }

        for (WorkerSchedule workerSchedule : workerSchedules)
        {
            if (workerSchedule == null)
            {
                continue;
            }

            workHours = +0;
        }

        return workHours;
    }

    public boolean isPlanned(Equipment equipment, RepairType repairType)
    {
        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getByEquipmentAndRepairType(equipment, repairType);

        return maintenanceSchedule != null;
    }
}
