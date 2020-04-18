$('#hideSecond').hide();
// $('td').click(function () {
//     var checked = $('input', this).is(':checked');
//     $('span', this).text(checked ? ' Tak ' : ' Nie ');
// });



// let kupa = $('.name');
// if(kupa.val("disabled",true)){
//     console.log("NIE ZAZNACZONY")

$("#edit").click(function () {
//
//     // if (document.querySelector(".nazwa").disabled = false) {        //wyłącza submit button gdy edycja wyłączona
//         document.querySelector(".nazwa").disabled = true
//     // }
//
//     console.log("ZMIANA")



   $(this).closest('tr').find('input.nazwa').toggle("disabled");

    // if(kot == true){
    //     console.log("NIE ZAZNACZONY")
    // }
    // $(this).closest('td').next('td').next('td').find('input').prop("disabled", !this.checked);
});
// $("#hide").click(function () {
//     $('#hideFirst').toggle();
//     $('#hideSecond').toggle();
// });
// $(document).ready(function() {
//     $('#dataTable2').DataTable();
//     $('#dataTable').DataTable();
// });
