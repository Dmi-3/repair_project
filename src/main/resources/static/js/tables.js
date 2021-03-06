var initTable = function (tableId) {
    $(tableId).dataTable({
        order: [],
        aoColumnDefs: [{orderable: false, targets: ['no-sort']}]
    });

    updateCheckBoxex();
};

var updateCheckBoxex = function () {
    $("#mainCheckBox").click(function () {
        $('input:checkbox').prop('checked', $(this).prop("checked"));
    });

    $(".mainCheckboxClass").click(
        function () {
            if ($('input.mainCheckboxClass:checked').length === $('input.mainCheckboxClass').length)
                $("#mainCheckBox").prop("checked", true);
            else
                $("#mainCheckBox").prop("checked", false);
        });
};

var editCategory = function (id, name) {
    $("#editedCategoryId").val(id);
    $("#editedCategoryName").val(name);
};

var editEquipment = function(id, name, categoryId)
{
    $("#editEquipmentId").val(id);
    $("#editEquipmentNameInput").val(name);
    $("#editCategoriesList").val(categoryId).trigger('change');
};

var editRepairType = function(id, name)
{
    $("#editRepairTypeId").val(id);
    $("#editRepairTypeName").val(name);
};

var editRepairStandard = function (id, name, categoryId, repairTypeId, laborIntensity, repairPeriodicity) {
    $("#editRepairStandardId").val(id);
    $("#editRepairStandardName").val(name);
    $("#editCategoriesList").val(categoryId).trigger('change');
    $("#editRepairTypesList").val(repairTypeId).trigger('change');
    $("#editLaborIntensity").val(laborIntensity);
    $("#editRepairPeriodicity").val(repairPeriodicity);
};

var editRepairHistory = function (id, equipmentId, repairTypeId, equipmentDowntime, date) {
    $("#editRepairHistoryId").val(id);
    $("#editEquipmentList").val(equipmentId).trigger('change');
    $("#editRepairTypesList").val(repairTypeId).trigger('change');
    $("#editEquipmentDowntime").val(equipmentDowntime);
    $("#editDate").val(date);
};

var editQualification = function (id, name, repairTypeId) {
    $("#editQualificationId").val(id);
    $("#editQualificationName").val(name);
    $("#editRepairTypesList").val(repairTypeId).trigger('change');
};

var editWorker = function (id, name, qualifications, tariffRate) {
    $("#editWorkerId").val(id);
    $("#editWorkerName").val(name);
    $("#editQualificationsList").val($.parseJSON(qualifications)).trigger('change');
    $("#editTariffRate").val(tariffRate);
};

var editMaintenanceSchedule = function (id, equipmentId, repairTypeId, workerId, laborIntensity, date) {

    $("#editMaintenanceScheduleId").val(id);
    $("#editEquipmentList").val(equipmentId).trigger('change');
    $("#editRepairTypesList").val(repairTypeId).trigger('change');
    $("#editWorker").val(workerId).trigger('change');
    $("#editLaborIntensity").val(laborIntensity);
    $("#editDate").val(date);
};

var editMainSchedule = function (id, equipmentId, repairTypeId, workerId, laborIntensity, date) {
    var editWorkersList = $("#editWorkersList");
    var editRepairTypeList = $("#editRepairTypesList");
    var editEquipmentsList = $("#editEquipmentList");
    var editDate =  $("#editDate");
    editDate.val(date);
    $("#editMainScheduleId").val(id);
    editRepairTypeList.val(equipmentId).trigger('change');
    editEquipmentsList.val(repairTypeId).trigger('change');
    $("#editLaborIntensity").val(laborIntensity);


    editWorkersList.val(workerId).trigger('change');

};

var editWorkerSchedule = function (id, workerId, date, laborIntensity, maintenanceScheduleId) {
    console.log("Test");
    $("#editWorkerScheduleId").val(id);
    $("#editWorker").val(workerId);
    $("#editDate").val(date);
    $("#editLaborIntensity").val(laborIntensity);
    $("#editMaintenanceScheduleId").val(maintenanceScheduleId);
};

var autoSetLaborIntensity = function () {
    var repairTypeList = $("#repairTypesList");
    var equipmentsList = $("#equipmentList");

    var editRepairTypeList = $("#editRepairTypesList");
    var editEquipmentsList = $("#editEquipmentList");

    sendStandardValueRequest(repairTypeList.val(), equipmentsList.val(), "#laborIntensity");

    repairTypeList.change(function () {
        sendStandardValueRequest(repairTypeList.val(), equipmentsList.val(), "#laborIntensity")
    });

    equipmentsList.change(function () {
        sendStandardValueRequest(repairTypeList.val(), equipmentsList.val(),"#laborIntensity")
    });

    editEquipmentsList.change(function () {
        sendStandardValueRequest(editRepairTypeList.val(), editEquipmentsList.val(),"#editLaborIntensity")
    });

    editRepairTypeList.change(function () {
        sendStandardValueRequest(editRepairTypeList.val(), editEquipmentsList.val(), "#editLaborIntensity")
    });


};

var autoSetFreeWorkers = function (repairTypeListId, workersListId, dateId) {
    var repairTypeList = $(repairTypeListId);
    var workersList = $(workersListId);
    var date = $(dateId);

    date.change(function () {
        sendForGettingFreeWorkersRequest(repairTypeList.val(), date.val(), workersList)
    });

    repairTypeList.change(function () {
        sendForGettingFreeWorkersRequest(repairTypeList.val(), date.val(), workersList)
    });
};

var sendStandardValueRequest = function (equipmentId, repairTypeId, laborIntensityInputId) {
    $.ajax({
        type: "POST",
        cache: false,
        url: '/repair-standards/getLaborIntensity',
        data: {
            equipmentId: equipmentId,
            repairTypeId: repairTypeId
        },
        success: function (response) {
            $(laborIntensityInputId).val(response);
        }
    });
};

var sendForGettingFreeWorkersRequest = function (repairTypeId, date, workersList) {
    $.ajax({
        type: "POST",
        cache: false,
        url: '/workers/getByRepairType',
        data: {
            repairTypeId: repairTypeId,
            date: date
        },
        success: function (response) {
            workersList.find('option').remove();
            $.each(response, function(i, worker){
                var option = "<option value=" + i + '>' + worker + "</option>";
                workersList.append(option);
            });
        },
        error: function (response)
        {
            alert("error");
        }
    });

};

/*
var removeCategories = function () {
    var selectedCheckBoxes = $('input.mainCheckboxClass:checked')
    var checkBoxesIds = [];
    for (var i=0;i<selectedCheckBoxes.length;i++) {

        if (selectedCheckBoxes[i]==null)
        {
            continue;
        }

        checkBoxesIds.push(selectedCheckBoxes[i].id.split('-')[2]);
    }
    if (checkBoxesIds.length > 0) {
        $.ajax({
            type: "POST",
            cache: false,
            url: '/remove',
            data: {
                checkBoxesIds: checkBoxesIds.toString()
            }
            ,
            success : function(response) {
                //location.reload();
                window.location.href = response;
            }
        });
    }
};*/

