package com.ssau.repair.project.repair_project.rest;

import com.ssau.repair.project.repair_project.entities.*;
import com.ssau.repair.project.repair_project.hungarian.Hungarian;
import com.ssau.repair.project.repair_project.repositories.*;
import com.ssau.repair.project.repair_project.util.MyLinkedMap;
import com.ssau.repair.project.repair_project.util.Pair;
import com.ssau.repair.project.repair_project.util.Utils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@EnableScheduling
public class MainController
{
    private static final Integer WORK_DAY = 8;
    private final EquipmentRepository equipmentRepository;
    private final WorkerRepository workerRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final RepairStandardRepository repairStandardRepository;
    private final Utils utils;
    private final WorkerScheduleRepository workerScheduleRepository;
    private final QualificationRepository qualificationRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public MainController(EquipmentRepository equipmentRepository, WorkerRepository workerRepository, MaintenanceScheduleRepository maintenanceScheduleRepository,
                          RepairStandardRepository repairStandardRepository, RepairHistoryRepository repairHistoryRepository,
                          WorkerScheduleRepository workerScheduleRepository, WorkerScheduleRepository workerScheduleRepository1, QualificationRepository qualificationRepository, EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentRepository = equipmentRepository;
        this.workerRepository = workerRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.repairStandardRepository = repairStandardRepository;
        this.workerScheduleRepository = workerScheduleRepository1;
        this.qualificationRepository = qualificationRepository;
        this.equipmentCategoryRepository = equipmentCategoryRepository;
        this.utils = new Utils(repairHistoryRepository, workerScheduleRepository, maintenanceScheduleRepository);
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model)
    {
        return "index";
    }

    //@Scheduled(cron = "55 56 11 * * *")

    @RequestMapping("/test")
    public void autoCheckAndStartMaintenance()
    {
        /*final Random random = new Random();
        for (int i = 0; i < 1000; i++)
        {
            Worker worker = new Worker();
            worker.setName("Worker"+i);
            Set<Qualification> qualifications = new LinkedHashSet<>();
            qualifications.add(qualificationRepository.getById(new Long(random.nextInt(4)+1)));
            worker.setQualifications(qualifications);
            worker.setTariffRate(40*(random.nextInt(5)+1));
            workerRepository.save(worker);
        }

        for (int i = 0; i < 1000; i++)
        {
            Equipment equipment = new Equipment();
            equipment.setName("Equipment"+i);
            equipment.setEquipmentCategory(equipmentCategoryRepository.getById(new Long(random.nextInt(5)+1)));
            equipmentRepository.save(equipment);
        }*/
        List<Equipment> equipmentList = equipmentRepository.findAll();
        if (equipmentList.isEmpty())
        {
            //LOG
            return;
        }

        MyLinkedMap<Equipment, RepairType> needRepairMap = getNeedRepairEquipment(equipmentList);
        MyLinkedMap<Worker, LocalDate> workers = getFreeWorkerInNearDays(needRepairMap.size());
        //TODO:Нужно как то подобрать список квалифицированных + неквалифицированных работников
        int[][] matrix = getWorkerToRepairMatrix(needRepairMap, workers);
        Hungarian hungarian = new Hungarian(matrix);
        int[] result = hungarian.getResult();
        for(int i = 0; i < result.length; i++)
        {
            System.out.println(String.format("Row%d => Col%d (%d)", i + 1, result[i] + 1, matrix[i][result[i]])); // Rowi => Colj (value)
        }

        planMaintenance(needRepairMap, workers, result);
    }

    private void planMaintenance(MyLinkedMap<Equipment, RepairType> needRepairMap, MyLinkedMap<Worker, LocalDate> workers, int[] result)
    {

        for (int i = 0; i < result.length; i++)
        {
            Map.Entry<Equipment, RepairType> equipmentRepair = needRepairMap.getEntry(result[i]);
            Map.Entry<Worker, LocalDate> workerForRepair = workers.getEntry(i);

            RepairStandard repairStandard = repairStandardRepository
                    .getRepairStandardByEquipmentCategoryAndRepairType(equipmentRepair.getKey().getEquipmentCategory(), equipmentRepair.getValue());

            MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(equipmentRepair.getKey(), equipmentRepair.getValue(),
                    workerForRepair.getKey(), repairStandard.getLaborIntensity(), workerForRepair.getValue());
            maintenanceScheduleRepository.save(maintenanceSchedule);
            utils.saveInWorkerSchedule(maintenanceSchedule, WORK_DAY);
        }
    }

