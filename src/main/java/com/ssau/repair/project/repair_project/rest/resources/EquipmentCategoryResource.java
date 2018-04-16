package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/equipment-category")
public class EquipmentCategoryResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentCategoryResource.class);

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    public EquipmentCategoryResource(EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("equipmentsCategories", equipmentCategoryRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all equipments categories objects.", ex);
            model.addAttribute("equipmentsCategories", Collections.emptyList());
        }
        return "admin_functions/data_base/equipment-category";
    }

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

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("name") String name, final RedirectAttributes redirectAttributes)
    {
        if (name == null || name.trim().isEmpty())
        {
            return "redirect:" + getRedirectCategoryPage();
            //return "The name of the equipment category object wasn't found.";
        }

        try
        {
            EquipmentCategory equipmentCategory = new EquipmentCategory();
            equipmentCategory.setName(name);
            equipmentCategoryRepository.save(equipmentCategory);
            redirectAttributes.addFlashAttribute("success", "The equipment category " + name + " was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the equipment category object.", ex);
            redirectAttributes.addFlashAttribute("error", "The equipment category object wasn't created.");
        }
        return "redirect:" + getRedirectCategoryPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@RequestParam(value = "categoryIds", required = false) String[] checkBoxesIds, final RedirectAttributes redirectAttributes)
    {
        if (checkBoxesIds == null || checkBoxesIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipments categories objects for remove wasn't found, please select " +
                    "interesting you categories for remove by a checkbox.");
            return "redirect:" + getRedirectCategoryPage();
        }

        try
        {
            for (String id : checkBoxesIds)
            {
                EquipmentCategory equipmentCategory = getById(Long.parseLong(id));

                if (equipmentCategory == null)
                {
                    continue;
                }
                equipmentCategoryRepository.delete(equipmentCategory);
            }
            redirectAttributes.addFlashAttribute("success", "The equipment category object(s) with ids: " + Arrays.toString(checkBoxesIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the equipment category object(s).", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the equipment category object(s).");
        }

        return "redirect:" + getRedirectCategoryPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name, final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the edited category wasn't found.");
            return "redirect:" + getRedirectCategoryPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the edited category with id"+ id +"wasn't found.");
            return "redirect:" + getRedirectCategoryPage();
        }

        EquipmentCategory equipmentCategory = getById(id);

        if (equipmentCategory == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The edited category with id"+ id +"wasn't found.");
            return "redirect:" + getRedirectCategoryPage();
        }

        equipmentCategory.setName(name);
        equipmentCategoryRepository.save(equipmentCategory);

        redirectAttributes.addFlashAttribute("success", "The equipment category with id"+ id +" was changed.");
        return "redirect:" + getRedirectCategoryPage();
    }

    private String getRedirectCategoryPage()
    {
        return "/equipment-category";
    }
}
