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

var editRepairStandard = function (id, name, categoryId, repairTypeId, laborIntensity) {
    $("#editRepairStandardId").val(id);
    $("#editRepairStandardName").val(name);
    $("#editCategoriesList").val(categoryId).trigger('change');
    $("#editRepairTypesList").val(repairTypeId).trigger('change');
    $("#editLaborIntensity").val(laborIntensity);
}

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

