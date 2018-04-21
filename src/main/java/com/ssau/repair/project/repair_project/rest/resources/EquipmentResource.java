package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.EquipmentCategory;
import com.ssau.repair.project.repair_project.repositories.EquipmentCategoryRepository;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
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
@RequestMapping("/equipments")
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("equipments", equipmentRepository.findAll());
            model.addAttribute("equipmentsCategories", equipmentCategoryRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all equipments objects.", ex);
            model.addAttribute("equipments", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all equipments objects.");
        }
        return "admin_functions/data_base/equipments";
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public List<Equipment> getByName(@RequestParam(value = "name", required = false) String name)
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
            return Collections.emptyList();
        }
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Equipment getById(@RequestParam(value = "id", required = false) Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return equipmentRepository.getById(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the equipment object with id." + id, ex);
            return null;
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         final RedirectAttributes redirectAttributes)
    {
        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The name of the equipment object wasn't found.");
            return getRedirectEquipmentsPage();
        }

        if (categoryId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment category object id wasn't found.");
            return getRedirectEquipmentsPage();
        }

        try
        {
            EquipmentCategory equipmentCategory = equipmentCategoryRepository.getById(categoryId);

            if (equipmentCategory == null)
            {
                redirectAttributes.addFlashAttribute("warning", "Id of the edited category wasn't found.");
                return getRedirectEquipmentsPage();
            }

            Equipment equipment = new Equipment(name, equipmentCategory);
            equipmentRepository.save(equipment);
            redirectAttributes.addFlashAttribute("success", "The equipment object " + name + " was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the equipment object.", ex);
            redirectAttributes.addFlashAttribute("error", "The equipment object wasn't created.");
        }
        return getRedirectEquipmentsPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@RequestParam(value = "equipmentsIds", required = false) String[] equipmentsIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (equipmentsIds == null || equipmentsIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipments objects for remove wasn't found, please select " +
                    "interesting you equipments for remove by a checkbox.");
            return getRedirectEquipmentsPage();
        }

        try
        {
            for (String id : equipmentsIds)
            {
                Equipment equipment = equipmentRepository.getById(Long.parseLong(id));

                if (equipment == null)
                {
                    continue;
                }

                equipmentRepository.delete(equipment);
            }
            redirectAttributes.addFlashAttribute("success", "The equipment object(s) with ids: " +
                    Arrays.toString(equipmentsIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the equipment object(s).", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the equipment object(s).");
        }
        return getRedirectEquipmentsPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the edited equipment wasn't found.");
            return getRedirectEquipmentsPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the edited equipment with id" + id + "wasn't found.");
            return getRedirectEquipmentsPage();
        }

        if (categoryId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the edited category wasn't found.");
            return getRedirectEquipmentsPage();
        }

        try
        {
            Equipment equipment = equipmentRepository.getById(id);

            if (equipment == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The edited equipment with id" + id + "wasn't found.");
                return getRedirectEquipmentsPage();
            }

            EquipmentCategory equipmentCategory = equipmentCategoryRepository.getById(categoryId);

            if (equipmentCategory == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The edited category with id" + id + "wasn't found.");
                return getRedirectEquipmentsPage();
            }

            equipment.setName(name);
            equipment.setEquipmentCategory(equipmentCategory);
            equipmentRepository.save(equipment);

            redirectAttributes.addFlashAttribute("success", "The equipment category with id" + id + " was changed.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during updating the equipment object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during updating the equipment object.");
        }
        return getRedirectEquipmentsPage();
    }

    private String getRedirectEquipmentsPage()
    {
        return "redirect:/equipments";
    }
}
