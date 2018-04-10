package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/equipmentsCategory")
public class EquipmentCategoryResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentCategoryResource.class);

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    public EquipmentCategoryResource(EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }

    @GetMapping("/all")
    public List<EquipmentCategory> getAll()
    {
        try
        {
            return equipmentCategoryRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all equipments categories objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public List<EquipmentCategory> getByName(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return equipmentCategoryRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting equipments categories objects with name." + name, ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public EquipmentCategory getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return equipmentCategoryRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the equipments category object with id." + id, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return "The name of the equipment category object wasn't found.";
        }

        try
        {
            EquipmentCategory equipmentCategory = new EquipmentCategory();
            equipmentCategory.setName(name);
            equipmentCategoryRepository.save(equipmentCategory);
            return "The equipment category " + name + " was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the equipment category object.", ex);
            return "Error: the equipment category object wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The equipment category id wasn't found";
        }

        try
        {
            EquipmentCategory equipmentCategory = getById(id);

            if (equipmentCategory == null)
            {
                return "The equipment category object with id " + id + " wasn't found";
            }

            equipmentCategoryRepository.delete(equipmentCategory);
            return "The equipment category object " + equipmentCategory.getName() + " was deleted";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the equipment category object.", ex);
            return "Error: the equipment category object wasn't removed.";
        }

    }
}
