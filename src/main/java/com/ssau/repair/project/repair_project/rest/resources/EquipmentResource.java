package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/equipments")
public class EquipmentResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentResource.class);

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentResource(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/all")
    public List<Equipment> getAll()
    {
        return equipmentRepository.findAll();
    }

    @GetMapping("/{name}")
    public List<Equipment> getByName(@PathVariable("name") String name)
    {
        return equipmentRepository.findByName(name);
    }

    public Equipment getById(@PathVariable("id") Long id)
    {
        return equipmentRepository.getOne(id);
    }

    @GetMapping("/create/{name}")
    public String create(@PathVariable("name") String name)
    {
        try
        {
            Equipment newEquipment = new Equipment();
            newEquipment.setName(name);
            equipmentRepository.save(newEquipment);
            return "The Equipment "+name+" was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new Equipment.");
            return "Error: Equipment wasn't created.";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id)
    {
        Equipment equipment = getById(id);

        if (equipment == null)
        {
            return "The Equipment wasn't found";
        }
        else
        {
            equipmentRepository.delete(equipment);
            return "The Equipment "+equipment.getName()+" was deleted";
        }
    }
}
