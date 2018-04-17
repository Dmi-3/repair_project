package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
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
@RequestMapping("/repair-types")
public class RepairTypeResource
{
    private static final Logger LOG = Logger.getLogger(EquipmentResource.class);

    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairTypeResource(RepairTypeRepository repairTypeRepository)
    {
        this.repairTypeRepository = repairTypeRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair types objects.", ex);
            model.addAttribute("repairTypes", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all repair types objects.");
        }
        return "admin_functions/data_base/repair-types";
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public List<RepairType> getByName(@RequestParam(value = "name", required = false) String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return repairTypeRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair type object with name." + name, ex);
            return Collections.emptyList();
        }
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairType getById(@RequestParam(value = "id", required = false) Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairTypeRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair type object with id." + id, ex);
            return null;
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam("name") String name,
                         final RedirectAttributes redirectAttributes)
    {
        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("error", "The name of the repair type object wasn't found.");
            return "redirect:" + getRedirectRepairTypesPage();
        }

        try
        {
            RepairType repairType = new RepairType();
            repairType.setName(name);
            repairTypeRepository.save(repairType);
            redirectAttributes.addFlashAttribute("success", "The repair type " + name + " was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the repair type.", ex);
            redirectAttributes.addFlashAttribute("error", "The the repair type object wasn't created.");
        }
        return "redirect:" + getRedirectRepairTypesPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam("repairTypesIds") String[] repairTypesIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (repairTypesIds == null || repairTypesIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type objects for remove wasn't found, please select " +
                    "interesting you repair types for remove by a checkbox.");
            return "redirect:" + getRedirectRepairTypesPage();
        }

        try
        {
            for (String id : repairTypesIds)
            {
                RepairType repairType = repairTypeRepository.getById(Long.parseLong(id));

                if (repairType == null)
                {
                    return "The repair type object with id " + id + " wasn't found";
                }

                repairTypeRepository.delete(repairType);
            }
            redirectAttributes.addFlashAttribute("success", "The repair type object(s) with ids: " + Arrays.toString(repairTypesIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the repair type object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the repair type object(s).");
        }
        return "redirect:" + getRedirectRepairTypesPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the repair type wasn't found.");
            return "redirect:" + getRedirectRepairTypesPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the repair type with id" + id + "wasn't found.");
            return "redirect:" + getRedirectRepairTypesPage();
        }

        RepairType repairType = repairTypeRepository.getById(id);

        if (repairType == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type with id" + id + "wasn't found.");
            return "redirect:" + getRedirectRepairTypesPage();
        }

        repairType.setName(name);
        repairTypeRepository.save(repairType);

        redirectAttributes.addFlashAttribute("success", "The repair type with id" + id + " was changed.");
        return "redirect:" + getRedirectRepairTypesPage();
    }

    private String getRedirectRepairTypesPage()
    {
        return "/repair-types";
    }
}