    private MyLinkedMap<Equipment, RepairType> getNeedRepairEquipment(List<Equipment> equipmentList)
    {
        MyLinkedMap<Equipment, Pair<RepairType, Integer>> plannedMaintenanceMap = utils.getRepairMap(equipmentList);
        if (plannedMaintenanceMap.isEmpty())
        {
            return new MyLinkedMap<>();
        }

        MyLinkedMap<Equipment, RepairType> needRepairEquipments = new MyLinkedMap<>();

         for (Map.Entry<Equipment, Pair<RepairType, Integer>> plannedMaintenance : plannedMaintenanceMap.entrySet())
        {
            if (plannedMaintenance == null)
            {
                continue;
            }

            Integer daysToRepair = plannedMaintenance.getValue().getValue();
            if (daysToRepair <= 0)
            {
                needRepairEquipments.put(plannedMaintenance.getKey(),plannedMaintenance.getValue().getKey());
            }
        }
        return needRepairEquipments;
    }

    private int[][] getWorkerToRepairMatrix(MyLinkedMap<Equipment,RepairType> plannedMaintenanceMap, MyLinkedMap<Worker, LocalDate> workers)
    {
        if (plannedMaintenanceMap == null)
        {
            return null;
        }

        int[][] matrix = new int[20][20];

        for (int i = 0; i < matrix.length; i++)
        {
            Equipment equipment = plannedMaintenanceMap.getEntry(i).getKey();
            RepairType repairType = plannedMaintenanceMap.getValue(i);
            RepairStandard repairStandard = repairStandardRepository
                    .getRepairStandardByEquipmentCategoryAndRepairType(equipment.getEquipmentCategory(), repairType);

            for (int j = 0; j < matrix.length; j++)
            {

                Worker worker = workers.getEntry(j).getKey();
                Set<Qualification> repairTypeQualifications = repairType.getQualifications();
                Set<Qualification> workerQualifications = worker.getQualifications();

                if(isQualificationWorker(repairTypeQualifications, workerQualifications))
                {
                    matrix[i][j] = repairStandard.getLaborIntensity() * worker.getTariffRate();
                }
                else
                {
                    matrix[i][j] = repairStandard.getLaborIntensity()*2*worker.getTariffRate() ;
                }
            }
        }

        return matrix;
    }

    private Boolean isQualificationWorker(Set<Qualification> repairTypeQualifications, Set<Qualification> workerQualifications)
    {
        for (Qualification repairTypeQualification : repairTypeQualifications)
        {
            for (Qualification workerQualification : workerQualifications)
            {
                if (workerQualification.getId().equals(repairTypeQualification.getId()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Worker> getFittingWorker(List<Worker> workers, LocalDate date)
    {
        if (workers == null || date == null)
        {
            return null;
        }

        List<Worker> fittingWorkers = new ArrayList<>();
        for (Worker worker : workers)
        {
            Set<WorkerSchedule> workerScheduleSet = workerScheduleRepository.getByWorkerAndDate(worker, date);

            if (workerScheduleSet == null || workerScheduleSet.isEmpty())
            {
                fittingWorkers.add(worker);
                continue;
            }

            Integer manHours = 0;

            for (WorkerSchedule workerSchedule : workerScheduleSet)
            {
                if (workerSchedule == null)
                {
                    continue;
                }

                manHours += (workerSchedule.getLaborIntensity() == null) ? 0 : workerSchedule.getLaborIntensity();
            }

            if (manHours >= WORK_DAY)
            {
                continue;
            }
            fittingWorkers.add(worker);
        }

        if (fittingWorkers.isEmpty())
        {
            return null;
        }

       // return Collections.min(fittingWorkers);
        return fittingWorkers;
    }

    //------------------------------
    private MyLinkedMap<Worker, LocalDate> getFreeWorkerInNearDays(int equipmentCount)
    {
        List<Worker> workers = workerRepository.findAll();
        MyLinkedMap<Worker, LocalDate> freeWorkers = new MyLinkedMap<>();

        if (workers.isEmpty())
        {
            return freeWorkers;
        }

        LocalDate date = LocalDate.now();

        while (freeWorkers.size() < equipmentCount)
        {
            List<Worker> fittingWorkers = getFittingWorker(workers, date);

            while (freeWorkers.size() < equipmentCount && fittingWorkers.size() > 0)
            {
                fittingWorkers.sort(Collections.reverseOrder());
                Worker bestWorker = fittingWorkers.get(fittingWorkers.size() - 1);
                freeWorkers.put(fittingWorkers.get(fittingWorkers.size() - 1), date);
                fittingWorkers.remove(fittingWorkers.size() - 1);
                workers.remove(bestWorker);
            }

            date = date.plusDays(1);
        }

        return freeWorkers;
    }
}
