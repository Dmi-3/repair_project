package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.entities.*;
import com.ssau.repair.project.repair_project.repositories.*;
import org.apache.log4j.Logger;
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
import java.util.Set;

@Controller
@RequestMapping("/main-schedule")
public class MainSchedule
{
    private static final Logger LOG = Logger.getLogger(MainSchedule.class);

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final WorkerRepository workerRepository;
    private final EquipmentRepository equipmentRepository;
    private final RepairTypeRepository repairTypeRepository;
    private final RepairStandardRepository repairStandardRepository;
    private final RepairHistoryRepository repairHistoryRepository;
    private final WorkerScheduleRepository workerScheduleRepository;

    public MainSchedule(MaintenanceScheduleRepository maintenanceScheduleRepository, WorkerRepository workerRepository, EquipmentRepository equipmentRepository, RepairTypeRepository repairTypeRepository, RepairStandardRepository repairStandardRepository, RepairHistoryRepository repairHistoryRepository, WorkerScheduleRepository workerScheduleRepository)
    {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.equipmentRepository = equipmentRepository;
        this.repairTypeRepository = repairTypeRepository;
        this.workerRepository = workerRepository;
        this.repairStandardRepository = repairStandardRepository;
        this.repairHistoryRepository = repairHistoryRepository;
        this.workerScheduleRepository = workerScheduleRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("maintenanceSchedule", maintenanceScheduleRepository.findAll());
            model.addAttribute("equipments", equipmentRepository.findAll());
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
            //model.addAttribute("workers", workerRepository.findAll());
            model.addAttribute("repairStandards", repairStandardRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all maintenance schedules objects.", ex);
            model.addAttribute("maintenanceSchedule", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all  maintenance schedules objects.");
        }
        return "schedules/main-schedule";
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
            return getRedirectMainSchedulesPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the maintenance schedule object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the maintenance schedule.");
            return getRedirectMainSchedulesPage();
        }

        if (workerId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        try
        {
            Equipment equipment = equipmentRepository.getById(equipmentId);
            if (equipment == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
                return getRedirectMainSchedulesPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);
            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type object wasn't found.");
                return getRedirectMainSchedulesPage();
            }

            Worker worker = workerRepository.getById(workerId);

            if (worker == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
                return getRedirectMainSchedulesPage();
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            if (localDate == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
                return getRedirectMainSchedulesPage();
            }

            MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(equipment, repairType, worker, laborIntensity, localDate);
            maintenanceScheduleRepository.save(maintenanceSchedule);
            saveInWorkerSchedule(maintenanceSchedule, 8);
            redirectAttributes.addFlashAttribute("success", "The maintenance schedule was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the maintenance schedule.", ex);
            redirectAttributes.addFlashAttribute("error", "The maintenance schedule object wasn't created.");
        }
        return getRedirectMainSchedulesPage();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "mainSchedulesIds", required = false) String[] mainSchedulesIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (mainSchedulesIds == null || mainSchedulesIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The schedules objects for execute wasn't found, please select " +
                    "interesting you schedules objects for remove by a checkbox.");
            return getRedirectMainSchedulesPage();
        }

        try
        {
            for (String id : mainSchedulesIds)
            {
                MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(Long.parseLong(id));

                if (maintenanceSchedule == null)
                {
                    continue;
                }

                RepairHistory repairHistory = new RepairHistory(maintenanceSchedule);

                repairHistoryRepository.save(repairHistory);
                maintenanceScheduleRepository.delete(maintenanceSchedule);
            }
            redirectAttributes.addFlashAttribute("success", "The maintenance schedule object(s) with ids: " + Arrays.toString(mainSchedulesIds) + " were execute and sent to repair history.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the maintenance schedule object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the maintenance schedule object(s).");
        }
        return getRedirectMainSchedulesPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "maintenaceScheduleId", required = false) Long maintenaceScheduleId,
                         @RequestParam(value = "equipmentId", required = false) Long equipmentId,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         @RequestParam(value = "laborIntensity", required = false) Integer laborIntensity,
                         @RequestParam(value = "workerId", required = false) Long workerId,
                         @RequestParam(value = "date", required = false) String date,
                         final RedirectAttributes redirectAttributes)
    {
        if (maintenaceScheduleId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the maintenance schedule wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (equipmentId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment id of the maintenance schedule object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the maintenance schedule object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the maintenance schedule.");
            return getRedirectMainSchedulesPage();
        }

        if (workerId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(maintenaceScheduleId);

        if (maintenanceSchedule == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The maintenance schedule with id" + maintenaceScheduleId + "wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        RepairType repairType = repairTypeRepository.getById(repairTypeId);

        if (repairType == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        Equipment equipment = equipmentRepository.getById(equipmentId);
        if (equipment == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        Worker worker = workerRepository.getById(workerId);

        if (worker == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment object wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        if (localDate == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the maintenance schedule wasn't found.");
            return getRedirectMainSchedulesPage();
        }

        Integer oldLaborIntesity = maintenanceSchedule.getLaborIntensity();

        maintenanceSchedule.setEquipment(equipment);
        maintenanceSchedule.setRepairType(repairType);
        maintenanceSchedule.setLaborIntensity(laborIntensity);
        maintenanceSchedule.setWorker(worker);

        maintenanceSchedule.setDate(localDate);

            maintenanceScheduleRepository.save(maintenanceSchedule);

        if (!oldLaborIntesity.equals(laborIntensity))
        {
            for (WorkerSchedule workerSchedule : maintenanceSchedule.getWorkerSchedules())
            {
                workerScheduleRepository.delete(workerSchedule);
            }

            saveInWorkerSchedule(maintenanceSchedule, 8);
        }

        redirectAttributes.addFlashAttribute("success", "The maintenance schedule with id" + maintenaceScheduleId + " was changed.");
        return getRedirectMainSchedulesPage();
    }

    private void saveInWorkerSchedule(MaintenanceSchedule maintenanceSchedule, Integer workDay)
    {
        Worker worker = maintenanceSchedule.getWorker();
        Integer laborIntensity = maintenanceSchedule.getLaborIntensity();
        Integer days = 0;
        while (laborIntensity > 0)
        {
            WorkerSchedule workerSchedule = null;
            LocalDate date = maintenanceSchedule.getDate().plusDays(days);
            Integer freeTime = workDay - getWorkHours(workerScheduleRepository.getByIdAndDate(worker.getId(), date));

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

    private String getRedirectMainSchedulesPage()
    {
        return "redirect:/main-schedule";
    }
}
