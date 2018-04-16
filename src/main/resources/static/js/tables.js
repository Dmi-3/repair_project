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

