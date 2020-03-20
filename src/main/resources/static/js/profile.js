//Maps your button click event to the File Upload click event
$(function(){
    $(".avatar").click(function () {
        $("#file1").trigger('click');
    });
});
//Button upload file click event
$(document).ready(function () {
    $('#up-different').hide();

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
    //Select all inputs insted of button, also  submit, reset can exists
    var inputs = $('input, textarea, select')
        .not(':input[type=button]');
    $(inputs).prop("disabled", (_, val) => !val);
    //Submit button disabled toggle
    $('#submit').prop("disabled", (_, val) => !val)
    //Wybierz inne zdjęcie message hidden toggle
    $('#up-different').toggle();


});
// :input[type=submit],