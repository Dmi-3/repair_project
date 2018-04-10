package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.RepairStandards;
import com.ssau.repair.project.repair_project.repositories.RepairStandardsRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/repairStandards")
public class RepairStandardsResource {
    private static final Logger LOG = Logger.getLogger(RepairStandardsResource.class);

    private final RepairStandardsRepository repairStandardsRepository;

    @Autowired
    public RepairStandardsResource(RepairStandardsRepository repairStandardsRepository) {
        this.repairStandardsRepository = repairStandardsRepository;
    }

    @GetMapping("/all")
    public List<RepairStandards> getAll()
    {
        return repairStandardsRepository.findAll();
    }

    @GetMapping("/{name}")
    public List<RepairStandards> getByName(@PathVariable("name") String name)
    {
        return repairStandardsRepository.findByName(name);
    }

    public RepairStandards getById(@PathVariable("id") Long id)
    {
        return repairStandardsRepository.getOne(id);
    }

    @GetMapping("/create/{name}")
    public String create(@PathVariable("name") String name)
    {
        try
        {
            RepairStandards newRepairStandards = new RepairStandards();
            newRepairStandards.setName(name);
            repairStandardsRepository.save(newRepairStandards);
            return "The repair standard "+name+" was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the repair standard.");
            return "Error: The repair standard wasn't created.";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id)
    {
        RepairStandards repairStandards = getById(id);

        if (repairStandards == null)
        {
            return "The repair standard wasn't found";
        }
        else
        {
            repairStandardsRepository.delete(repairStandards);
            return "The repair standard "+repairStandards.getName()+" was deleted";
        }
    }

}
