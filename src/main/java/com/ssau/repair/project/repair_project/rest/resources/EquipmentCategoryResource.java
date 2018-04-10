package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/equipmentsCategory")
public class EquipmentCategoryResource {
    private static final Logger LOG = Logger.getLogger(EquipmentCategoryResource.class);

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    public EquipmentCategoryResource(EquipmentCategoryRepository equipmentCategoryRepository) {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }

    @GetMapping("/all")
    public List<EquipmentCategory> getAll()
    {
        return equipmentCategoryRepository.findAll();
    }

    @GetMapping("/{name}")
    public List<EquipmentCategory> getByName(@PathVariable("name") String name)
    {
        return equipmentCategoryRepository.findByName(name);
    }

    public EquipmentCategory getById(@PathVariable("id") Long id)
    {
        return equipmentCategoryRepository.getOne(id);
    }

    @GetMapping("/create/{name}")
    public String create(@PathVariable("name") String name)
    {
        try
        {
            EquipmentCategory newEquipmentCategory = new EquipmentCategory();
            newEquipmentCategory.setName(name);
            equipmentCategoryRepository.save(newEquipmentCategory);
            return "The Equipment category "+name+" was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new Equipment.");
            return "Error: The Equipment wasn't created.";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id)
    {
        EquipmentCategory equipmentCategory = getById(id);

        if ( equipmentCategory == null)
        {
            return "The Equipment category wasn't found";
        }
        else
        {
            equipmentCategoryRepository.delete( equipmentCategory);
            return "The Equipment category "+ equipmentCategory.getName()+" was deleted";
        }
    }
}
