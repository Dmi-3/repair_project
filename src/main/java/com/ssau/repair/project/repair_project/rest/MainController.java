package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class MainController
{
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public MainController(EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }

   /* @RequestMapping("/equipment-category")
    public String categoryEquipments(Map<String, Object> model)
    {
        return "admin_functions/data_base/equipment-category";
    }*/

    @RequestMapping("/equipments")
    public String equipments(Map<String, Object> model)
    {
        return "admin_functions/data_base/equipments";
    }

    @RequestMapping("/maintenance-schedule")
    public String maintenanceSchedule(Map<String, Object> model)
    {
        return "admin_functions/data_base/maintenance-schedule";
    }

    @RequestMapping("/repair-history")
    public String repairHistory(Map<String, Object> model)
    {
        return "admin_functions/data_base/repair-history";
    }

    @RequestMapping("/repair-standards")
    public String repairStandards(Map<String, Object> model)
    {
        return "admin_functions/data_base/repair-standards";
    }

    @RequestMapping("/repair-types")
    public String repairTypes(Map<String, Object> model)
    {
        return "admin_functions/data_base/repair-types";
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model)
    {
        return "index";
    }
}
