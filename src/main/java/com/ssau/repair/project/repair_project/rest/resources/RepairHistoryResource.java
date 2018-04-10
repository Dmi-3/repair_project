package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.RepairHistory;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.RepairHistoryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/equipmentsCategory")
public class RepairHistoryResource
{
    private static final Logger LOG = Logger.getLogger(RepairHistoryResource.class);

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final RepairHistoryRepository repairHistoryRepository;

    @Autowired
    public RepairHistoryResource(MaintenanceScheduleRepository maintenanceScheduleRepository,
                                 RepairHistoryRepository repairHistoryRepository)
    {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.repairHistoryRepository = repairHistoryRepository;
    }

    @GetMapping("/all")
    public List<RepairHistory> getAll()
    {
        try
        {
            return repairHistoryRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair history objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairHistory getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairHistoryRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair history object with id." + id, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("scheduleId") Long scheduleId)
    {
        if (scheduleId == null)
        {
            return "Id of the maintenance schedule object wasn't found.";
        }

        try
        {
            MaintenanceSchedule maintenanceSchedule = maintenanceScheduleRepository.getOne(scheduleId);
            RepairHistory repairHistory = new RepairHistory();
            repairHistory.setEquipment(maintenanceSchedule.getEquipment());
            repairHistory.setRepairType(maintenanceSchedule.getRepairType());
            repairHistoryRepository.save(repairHistory);
            return "The repair history object was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the repair history object.", ex);
            return "Error: the repair history object wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String delete(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The repair history object id wasn't found";
        }

        try
        {
            RepairHistory repairHistory = getById(id);

            if (repairHistory == null)
            {
                return "The repair history object with id " + id + " wasn't found";
            }

            repairHistoryRepository.delete(repairHistory);
            return "The repair history object with id " + id + " was deleted";
        }
        catch (Exception ex)
        {
            {
                LOG.error("An error occurred during removing the repair history object.", ex);
                return "Error: the repair history object wasn't removed.";
            }
        }
    }
}
