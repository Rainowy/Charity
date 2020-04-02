//Maps your button click event to the File Upload click event
$(function () {
    $(".avatar").click(function () {
        $("#file1").trigger('click');
    });
});
//Button upload file click event
$(document).ready(function () {
    $('#up-different').hide();

    if (editEnabled) {
        toggleDisabled()
    }
    var readURL = function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.avatar').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
    $(".file-upload").on('change', function () {
        readURL(this);
    });
});

$("#edit").click(function () {
    toggleDisabled();
    if(document.querySelector("#email").disabled){
        document.querySelector("#submit").disabled = true
    }


});
// ,:input[type=hidden]
function toggleDisabled() {
    var inputs = $('input, textarea, select')
        .not(':input[type=button], :input[type=hidden]'); //disabled nie jest dodawane do input type button oraz hidden(id)
    $(inputs).prop("disabled", (_, val) => !val);
    //Submit button disabled toggle
    // $('#submit').prop("disabled", (_, val) => !val) //włącza submit button przy
    //Wybierz inne zdjęcie message hidden toggle
    $('#up-different').toggle();
}
const inputs = document.querySelectorAll("input, select");
for (const el of inputs){
    el.oldValue = el.value + el.checked;
}
// Włącza submit button tylko gdy zmiana w polach
// Declares function and call it directly

// if( document.querySelector("#edit").disabled = false) {
    var setEnabled;
    (setEnabled = function() {
        var e = true;
        for (const el of inputs) {
            if (el.oldValue !== (el.value + el.checked)) {
                e = false;
                break;
            }
        }
        document.querySelector("#submit").disabled = e;
    })();

    document.oninput = setEnabled;
    document.onchange = setEnabled;

// }
//TODO sprawdzić i poukładać żeby to jakoś wyglądało oraz usunąć białe znaki z haseł !!!!