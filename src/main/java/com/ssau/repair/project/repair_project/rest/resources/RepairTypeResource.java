package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/repairType")
public class RepairTypeResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentResource.class);

    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairTypeResource(RepairTypeRepository repairTypeRepository)
    {
        this.repairTypeRepository = repairTypeRepository;
    }

    @GetMapping("/all")
    public List<RepairType> getAll()
    {
        try
        {
            return repairTypeRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair types objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getByName", method = RequestMethod.GET)
    public List<RepairType> getByName(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return repairTypeRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair type object with name." + name, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairType getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairTypeRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair type object with id." + id, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return "Name of the repair type object wasn't found.";
        }

        try
        {
            RepairType repairType = new RepairType();
            repairType.setName(name);
            repairTypeRepository.save(repairType);
            return "The repair type object " + name + " was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the repair type.", ex);
            return "Error: the repair type wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String delete(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The repair type object id wasn't found";
        }

        try
        {
            RepairType repairType = getById(id);

            if (repairType == null)
            {
                return "The repair type object with id " + id + " wasn't found";
            }

            repairTypeRepository.delete(repairType);
            return "The repair type " + repairType.getName() + " was deleted";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the repair type object.", ex);
            return "Error: the repair type object wasn't removed.";
        }

    }
}
