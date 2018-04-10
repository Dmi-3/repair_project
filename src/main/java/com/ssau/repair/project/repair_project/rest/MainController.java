package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class MainController
{
    private static final String message = "hello world";

    @RequestMapping("/")
    public String welcome(Map<String, Object> model)
    {
        model.put("message", message);
        return "welcome";
    }
}
