$(document).ready(function () {

    $('#sendButton').attr('disabled', true);
    $('.sub').attr('disabled', true);

    $('#newInstName').keyup(function () {
        if ($(this).val().length != 0 && $('#newInstDescription').val().length != 0) {
            $('#sendButton').attr('disabled', false);
            disableSubmit();
        } else {
            $('#sendButton').attr('disabled', true)
        }
    })
    $('#newInstDescription').keyup(function () {
        if ($(this).val().length != 0 && $('#newInstName').val().length != 0) {
            $('#sendButton').attr('disabled', false);
            disableSubmit()
        } else {
            $('#sendButton').attr('disabled', true);
        }
    })
})

function disableSubmit() {
    const submitButtons = document.querySelectorAll('#submit')
    submitButtons.forEach(function (currentBtn) {
        currentBtn.disabled = true;
    })
}

// document.getElementById("sendButton").addEventListener("change", function () {
//     console.log("ZMIENANAFD SIĘ")
//     // if ($('#sendButton').attr('disabled')) {
//     turno()
//
// });


// function turno() {
//     // document.querySelectorAll('#submit').forEach(function () {
//     //     // //     document.getElementById('#submit').attr('disabled', false)
//     //     $(this).attr('disabled', true)
//     //     //     // item.attr('disabled', true);
//     const editButtons = document.querySelectorAll('#submit')
//     editButtons.forEach(function (currentBtn) {
//         currentBtn.disabled = true;
//
//     })
// }

// $('#sendButton').change(() => {
//     if ($('#sendButton').attr('disabled')) {
//         turno()
//     }
// });


if (unhide !== "true") {
    console.log(unhide)
    $('#hideNew').hide();
}

// if ($('#newInstName').val().length == 0 && $('#newInstDescription').val().length == 0) {
//     console.log("OBA zero")

var b = ''

const editButtons = document.querySelectorAll('#edit')
editButtons.forEach(function (currentBtn) {
    currentBtn.addEventListener('click', function () {
        $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
        $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
        // $(this).closest('tr').find('input#submit').prop("disabled", (_, val) => !val);
        $(b).closest('tr').find('input#submit').prop("disabled", true)
        b = ''
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

// let elementNodeListOf = document.querySelectorAll('#name');
//
// elementNodeListOf.forEach(function (currentBtn) {
let elementNodeListOf = document.querySelectorAll('#name');
elementNodeListOf.forEach(item => {
    item.addEventListener('click', function () {
// elementNodeListOf.forEach(function () {
//     elementNodeListOf.addEventListener('change', function () {


        for (const el of elementNodeListOf) {
            el.oldValue = el.value + el.checked;
        }

        var setEnabled;
        (setEnabled = function () {

            var e = true;
            for (const el of elementNodeListOf) {
                if (el.oldValue !== (el.value + el.checked) && $('#sendButton').attr('disabled') !== undefined) {
                    e = false;
                    b = el;
                    // $("name").closest('tr').find('input#submit').prop("disabled", e);
                    // $(el).closest('tr').find('input#submit').prop("disabled", e)
                    break;
                }
            }//todo zrobić żeby ten blisko elementlist sie odpalal
            $(b).closest('tr').find('input#submit').prop("disabled", e)
            // document.querySelector("#submit").disabled = e;
            // $(' el.oldValue').closest('tr').find('input#submit').prop("disabled", e);
        })();
        document.oninput = setEnabled;
    })
})

// document.oninput = setEnabled;
// document.onchange = setEnabled;
// const inputs = document.querySelectorAll('#name')
// inputs.forEach(addEventListener('change', (event) => { {
//
//
//
// }
//     currentBtn.addEventListener('change', function () {
//         document.querySelector("#submit").disabled = false;
//         // for (const el of inputs) {
//         //     el.oldValue = el.value + el.checked;
//         // }
//     })
//
// });

// const inputs = $('input, textarea, select')
// for (const el of inputs) {
//     el.oldValue = el.value + el.checked;
// }

// var setEnabled;
// (setEnabled = function () {
//     var e = true;
//     for (const el of inputs) {
//         if (el.oldValue !== (el.value + el.checked)) {
//             e = false;
//             break;
//         }
//     }
//     document.querySelector("#submit").disabled = e;
// })();
//
// document.oninput = setEnabled;
// document.onchange = setEnabled;
