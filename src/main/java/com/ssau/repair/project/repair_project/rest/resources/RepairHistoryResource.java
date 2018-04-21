package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Equipment;
import com.ssau.repair.project.repair_project.entities.RepairHistory;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.EquipmentRepository;
import com.ssau.repair.project.repair_project.repositories.RepairHistoryRepository;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/repair-history")
public class RepairHistoryResource
{
    private static final Logger LOG = Logger.getLogger(RepairHistoryResource.class);

    private final RepairHistoryRepository repairHistoryRepository;
    private final EquipmentRepository equipmentRepository;
    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairHistoryResource(RepairHistoryRepository repairHistoryRepository, EquipmentRepository equipmentRepository, RepairTypeRepository repairTypeRepository)
    {

        this.repairHistoryRepository = repairHistoryRepository;
        this.equipmentRepository = equipmentRepository;
        this.repairTypeRepository = repairTypeRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("repairHistory", repairHistoryRepository.findAll());
            model.addAttribute("equipments", equipmentRepository.findAll());
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all repair history objects.", ex);
            model.addAttribute("repairHistory", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all repair history objects.");
        }
        return "admin_functions/data_base/repair-history";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public RepairHistory getById(@RequestParam("id") Long id)
    {
        if (id == null)
        {
            return null;
        }

        try
        {
            return repairHistoryRepository.getOne(id);
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting the repair history object with id." + id, ex);
            return null;
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "equipmentId", required = false) Long equipmentId,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         @RequestParam(value = "equipmentDowntime", required = false) Integer equipmentDowntime,
                         @RequestParam(value = "date", required = false) String date,
                         final RedirectAttributes redirectAttributes)
    {
        if (equipmentId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment id for repair history wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id for repair history wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (equipmentDowntime == null || equipmentDowntime <= 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The incorrect value of equipment downtime for the repair history.");
            return getRedirectRepairHistoryPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date for the repair history wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        try
        {
            Equipment equipment = equipmentRepository.getById(equipmentId);
            if (equipment == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The equipment with id" + equipmentId + "wasn't found.");
                return getRedirectRepairHistoryPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);
            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
                return getRedirectRepairHistoryPage();
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            if (localDate == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The date for the repair history wasn't found.");
                return getRedirectRepairHistoryPage();
            }

            RepairHistory repairHistory = new RepairHistory(equipment, repairType, equipmentDowntime, localDate);

            repairHistoryRepository.save(repairHistory);
            redirectAttributes.addFlashAttribute("success", "The repair history object was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating the repair history object.", ex);
            redirectAttributes.addFlashAttribute("error", "The the repair history object wasn't created.");
        }
        return getRedirectRepairHistoryPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam("repairHistoryIds") String[] repairHistoryIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (repairHistoryIds == null || repairHistoryIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair history objects for remove wasn't found, please select " +
                    "interesting you repair history objects for remove by a checkbox.");
            return getRedirectRepairHistoryPage();
        }

        try
        {
            for (String id : repairHistoryIds)
            {
                RepairHistory repairHistory = repairHistoryRepository.getById(Long.parseLong(id));

                if (repairHistory == null)
                {
                    continue;
                }

                repairHistoryRepository.delete(repairHistory);
            }
            redirectAttributes.addFlashAttribute("success", "The repair history object(s) with ids: " +
                    Arrays.toString(repairHistoryIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            {
                LOG.error("An error occurred during removing the repair history object.", ex);
                redirectAttributes.addFlashAttribute("error", "An error occurred during removing the repair history object(s).");
            }
        }
        return getRedirectRepairHistoryPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "equipmentId", required = false) Long equipmentId,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         @RequestParam(value = "equipmentDowntime", required = false) Integer equipmentDowntime,
                         @RequestParam(value = "date", required = false) String date,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the repair history object wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (equipmentId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the equipment wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the repair type wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (equipmentDowntime == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The equipment downtime wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        if (date == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The date wasn't found.");
            return getRedirectRepairHistoryPage();
        }

        try
        {
            Equipment equipment = equipmentRepository.getById(equipmentId);

            if (equipment == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The equipment with id" + equipmentId + "wasn't found.");
                return getRedirectRepairHistoryPage();
            }

            RepairType repairType = repairTypeRepository.getById(repairTypeId);
            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type with id" + repairTypeId + "wasn't found.");
                return getRedirectRepairHistoryPage();
            }

            RepairHistory repairHistory = repairHistoryRepository.getById(id);

            repairHistory.setEquipment(equipment);
            repairHistory.setRepairType(repairType);
            repairHistory.setEquipmentDowntime(equipmentDowntime);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate dateTime = LocalDate.parse(date, formatter);
            if (dateTime == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The date for the repair history wasn't found.");
                return getRedirectRepairHistoryPage();
            }
            repairHistory.setDate(dateTime);

            repairHistoryRepository.save(repairHistory);

            redirectAttributes.addFlashAttribute("success", "The repair history with id" + id + " was changed.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during updating the repair history object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during updating the repair history object.");
        }
        return getRedirectRepairHistoryPage();
    }

    private String getRedirectRepairHistoryPage()
    {
        return "redirect:/repair-history";
    }
}
