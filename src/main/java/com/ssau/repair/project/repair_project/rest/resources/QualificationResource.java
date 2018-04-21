package com.ssau.repair.project.repair_project.rest.resources;

import com.ssau.repair.project.repair_project.entities.Qualification;
import com.ssau.repair.project.repair_project.entities.RepairType;
import com.ssau.repair.project.repair_project.repositories.QualificationRepository;
import com.ssau.repair.project.repair_project.repositories.RepairTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/qualifications")
public class QualificationResource
{
    private static final Logger LOG = Logger.getLogger(QualificationResource.class);

    private final QualificationRepository qualificationRepository;
    private final RepairTypeRepository repairTypeRepository;

    public QualificationResource(QualificationRepository qualificationRepository, RepairTypeRepository repairTypeRepository)
    {
        this.qualificationRepository = qualificationRepository;
        this.repairTypeRepository = repairTypeRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model)
    {
        try
        {
            model.addAttribute("qualifications", qualificationRepository.findAll());
            model.addAttribute("repairTypes", repairTypeRepository.findAll());
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during getting all qualifications objects.", ex);
            model.addAttribute("qualifications", Collections.emptyList());
            model.addAttribute("error", "An error occurred during getting all qualifications objects.");
        }
        return "admin_functions/data_base/qualifications";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         final RedirectAttributes redirectAttributes)
    {
        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "The name of the qualification object wasn't found.");
            return getRedirectQualificationsPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the qualification object wasn't found.");
            return getRedirectQualificationsPage();
        }

        try
        {

            RepairType repairType = repairTypeRepository.getById(repairTypeId);
            if (repairType == null)
            {
                redirectAttributes.addFlashAttribute("warning", "The repair type of the qualification object wasn't found.");
                return getRedirectQualificationsPage();
            }

            Qualification qualification = new Qualification(name, repairType);
            qualificationRepository.save(qualification);
            redirectAttributes.addFlashAttribute("success", "The qualification " + name + " was added in data base.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during creating new the qualification.", ex);
            redirectAttributes.addFlashAttribute("error", "The the qualification object wasn't created.");
        }
        return getRedirectQualificationsPage();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "qualificationsIds", required = false) String[] qualificationsIds,
                         final RedirectAttributes redirectAttributes)
    {
        if (qualificationsIds == null || qualificationsIds.length == 0)
        {
            redirectAttributes.addFlashAttribute("warning", "The qualification objects for remove wasn't found, please select " +
                    "interesting you qualifications for remove by a checkbox.");
            return getRedirectQualificationsPage();
        }

        try
        {
            for (String id : qualificationsIds)
            {
                Qualification qualification = qualificationRepository.getById(Long.parseLong(id));

                if (qualification == null)
                {
                    return "The qualification object with id " + id + " wasn't found";
                }

                qualificationRepository.delete(qualification);
            }
            redirectAttributes.addFlashAttribute("success", "The qualifications object(s) with ids: " + Arrays.toString(qualificationsIds) + " were deleted.");
        }
        catch (Exception ex)
        {
            LOG.error("An error occurred during removing the qualification object.", ex);
            redirectAttributes.addFlashAttribute("error", "An error occurred during removing the qualification object(s).");
        }
        return getRedirectQualificationsPage();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "repairTypeId", required = false) Long repairTypeId,
                         final RedirectAttributes redirectAttributes)
    {
        if (id == null)
        {
            redirectAttributes.addFlashAttribute("warning", "Id of the qualification wasn't found.");
            return getRedirectQualificationsPage();
        }

        if (name == null || name.trim().isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning", "Name of the qualification with id " + id + "wasn't found.");
            return getRedirectQualificationsPage();
        }

        if (repairTypeId == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type id of the qualification object wasn't found.");
            return getRedirectQualificationsPage();
        }

        Qualification qualification = qualificationRepository.getById(id);

        if (qualification == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The qualification with id " + id + "wasn't found.");
            return getRedirectQualificationsPage();
        }

        RepairType repairType = repairTypeRepository.getById(repairTypeId);

        if (repairType == null)
        {
            redirectAttributes.addFlashAttribute("warning", "The repair type with id " + repairTypeId + "wasn't found.");
            return getRedirectQualificationsPage();
        }

        qualification.setName(name);
        qualification.setRepairType(repairType);
        qualificationRepository.save(qualification);

        redirectAttributes.addFlashAttribute("success", "The qualification with id " + id + " was changed.");
        return getRedirectQualificationsPage();
    }

    private String getRedirectQualificationsPage()
    {
        return "redirect:/qualifications";
    }
}
