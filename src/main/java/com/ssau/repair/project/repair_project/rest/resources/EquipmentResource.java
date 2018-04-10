package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rest/equipments")
public class EquipmentResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentResource.class);

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    public EquipmentResource(EquipmentRepository equipmentRepository, EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentRepository = equipmentRepository;
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }

    @GetMapping("/all")
    public List<Equipment> getAll()
    {
        try
        {
            return equipmentRepository.findAll();
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all equipments objects.", ex);
            return Collections.emptyList();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public List<Equipment> getByName(@RequestParam("name") String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return equipmentRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting equipments objects with name." + name, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Equipment getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return equipmentRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the equipment object with id." + id, ex);
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("name") String name,
                         @RequestParam("categoryId") Long categoryId)
    {
        if (name == null)
        {
            return "Name of the equipment object wasn't found.";
        }

        if (categoryId == null)
        {
            return "The equipment category object id wasn't found.";
        }

        try
        {
            EquipmentCategory equipmentCategory = equipmentCategoryRepository.getOne(categoryId);
            Equipment equipment = new Equipment();
            equipment.setName(name);
            equipment.setEquipmentCategory(equipmentCategory);
            equipmentRepository.save(equipment);
            return "The equipment object " + name + " was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the equipment object.", ex);
            return "Error: the equipment object wasn't created.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return "The equipment object id wasn't found";
        }

        try
        {
            Equipment equipment = getById(id);

            if (equipment == null)
            {
                return "The equipment object with id " + id + " wasn't found";
            }

            equipmentRepository.delete(equipment);
            return "The equipment object " + equipment.getName() + " was deleted";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the equipment object.", ex);
            return "Error: the equipment object wasn't removed.";
        }
    }
}
