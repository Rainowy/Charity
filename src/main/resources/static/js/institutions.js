const editButtons = document.querySelectorAll('#edit')
editButtons.forEach(function (currentBtn) {
    currentBtn.addEventListener('click', function () {
        $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
        $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
    })
});
