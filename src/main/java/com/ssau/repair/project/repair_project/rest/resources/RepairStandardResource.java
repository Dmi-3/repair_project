package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.RepairStandard;
import com.ssau.repair.project.repair_project.repositories.RepairStandardRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/repairStandards")
public class RepairStandardResource
{
    private static final Logger LOG = Logger.getLogger(RepairStandardResource.class);

    private final RepairStandardRepository repairStandardRepository;

    @Autowired
    public RepairStandardResource(RepairStandardRepository repairStandardRepository)
    {
        this.repairStandardRepository = repairStandardRepository;
    }

    @GetMapping("/all")
    public List<RepairStandard> getAll()
    {
        try
        {
            return repairStandardRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair standard objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getByName", method = RequestMethod.GET)
    public List<RepairStandard> getByName(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return repairStandardRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair standard object with name." + name, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairStandard getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairStandardRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair standard object with id." + id, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return "Id of the repair standard object wasn't found.";
        }

        try
        {
            RepairStandard repairStandard = new RepairStandard();
            repairStandard.setName(name);
            repairStandardRepository.save(repairStandard);
            return "The repair standard object " + name + " was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the repair standard object.", ex);
            return "Error: The repair standard object wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String delete(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The repair standard object id wasn't found";
        }

        try
        {
            RepairStandard repairStandard = getById(id);

            if (repairStandard == null)
            {
                return "The repair standard object with id " + id + " wasn't found";
            }

            repairStandardRepository.delete(repairStandard);
            return "The repair standard object " + repairStandard.getName() + " was deleted";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the repair standard object.", ex);
            return "Error: the repair standard object wasn't removed.";
        }

    }

}
