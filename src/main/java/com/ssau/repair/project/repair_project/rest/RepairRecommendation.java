package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.RepairHistoryRepository;
import com.ssau.repair.project.repair_project.repositories.WorkerScheduleRepository;
import com.ssau.repair.project.repair_project.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

@Controller
@RequestMapping("/repair-recommendation")
public class RepairRecommendation
{
    private static final Logger LOG = Logger.getLogger(RepairRecommendation.class);

    private final EquipmentRepository equipmentRepository;
    private final Utils utils;

    public RepairRecommendation(EquipmentRepository equipmentRepository, RepairHistoryRepository repairHistoryRepository,
                                WorkerScheduleRepository workerScheduleRepository, MaintenanceScheduleRepository maintenanceScheduleRepository)
    {
        this.equipmentRepository = equipmentRepository;
        this.utils = new Utils(repairHistoryRepository, workerScheduleRepository, maintenanceScheduleRepository);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("repairMap", utils.getRepairMap(equipmentRepository.findAll()));
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair recommendations.", ex);
            model.addAttribute("equipmentsCategories", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all equipments categories objects.");
        }
        return "schedules/repair-recommendation";
    }
}
