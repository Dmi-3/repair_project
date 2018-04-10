package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/repairType")
public class RepairTypeResource {
    private static final Logger LOG = Logger.getLogger(EquipmentResource.class);

    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairTypeResource(RepairTypeRepository repairTypeRepository) {
        this.repairTypeRepository = repairTypeRepository;
    }

    @GetMapping("/all")
    public List<RepairType> getAll() {
        return repairTypeRepository.findAll();
    }

    @GetMapping("/{name}")
    public List<RepairType> getByName(@PathVariable("name") String name) {
        return repairTypeRepository.findByName(name);
    }

    public RepairType getById(@PathVariable("id") Long id) {
        return repairTypeRepository.getOne(id);
    }

    @GetMapping("/create/{name}")
    public String create(@PathVariable("name") String name) {
        try {
            RepairType newRepairType = new RepairType();
            newRepairType.setName(name);
            repairTypeRepository.save(newRepairType);
            return "The RepairType " + name + " was added in data base.";
        } catch (Exception ex) {
            LOG.error("An error occurred during creating new the repair type.");
            return "Error: the repair type wasn't created.";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        RepairType RepairType = getById(id);

        if (RepairType == null) {
            return "The repair type wasn't found";
        } else {
            repairTypeRepository.delete(RepairType);
            return "The repair type " + RepairType.getName() + " was deleted";
        }
    }
}
