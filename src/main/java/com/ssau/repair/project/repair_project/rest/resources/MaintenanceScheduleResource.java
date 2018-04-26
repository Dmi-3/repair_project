package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.entities.Worker;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import com.ssau.repair.project.repair_project.repositories.WorkerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/maintenance-schedule")
public class MaintenanceScheduleResource
{
    private static final Logger LOG = Logger.getLogger(MaintenanceScheduleResource.class);

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final WorkerRepository workerRepository;
    private final EquipmentRepository equipmentRepository;
    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public MaintenanceScheduleResource(MaintenanceScheduleRepository maintenanceScheduleRepository,
                                       WorkerRepository workerRepository, EquipmentRepository equipmentRepository,
                                       RepairTypeRepository repairTypeRepository)
    {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.equipmentRepository = equipmentRepository;
        this.repairTypeRepository = repairTypeRepository;
        this.workerRepository = workerRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("maintenanceSchedule", maintenanceScheduleRepository.findAll());
            model.addAttribute("equipments", equipmentRepository.findAll());
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
            model.addAttribute("workers", workerRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all maintenance schedules objects.", ex);
            model.addAttribute("maintenanceSchedule", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all  maintenance schedules objects.");
        }
        return "admin_functions/data_base/maintenance-schedule";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam("equipmentId") Long equipmentId,
                         @RequestParam("repairTypeId") Long repairTypeId,
                         @RequestParam("workerId") Long workerId,
                         @RequestParam("laborIntensity") Integer laborIntensity,
                         @RequestParam("date") String date,
                         final RedirectAttributes redirectAttributes)
    {
        if (equipmentId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment id of the maintenance schedule object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the maintenance schedule object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the maintenance schedule.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (workerId == null )
        {
            redirectAttributes.addFlashAttribute("warning", "The worker object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        try
        {
            Equipment equipment = equipmentRepository.getById(equipmentId);
            if (equipment == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
                return getRedirectMaintenanceSchedulesPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);
            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type object wasn't found.");
                return getRedirectMaintenanceSchedulesPage();
            }

            Worker worker = workerRepository.getById(workerId);

            if (worker == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The worker object wasn't found.");
                return getRedirectMaintenanceSchedulesPage();
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            if (localDate == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
                return getRedirectMaintenanceSchedulesPage();
            }

            MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(equipment, repairType, worker, laborIntensity, localDate);
            maintenanceScheduleRepository.save(maintenanceSchedule);
            redirectAttributes.addFlashAttribute("success", "The maintenance schedule was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the maintenance schedule.", ex);
            redirectAttributes.addFlashAttribute("error", "The the maintenance schedule object wasn't created.");
        }
        return getRedirectMaintenanceSchedulesPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "maintenanceSchedulesIds", required = false) String[] maintenanceSchedulesIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (maintenanceSchedulesIds == null || maintenanceSchedulesIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The schedules objects for remove wasn't found, please select " +
                    "interesting you schedules objects for remove by a checkbox.");
            return getRedirectMaintenanceSchedulesPage();
        }

        try
        {
            for (String id : maintenanceSchedulesIds)
            {
                MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(Long.parseLong(id));

                if (maintenanceSchedule == null)
                {
                    continue;
                }

                maintenanceScheduleRepository.delete(maintenanceSchedule);
            }
            redirectAttributes.addFlashAttribute("success", "The maintenance schedule object(s) with ids: " + Arrays.toString(maintenanceSchedulesIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the maintenance schedule object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the maintenance schedule object(s).");
        }
        return getRedirectMaintenanceSchedulesPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam("equipmentId") Long equipmentId,
                         @RequestParam("repairTypeId") Long repairTypeId,
                         @RequestParam("laborIntensity") Integer laborIntensity,
                         @RequestParam("workerId") Long workerId,
                         @RequestParam("date") String date,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the maintenance schedule wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (equipmentId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment id of the maintenance schedule object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the maintenance schedule object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the maintenance schedule.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (workerId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(id);

        if (maintenanceSchedule == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The maintenance schedule with id" + id + "wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        RepairType repairType = repairTypeRepository.getById(repairTypeId);

        if (repairType == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        Equipment equipment = equipmentRepository.getById(equipmentId);
        if (equipment == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        Worker worker = workerRepository.getById(workerId);

        if (worker == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        maintenanceSchedule.setEquipment(equipment);
        maintenanceSchedule.setRepairType(repairType);
        maintenanceSchedule.setLaborIntensity(laborIntensity);
        maintenanceSchedule.setWorker(worker);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        if (localDate == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMaintenanceSchedulesPage();
        }

        maintenanceSchedule.setDate(localDate);

        maintenanceScheduleRepository.save(maintenanceSchedule);
        redirectAttributes.addFlashAttribute("success", "The qualification with id" + id + " was changed.");
        return getRedirectMaintenanceSchedulesPage();
    }

    private String getRedirectMaintenanceSchedulesPage()
    {
        return "redirect:/maintenance-schedule";
    }
}
