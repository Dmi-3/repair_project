package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.Worker;
import com.ssau.repair.project.repair_project.entities.WorkerSchedule;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.WorkerRepository;
import com.ssau.repair.project.repair_project.repositories.WorkerScheduleRepository;
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

@Controller
@RequestMapping("/worker-schedule")
public class WorkerScheduleResource
{
    private static final Logger LOG = Logger.getLogger(WorkerScheduleResource.class);

    private final WorkerScheduleRepository workerScheduleRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public WorkerScheduleResource(WorkerScheduleRepository workerScheduleRepository, MaintenanceScheduleRepository maintenanceScheduleRepository,
                                  WorkerRepository workerRepository1)
    {
        this.workerScheduleRepository = workerScheduleRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.workerRepository = workerRepository1;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("workerSchedule", workerScheduleRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all worker schedule objects.", ex);
            model.addAttribute("workerSchedule", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all worker schedule objects.");
        }
        return "admin_functions/data_base/worker-schedule";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "workerId", required = false) Long workerId,
                         @RequestParam(value = "date", required = false) String dateStr,
                         @RequestParam(value = "laborIntensity", required = false) Integer laborIntensity,
                         @RequestParam(value = "maintenaceScheduleId", required = false) Long maintenanceScheduleId,
                         final RedirectAttributes redirectAttributes)
    {
        if (workerId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker id wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (dateStr == null || dateStr.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The date wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (laborIntensity == null || laborIntensity < 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of the labor intensity wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (maintenanceScheduleId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The maintenance scheduleId id wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        try
        {
            Worker worker = workerRepository.getById(workerId);

            if (worker == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The worker wasn't found.");
                return getRedirectWorkerSchedulePage();
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(maintenanceScheduleId);
            WorkerSchedule workerSchedule = new WorkerSchedule(worker, date, laborIntensity, maintenanceSchedule);

            workerScheduleRepository.save(workerSchedule);
            redirectAttributes.addFlashAttribute("success", "The worker schedule was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the worker.", ex);
            redirectAttributes.addFlashAttribute("error", "The worker object wasn't created.");
        }
        return getRedirectWorkerSchedulePage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "workerScheduleIds", required = false) String[] workerScheduleIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (workerScheduleIds == null || workerScheduleIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker schedule objects for remove wasn't found, please select " +
                    "interesting you worker schedule for remove by a checkbox.");
            return getRedirectWorkerSchedulePage();
        }

        try
        {
            for (String id : workerScheduleIds)
            {
                WorkerSchedule workerSchedule = workerScheduleRepository.getById(Long.parseLong(id));

                if (workerSchedule == null)
                {
                    continue;
                }

                workerScheduleRepository.delete(workerSchedule);
            }
            redirectAttributes.addFlashAttribute("success", "The worker schedule object(s) with ids: " + Arrays.toString(workerScheduleIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the worker schedule object(s).", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the worker schedule object(s).");
        }
        return getRedirectWorkerSchedulePage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "workerId", required = false) Long workerId,
                         @RequestParam(value = "date", required = false) String dateStr,
                         @RequestParam(value = "laborIntensity", required = false) Integer laborIntensity,
                         @RequestParam(value = "maintenaceScheduleId", required = false) Long maintenanceScheduleId,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the worker schedule wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (workerId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker id wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (dateStr == null || dateStr.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The date wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (laborIntensity == null || laborIntensity < 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of the labor intensity wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        if (maintenanceScheduleId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The maintenance scheduleId id wasn't found.");
            return getRedirectWorkerSchedulePage();
        }

        WorkerSchedule workerSchedule = workerScheduleRepository.getById(id);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);

        MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getById(maintenanceScheduleId);

        workerSchedule.setDate(date);
        workerSchedule.setLaborIntensity(laborIntensity);
        workerSchedule.setMaintenanceSchedule(maintenanceSchedule);

        workerScheduleRepository.save(workerSchedule);
        redirectAttributes.addFlashAttribute("success", "The worker schedule with id " + id + " was changed.");
        return getRedirectWorkerSchedulePage();
    }

    private String getRedirectWorkerSchedulePage()
    {
        return "redirect:/worker-schedule";
    }
}
