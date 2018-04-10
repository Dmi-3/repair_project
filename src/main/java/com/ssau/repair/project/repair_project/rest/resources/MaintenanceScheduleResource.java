package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.MaintenanceSchedule;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import com.ssau.repair.project.repair_project.repositories.MaintenanceScheduleRepository;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/maintenanceSchedule")
public class MaintenanceScheduleResource
{
    private static final Logger LOG = Logger.getLogger(MaintenanceScheduleResource.class);

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final EquipmentRepository equipmentRepository;
    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public MaintenanceScheduleResource(MaintenanceScheduleRepository maintenanceScheduleRepository,
                                       EquipmentRepository equipmentRepository,
                                       RepairTypeRepository repairTypeRepository)
    {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.equipmentRepository = equipmentRepository;
        this.repairTypeRepository = repairTypeRepository;
    }

    @GetMapping("/all")
    public List<MaintenanceSchedule> getAll()
    {
        try
        {
            return maintenanceScheduleRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all maintenance schedules objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public MaintenanceSchedule getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return maintenanceScheduleRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the maintenance schedule object with id." + id, ex);
            return null;
        }
    }


    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("equipmentId") Long equipmentId, @RequestParam("repairTypeId") Long repairTypeId)
    {
        if (equipmentId == null)
        {
            return null;
        }

        if (repairTypeId == null)
        {
            return null;
        }

        try
        {
            MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule();
            Equipment equipment = equipmentRepository.getOne(equipmentId);
            RepairType repairType = repairTypeRepository.getOne(repairTypeId);

            maintenanceSchedule.setEquipment(equipment);
            maintenanceSchedule.setRepairType(repairType);
            maintenanceScheduleRepository.save(maintenanceSchedule);
            return "The maintenance schedule object was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the maintenance schedule object.", ex);
            return "Error: the maintenance schedule object wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String delete(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The maintenance schedule object id wasn't found";
        }

        try
        {
            MaintenanceSchedule maintenanceSchedule = getById(id);

            if (maintenanceSchedule == null)
            {
                return "The maintenance schedule object with " + id + " wasn't found";
            }

            maintenanceScheduleRepository.delete(maintenanceSchedule);
            return "The maintenance schedule object with id " + id + " was deleted";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the maintenance schedule object.", ex);
            return "Error: the maintenance schedule object wasn't removed.";
        }
    }
}
