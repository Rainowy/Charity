$(document).ready(function () {
    let nameItems = document.querySelectorAll('#name');
    let descriptionItems = document.querySelectorAll('#description');
    let checkedItem = ''

    $('#sendButton').attr('disabled', true);
    $('.sub').attr('disabled', true);
    addEventToItems(nameItems);
    addEventToItems(descriptionItems);

    $('#newInstName').keyup(function () {
        if ($(this).val().length != 0 && $('#newInstDescription').val().length != 0) {
            $('#sendButton').attr('disabled', false);
            $('#hide').attr('disabled', true);
            disableAllSubmit();
        } else {
            $('#sendButton').attr('disabled', true)
            $('#hide').attr('disabled', false);
            disableCurrentSubmit();
        }
    })
    $('#newInstDescription').keyup(function () {
        if ($(this).val().length != 0 && $('#newInstName').val().length != 0) {
            $('#sendButton').attr('disabled', false);
            $('#hide').attr('disabled', true);
            disableAllSubmit()
        } else {
            $('#sendButton').attr('disabled', true)
            $('#hide').attr('disabled', false);
            disableCurrentSubmit();
        }
    })

    function disableCurrentSubmit() {
        if ($(checkedItem).closest('tr').find('input#name').attr('disabled') !== undefined) {
            $(checkedItem).closest('tr').find('input#submit').prop("disabled", true);
        }
    }

    function disableAllSubmit() {
        const submitButtons = document.querySelectorAll('#submit')
        submitButtons.forEach(function (currentBtn) {
            currentBtn.disabled = true;
        })
    }

    if (unhide !== "true") {
        console.log(unhide)
        $('#hideNew').hide();
    }

    $("#hide").click(function () {
        $('#hideNew').toggle();
    });
    
    function drawSubmitTimeout() {
        table.on('draw.dt', function () {
            setTimeout(function () {
                addEventToItems(nameItems);
                addEventToItems(descriptionItems);
                disableAllSubmit()
            }, 300);
        });
    }

    function addEventToItems(items) {
        items.forEach(item => {
            item.addEventListener('click', function () {
                valueChanged(items);
            })
        })
    }

    function valueChanged(items) {
        for (const el of items) {
            el.oldValue = el.value + el.checked;
        }
        var setEnabled;
        (setEnabled = function () {
            var e = true;
            for (const el of items) {
                if (el.oldValue !== (el.value + el.checked) && $('#sendButton').attr('disabled') !== undefined) {
                    e = false;
                    checkedItem = el;
                    break;
                }
            }
            $(checkedItem).closest('tr').find('input#submit').prop("disabled", e)
        })();
        document.oninput = setEnabled;
    }

    /** Specific dataTables Functions starts here */
    //Custom dataTable search(input="text")
    $.fn.dataTableExt.ofnSearch['html-input'] = function (value) {
        return $(value).val();
    };
    var table = $("#dataTable").DataTable({
        columnDefs: [
            {"type": "html-input", "targets": [1, 2, 3]}]
    });

    table.on('length.dt', drawSubmitTimeout());
    table.on('page.dt', drawSubmitTimeout());

    table.on('click', '#edit', function () {
        $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
        $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
        $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
        checkedItem = ''
    });

//invalidate old input value when editing for proper search
    $("#dataTable td input").on('change', function () {
        var $td = $(this).parent();
        $td.find('input').attr('value', this.value);
        table.cell($td).invalidate().draw();
    });
//invalidate old select value when editing for proper search
    $("#dataTable td select").on('change', function () {
        var $td = $(this).parent();
        var value = this.value;
        $td.find('option').each(function (i, o) {
            $(o).removeAttr('selected');
            if ($(o).val() == value) $(o).attr('selected', true);
        })
        table.cell($td).invalidate().draw();
    });
})