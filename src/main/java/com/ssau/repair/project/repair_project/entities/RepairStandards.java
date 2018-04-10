package com.ssau.repair.project.repair_project.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Repair_periodically_standards")
public class RepairStandards implements Serializable {

    public RepairStandards() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Equipment_category_id")
    private Long equipmentCategoryId;

    public Long getEquipmentCategoryId() {
        return equipmentCategoryId;
    }

    public void setEquipmentCategoryId(Long equipmentCategoryId) {
        this.equipmentCategoryId = equipmentCategoryId;
    }

    @Column(name = "Name")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Repair_type")
    private Long repairType;

    public Long getRepairType() {
        return repairType;
    }

    public void setRepairType(Long repairType) {
        this.repairType = repairType;
    }

    @Column(name = "Repair_periodicity")
    private Integer repairPeriodicity;

    public Integer getRepairPeriodicity() {
        return repairPeriodicity;
    }

    public void setRepairPeriodicity(Integer repairPeriodicity) {
        this.repairPeriodicity = repairPeriodicity;
    }

    @Column(name = "Equipment_downtime")
    private Integer equipmentDowntime;

    public Integer getEquipmentDowntime() {
        return equipmentDowntime;
    }

    public void setEquipmentDowntime(Integer equipmentDowntime) {
        this.equipmentDowntime = equipmentDowntime;
    }

    @Column(name = "Labor_intensity")
    private Integer laborIntensity;

    public Integer getLaborIntensity() {
        return laborIntensity;
    }

    public void setLaborIntensity(Integer laborIntensity) {
        this.laborIntensity = laborIntensity;
    }
}
