package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.entities.RepairStandard;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import com.ssau.repair.project.repair_project.repositories.RepairStandardRepository;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/repair-standards")
public class RepairStandardResource
{
    private static final Logger LOG = Logger.getLogger(RepairStandardResource.class);

    private final RepairStandardRepository repairStandardRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final EquipmentRepository equipmentRepository;
    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairStandardResource(RepairStandardRepository repairStandardRepository, EquipmentCategoryRepository equipmentCategoryRepository, EquipmentRepository equipmentRepository, RepairTypeRepository repairTypeRepository)
    {
        this.repairStandardRepository = repairStandardRepository;
        this.equipmentCategoryRepository = equipmentCategoryRepository;
        this.equipmentRepository = equipmentRepository;
        this.repairTypeRepository = repairTypeRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("repairStandards", repairStandardRepository.findAll());
            model.addAttribute("equipmentsCategories", equipmentCategoryRepository.findAll());
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair standard objects.", ex);
            model.addAttribute("repairStandards", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all repair standard objects.");
        }
        return "admin_functions/data_base/repair-standards";
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public List<RepairStandard> getByName(@RequestParam(value = "name", required = false) String name)
    {
        if (name == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return repairStandardRepository.findByName(name);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair standard object with name." + name, ex);
            return null;
        }
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairStandard getById(@RequestParam(value = "id", required = false) Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairStandardRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair standard object with id." + id, ex);
            return null;
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         @RequestParam(value = "laborIntensity", required = false) Integer laborIntensity,
                         final RedirectAttributes redirectAttributes)
    {

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The name of the repair standard object wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (categoryId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The category id of the repair standard object wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the repair standard object wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the repair standard wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        try
        {
            EquipmentCategory equipmentCategory = equipmentCategoryRepository.getById(categoryId);
            if (equipmentCategory == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The edited category with id" + categoryId + "wasn't found.");
                return getRedirectRepairStandardsPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);

            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
                return getRedirectRepairStandardsPage();
            }

            RepairStandard repairStandard = new RepairStandard(name, equipmentCategory, repairType, laborIntensity);

            repairStandardRepository.save(repairStandard);
            redirectAttributes.addFlashAttribute("success", "The repair standard was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the repair standard object.", ex);
            redirectAttributes.addFlashAttribute("error", "The repair standard wasn't created.");
        }
        return getRedirectRepairStandardsPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "repairStandardsIds", required = false) String[] repairStandardIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (repairStandardIds == null || repairStandardIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair standards objects for remove wasn't found, please select " +
                    "interesting you repair standards for remove by a checkbox.");
            return getRedirectRepairStandardsPage();
        }

        try
        {
            for (String id : repairStandardIds)
            {
                RepairStandard repairStandard = repairStandardRepository.getById(Long.parseLong(id));

                if (repairStandard == null)
                {
                    continue;
                }

                repairStandardRepository.delete(repairStandard);
            }
            redirectAttributes.addFlashAttribute("success", "The repair standards object(s) with ids: " +
                    Arrays.toString(repairStandardIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the repair standards object(s).", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the repair standards object(s).");
        }
        return getRedirectRepairStandardsPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         @RequestParam(value = "laborIntensity", required = false) Integer laborIntensity,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the repair standard wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the repair standard with id " + id + "wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (categoryId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the edited category wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the repair standard object wasn't found.");
            return getRedirectRepairStandardsPage();
        }

        if (laborIntensity == null || laborIntensity <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of labor intensity for the repair standard.");
            return getRedirectRepairStandardsPage();
        }

        try
        {
            RepairStandard repairStandard = repairStandardRepository.getById(id);

            if (repairStandard == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair standard with id" + id + "wasn't found.");
                return getRedirectRepairStandardsPage();
            }

            EquipmentCategory equipmentCategory = equipmentCategoryRepository.getById(categoryId);
            if (equipmentCategory == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The edited category with id" + categoryId + "wasn't found.");
                return getRedirectRepairStandardsPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);

            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
                return getRedirectRepairStandardsPage();
            }

            repairStandard.setName(name);
            repairStandard.setEquipmentCategory(equipmentCategory);
            repairStandard.setRepairType(repairType);
            repairStandard.setLaborIntensity(laborIntensity);
            repairStandardRepository.save(repairStandard);

            redirectAttributes.addFlashAttribute("success", "The repair standard with id" + id + " was changed.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during updating the repair standard object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during updating the repair standard object.");
        }
        return getRedirectRepairStandardsPage();
    }

    @ResponseBody
    @RequestMapping(value = "/getLaborIntensity", method = RequestMethod.POST, produces="application/json")
    public Integer getLaborIntensity(@RequestParam(value = "equipmentId", required = false) Long equipmentId,
                                     @RequestParam(value = "repairTypeId", required = false) Long repairTypeId)
    {
        if (repairTypeId == null || equipmentId == null)
        {
            return null;
        }

        Equipment equipment = equipmentRepository.getById(equipmentId);
        RepairType repairType = repairTypeRepository.getById(repairTypeId);

        if (equipment == null || repairType == null)
        {
            return null;
        }

        RepairStandard repairStandard = repairStandardRepository.getRepairStandardByEquipmentCategoryAndRepairType(equipment.getEquipmentCategory(), repairType);

        if (repairStandard == null)
        {
            return null;
        }

        return repairStandard.getLaborIntensity();
    }

    private String getRedirectRepairStandardsPage()
    {
        return "redirect:/repair-standards";
    }
}
