package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.ao.Equipment;
import com.ssau.repair.project.repair_project.rest.repository.EquipmentRepository;
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

    @Autowired
    EquipmentRepository equipmentRepository;

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

    public Equipment getById(@PathVariable("id") Integer id)
    {
        return equipmentRepository.getOne(id);
    }

    @GetMapping("/create/{name}")
    public String create(@PathVariable("name") String name)
    {
        try
        {
            Equipment newEquipment = new Equipment(name);
            equipmentRepository.save(newEquipment);
            return "Equipment "+name+" was added in data base.";
        }
        catch (Exception ex)
        {
            LOG.error("Error during creating new Equipment.");
            return "Error: Equipment wasn't created.";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id)
    {
        Equipment equipment = getById(id);

        if (equipment == null)
        {
            return "Equipment didn't found";
        }
        else
        {
            equipmentRepository.delete(equipment);
            return "Equipment "+equipment.getName()+" was deleted";
        }
    }
}
