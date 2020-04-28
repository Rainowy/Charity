let checkedItem = ''

$(document).ready(function () {

    $('#sendButton').attr('disabled', true);
    $('.sub').attr('disabled', true);

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

const editButtons = document.querySelectorAll('#edit')
editButtons.forEach(function (currentBtn) {
    currentBtn.addEventListener('click', function () {
        $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
        $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
        $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
        checkedItem = ''
    })
})

$("#hide").click(function () {
    $('#hideNew').toggle();
});

// document.querySelectorAll('#name').forEach(item => {
//     item.addEventListener('change', function () {
//         if ($('#sendButton').attr('disabled') !== undefined) {
//             $(this).closest('tr').find('input#submit').prop("disabled", false);
//         } else $(this).closest('tr').find('input#submit').prop("disabled", true);
//     })
// })

let allNames = document.querySelectorAll('#name');
allNames.forEach(item => {
    item.addEventListener('click', function () {
        valueChanged(allNames);
    })
})

let allDescriptions = document.querySelectorAll('#description');
allDescriptions.forEach(item => {
    item.addEventListener('click', function () {
        valueChanged(allDescriptions);
    })
})

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
