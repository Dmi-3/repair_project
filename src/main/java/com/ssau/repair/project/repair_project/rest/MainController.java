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

    @RequestMapping("/")
    public String welcome(Map<String, Object> model)
    {
        return "index";
    }
}
