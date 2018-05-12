package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.entities.*;
import com.ssau.repair.project.repair_project.repositories.*;
import com.ssau.repair.project.repair_project.util.Pair;
import com.ssau.repair.project.repair_project.util.Utils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@EnableScheduling
public class MainController
{
    private static final Integer WORK_DAY = 8;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final RepairStandardRepository repairStandardRepository;
    private final Utils utils;
    private final WorkerScheduleRepository workerScheduleRepository;

    public MainController(EquipmentRepository equipmentRepository, MaintenanceScheduleRepository maintenanceScheduleRepository,
                          RepairStandardRepository repairStandardRepository, RepairHistoryRepository repairHistoryRepository,
                          WorkerScheduleRepository workerScheduleRepository, WorkerScheduleRepository workerScheduleRepository1)
    {
        this.equipmentRepository = equipmentRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.repairStandardRepository = repairStandardRepository;
        this.workerScheduleRepository = workerScheduleRepository1;
        this.utils = new Utils(repairHistoryRepository, workerScheduleRepository, maintenanceScheduleRepository);
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model)
    {
        return "index";
    }

    @Scheduled(cron = "30 26 22 * * *")
    public void autoCheckAndStartMaintenance()
    {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        if (equipmentList.isEmpty())
        {
            //LOG
            return;
        }

        Map<Equipment, Pair<RepairType, Integer>> plannedMaintenanceMap = utils.getRepairMap(equipmentList);

        for (Map.Entry<Equipment, Pair<RepairType, Integer>> plannedMaintenance : plannedMaintenanceMap.entrySet())
        {
            if (plannedMaintenance == null)
            {
                continue;
            }

            Integer daysToRepair = plannedMaintenance.getValue().getValue();
            if (daysToRepair <= 0)
            {
                MaintenanceSchedule maintenanceSchedule = planMaintenance(plannedMaintenance.getKey(), plannedMaintenance.getValue().getKey());

                if (maintenanceSchedule == null)
                {
                    continue;
                }

                maintenanceScheduleRepository.save(maintenanceSchedule);
                utils.saveInWorkerSchedule(maintenanceSchedule, 8);
            }
        }
    }


    private MaintenanceSchedule planMaintenance(Equipment equipment, RepairType repairType)
    {
        if (equipment == null || repairType == null)
        {
            return null;
        }

        if (utils.isPlanned(equipment, repairType))
        {
            return null;
        }

        RepairStandard repairStandard = repairStandardRepository
                .getRepairStandardByEquipmentCategoryAndRepairType(equipment.getEquipmentCategory(), repairType);

        if (repairStandard == null)
        {
            return null;
        }

        List<Worker> workers = utils.getWorkersByRepairType(repairType);

        if (workers == null || workers.isEmpty())
        {
            return null;
        }

        Worker worker = null;
        LocalDate date = LocalDate.now();

        while (worker == null)
        {
            worker = getFittingWorker(workers, date);
            if (worker == null)
            {
                date = date.plusDays(1);
            }
        }

        return new MaintenanceSchedule(equipment, repairType, worker, repairStandard.getLaborIntensity(), date);
    }

    private Worker getFittingWorker(List<Worker> workers, LocalDate date)
    {
        if (workers == null || date == null)
        {
            return null;
        }

        List<Worker> fittingWorkers = new ArrayList<>();
        for (Worker worker : workers)
        {
            Set<WorkerSchedule> workerScheduleSet = workerScheduleRepository.getByWorkerAndDate(worker, date);

            if (workerScheduleSet == null || workerScheduleSet.isEmpty())
            {
                fittingWorkers.add(worker);
                continue;
            }

            Integer manHours = 0;

            for (WorkerSchedule workerSchedule : workerScheduleSet)
            {
                if (workerSchedule == null)
                {
                    continue;
                }

                manHours += (workerSchedule.getLaborIntensity() == null) ? 0 : workerSchedule.getLaborIntensity();
            }

            if (manHours >= WORK_DAY)
            {
                continue;
            }
            fittingWorkers.add(worker);
        }

        if (fittingWorkers.isEmpty())
        {
            return null;
        }

        return Collections.min(fittingWorkers);
    }
}
