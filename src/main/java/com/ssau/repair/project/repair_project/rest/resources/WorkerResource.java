package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Qualification;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.entities.Worker;
import com.ssau.repair.project.repair_project.repositories.*;
import com.ssau.repair.project.repair_project.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/workers")
public class WorkerResource
{
    private static final Logger LOG = Logger.getLogger(WorkerResource.class);

    private final WorkerRepository workerRepository;
    private final QualificationRepository qualificationRepository;
    private final RepairTypeRepository repairTypeRepository;
    private final Utils utils;

    @Autowired
    public WorkerResource(WorkerRepository workerRepository, QualificationRepository qualificationRepository,
                          RepairTypeRepository repairTypeRepository, RepairHistoryRepository repairHistoryRepository,
                          WorkerScheduleRepository workerScheduleRepository, MaintenanceScheduleRepository maintenanceScheduleRepository)
    {
        this.workerRepository = workerRepository;
        this.qualificationRepository = qualificationRepository;
        this.repairTypeRepository = repairTypeRepository;
        this.utils = new Utils(repairHistoryRepository, workerScheduleRepository, maintenanceScheduleRepository);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("workers", workerRepository.findAll());
            model.addAttribute("qualifications", qualificationRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all workers objects.", ex);
            model.addAttribute("workers", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all workers objects.");
        }
        return "admin_functions/data_base/workers";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "tariffRate", required = false) Integer tariffRate,
                         @RequestParam(value = "qualificationsIds", required = false) String[] qualificationsIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The name of the worker object wasn't found.");
            return getRedirectWorkersPage();
        }

        if (qualificationsIds == null || qualificationsIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The qualifications of the worker object wasn't found.");
            return getRedirectWorkersPage();
        }

        if (tariffRate == null || tariffRate < 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of worker's tariff rate.");
            return getRedirectWorkersPage();
        }

        try
        {
            Set<Qualification> qualifications = new HashSet<>();

            checkQualifications(qualificationsIds, qualifications);

            Worker worker = new Worker(name, tariffRate, qualifications);

            workerRepository.save(worker);
            redirectAttributes.addFlashAttribute("success", "The worker " + name + " was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the worker.", ex);
            redirectAttributes.addFlashAttribute("error", "The the worker object wasn't created.");
        }
        return getRedirectWorkersPage();
    }

    private void checkQualifications(String[] qualificationsIds, Set<Qualification> qualifications)
    {
        for (String qualificationId : qualificationsIds)
        {
            if (qualificationId == null)
            {
                continue;
            }

            Qualification qualification = qualificationRepository.getById(Long.parseLong(qualificationId));

            if (qualification == null)
            {
                continue;
            }

            qualifications.add(qualification);
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "workersIds", required = false) String[] workersIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (workersIds == null || workersIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker objects for remove wasn't found, please select " +
                    "interesting you workers for remove by a checkbox.");
            return getRedirectWorkersPage();
        }

        try
        {
            for (String id : workersIds)
            {
                Worker worker = workerRepository.getById(Long.parseLong(id));

                if (worker == null)
                {
                    continue;
                }

                workerRepository.delete(worker);
            }
            redirectAttributes.addFlashAttribute("success", "The workers object(s) with ids: " + Arrays.toString(workersIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the worker object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the worker object(s).");
        }
        return getRedirectWorkersPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "qualificationsIds", required = false) String[] qualificationsIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the worker wasn't found.");
            return getRedirectWorkersPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the worker with id " + id + "wasn't found.");
            return getRedirectWorkersPage();
        }

        if (qualificationsIds == null || qualificationsIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The qualifications of the worker object wasn't found.");
            return getRedirectWorkersPage();
        }

        Worker worker = workerRepository.getById(id);

        if (worker == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The worker with id " + id + "wasn't found.");
            return getRedirectWorkersPage();
        }

        Set<Qualification> qualifications = new HashSet<>();
        checkQualifications(qualificationsIds, qualifications);

        worker.setName(name);
        worker.setQualifications(qualifications);
        workerRepository.save(worker);

        redirectAttributes.addFlashAttribute("success", "The worker with id " + id + " was changed.");
        return getRedirectWorkersPage();
    }

    private String getRedirectWorkersPage()
    {
        return "redirect:/workers";
    }

    @ResponseBody
    @RequestMapping(value = "/getByRepairType", method = RequestMethod.POST, produces = "application/json")
    public HashMap<Long, String> getWorkersByRepairType(@RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                                                        @RequestParam(value = "date", required = false) String date)
    {
        if (repairTypeId == null || date == null || date.trim().isEmpty())
        {
            return null;
        }

        RepairType repairType = repairTypeRepository.getById(repairTypeId);

        if (repairType == null)
        {
            return null;
        }

        List<Worker> workers = utils.getWorkersByRepairType(repairType);

        if (workers == null || workers.isEmpty())
        {
            return null;
        }

        HashMap<Long, String> workersMap = new HashMap<>();
        for (Worker worker : workers)
        {
            workersMap.put(worker.getId(), worker.getName());
        }

        return workersMap;
    }

  /*  private HashMap<Long, String> selectUnemploedWorkers(Set<Worker> workers, String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        HashMap<Long, String> freeWorkers = new HashMap<>();

        for (Worker worker : workers)
        {
            Set<MaintenanceSchedule> maintenanceSchedules = worker.getMaintenanceSchedules();
            Long count = maintenanceSchedules.stream().filter(t -> t.getDate().equals(localDate)).count();

            if (count > 0)
            {
                continue;
            }
            freeWorkers.put(worker.getId(), worker.getName());
        }
        return freeWorkers;
    }*/
}
