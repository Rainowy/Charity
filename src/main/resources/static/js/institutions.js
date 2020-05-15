let namesItems = document.querySelectorAll('#name');
let descriptionsItems = document.querySelectorAll('#description');
let checkedItem = ''

//Custom dataTable search(input="text")
$.fn.dataTableExt.ofnSearch['html-input'] = function (value) {
    return $(value).val();
};

var table = $("#dataTable").DataTable({
    columnDefs: [
        {"type": "html-input", "targets": [1, 2, 3]}
    ]
});
$('#dataTable').on('length.dt', function (e, settings, len) {
    console.log('New page length: ' + len);
    //

    $('#dataTable').on('draw.dt', function () {


        setTimeout(function () {
            // let allNames = document.querySelectorAll('#name');
            // allNames.forEach(item => {
            //     item.addEventListener('click', function () {
            //         valueChanged(allNames);
            //     })
            // })
            //
            //
            // let allDescriptions = document.querySelectorAll('#description');
            // allDescriptions.forEach(item => {
            //     item.addEventListener('click', function () {
            //         valueChanged(allDescriptions);
            //     })
            // })
            combine(namesItems);
            combine(descriptionsItems);
            disableAllSubmit()

        },
            300);

    });

});


function disableOnly() {
    const submitButtons = document.querySelectorAll('#submit')
    submitButtons.forEach(function (currentBtn) {
        if (currentBtn.disabled == false) {
            currentBtn.disabled = true;
        }

    })
}

//invalidate old value when editing for proper search
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

$(document).ready(function () {

    $('#dataTable').DataTable(); //necessary for dataTables to work
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
/** TUTAJ */
// $('#dataTable').dataTable( {
//     "drawCallback": function( settings ) {
//         alert( 'DataTables has redrawn the table' );
//     }
// } );


$('#dataTable').on('click', '#edit', function () {
    $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
    $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
    $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
    // $(this).closest('tr').find('input#submit').prop("disabled", true)
    checkedItem = ''

});


// const editButtons = document.querySelectorAll('#edit')
// editButtons.forEach(function (currentBtn) {
//     currentBtn.addEventListener('click', function () {
//         $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
//         $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
//         $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
//         checkedItem = ''
//     })
// })

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


function combine(items) {
    let allNames = document.querySelectorAll('#name');
    items.forEach(item => {
        item.addEventListener('click', function () {
            valueChanged(items);
        })
    })

    // let allDescriptions = document.querySelectorAll('#description');
    // allDescriptions.forEach(item => {
    //     item.addEventListener('click', function () {
    //         valueChanged(allDescriptions);
    //     })
    // })

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


// let checkedItem = ''
//
// //Custom dataTable search(input="text")
// $.fn.dataTableExt.ofnSearch['html-input'] = function (value) {
//     return $(value).val();
// };
//
// var table = $("#dataTable").DataTable({
//
//     // "lengthMenu": [ [10, 25, 50, 100, -1], [10, 25, 50, 100, "All"] ],
//     columnDefs: [
//         {"type": "html-input", "targets": [1, 2, 3]}
//     ]
//
// });
// // let kolec = $('#dataTable').on('page.dt', function () {
// //
// //     // table.buttons().disable();
// //     $('#dataTable').DataTable( {
// //         initComplete: function(settings, json) {
// //             disableAllSubmit()
// //         }
// //     } );
// //
// //     // var info = table.page.info();
// //     // $('#pageInfo').html('Showing page: ' + info.page + ' of ' + info.pages);
// //     console.log("KLIKNIĘTE")
// //     // disableAllSubmit()
// // });
//
//
//     // $('#dataTable').on('page.dt', function () {
//     //     // var table = $('#dataTable').DataTable({
//     //     //     initComplete: function () {
//     //     //         console.log('In initComplete');
//     //         }
//     //     });
//     // });
//
//
//
// //invalidate old value when editing for proper search
// $("#dataTable td input").on('change', function () {
//     var $td = $(this).parent();
//     $td.find('input').attr('value', this.value);
//     table.cell($td).invalidate().draw();
// });
// //invalidate old select value when editing for proper search
// $("#dataTable td select").on('change', function () {
//     var $td = $(this).parent();
//     var value = this.value;
//     $td.find('option').each(function (i, o) {
//         $(o).removeAttr('selected');
//         if ($(o).val() == value) $(o).attr('selected', true);
//     })
//     table.cell($td).invalidate().draw();
// });
// var table = $('#dataTable').DataTable();
//
// // table.on( 'draw', function () {
// //     console.log( 'Redraw occurred at: '+new Date().getTime() );
// //     disableOnly()
// // } );
//
// $(document).ready(function () {
//     //
//     // table.on(('click',  $('select[name="dataTable_length"]'), function () {
//     //     $('submit').attr('disabled', true);
//     // }))
//     // $('select[name="dataTable_length"]')
//     //
//     // var jQuery = $('#dataTable_length').closest('label').find('select[name="dataTable_length"]');
//     // jQuery.addEventListener("click",function () {
//     //     console.log("KLIKNIĘTE")
//
//     // })
//
//     // $('#dataTable').on('click', '#dataTable_length', function(){
//     //     $(this).closest('label').find('select#name').prop("disabled", (_, val) => !val);
//     //
//     //     disableAllSubmit()
//     //
//     //  // });
//     // $('#dataTable').on('page.dt', function () {
//     //     var table = $('#dataTable').DataTable();
//     //      // table.buttons().disable();
//     //     $('#dataTable').dataTable( {
//     //         "initComplete": function(settings, json) {
//     //             disableAllSubmit()
//     //         }
//     //     } );
//     //
//     //      // var info = table.page.info();
//     //      // $('#pageInfo').html('Showing page: ' + info.page + ' of ' + info.pages);
//     //      console.log("KLIKNIĘTE")
//     //      // disableAllSubmit()
//     //  })
//     //  var table = $('#dataTable').DataTable();
//     //  var buttons = table.buttons( ['.edit', '.delete'] );
//     //
//     //  if ( table.rows( { selected: true } ).indexes().length === 0 ) {
//     //      buttons.disable();
//     //  }
//     //  else {
//     //      buttons.enable();
//     //  }
//
//
// });
// // $(document).ready(function() {
// //    var oTable = $('#dataTable').dataTable( {
// //         "fnDrawCallback": function () {
// //             setTimeout( function () {
// //                disableAllSubmit();
// //             }, 0 );
// //         }
// //     } );
// // } );
//
// // $(document).ready(function() {
// //     var table = $('#example').DataTable( {
// //         dom: 'Bfrtip',
// //         select: true,
// //         buttons: [
// //             {
// //                 text: 'Row selected data',
// //                 action: function ( e, dt, node, config ) {
// //                     alert(
// //                     'Row data: '+
// //                     JSON.stringify( dt.row( { selected: true } ).data() )
// //                         );
// //                 },
// //                 enabled: false
// //             },
// //             {
// //                 text: 'Count rows selected',
// //                 action: function ( e, dt, node, config ) {
// //                     alert( 'Rows: '+ dt.rows( { selected: true } ).count() );
// //                 },
// //                 enabled: false
// //             }
// //         ]
// //     } );
// //
// //     table.on( 'select deselect', function () {
// //         var selectedRows = table.rows( { selected: true } ).count();
// //
// //         table.button( 0 ).enable( selectedRows === 1 );
// //         table.button( 1 ).enable( selectedRows > 0 );
// //     } );
// // } );
//
//
// $('#dataTable').on('click', '#edit', function () {
//     var table = $('#dataTable').DataTable();
//     // table.buttons().enable();
//     // Reset form
//     $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
//     $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
//     $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
//     checkedItem = ''
//     // $('#form-edit').get(0).reset();
//     //
//     // // Store current row
//     // $('#modal-edit').data('row', $(this).closest('tr'));
//     //
//     // // Show dialog
//     // $('#modal-edit').modal('show');
// });
//
// $('#dataTable').DataTable(); //necessary for dataTables to work
// $('#sendButton').attr('disabled', true);
// $('.sub').attr('disabled', true);
//
// $('#newInstName').keyup(function () {
//     if ($(this).val().length != 0 && $('#newInstDescription').val().length != 0) {
//         $('#sendButton').attr('disabled', false);
//         $('#hide').attr('disabled', true);
//         disableAllSubmit();
//     } else {
//         $('#sendButton').attr('disabled', true)
//         $('#hide').attr('disabled', false);
//         disableCurrentSubmit();
//     }
// })
// $('#newInstDescription').keyup(function () {
//     if ($(this).val().length != 0 && $('#newInstName').val().length != 0) {
//         $('#sendButton').attr('disabled', false);
//         $('#hide').attr('disabled', true);
//         disableAllSubmit()
//     } else {
//         $('#sendButton').attr('disabled', true)
//         $('#hide').attr('disabled', false);
//         disableCurrentSubmit();
//     }
// })
//
//
// function disableCurrentSubmit() {
//     if ($(checkedItem).closest('tr').find('input#name').attr('disabled') !== undefined) {
//         $(checkedItem).closest('tr').find('input#submit').prop("disabled", true);
//     }
// }
//
// function disableAllSubmit() {
//     const submitButtons = document.querySelectorAll('#submit')
//     submitButtons.forEach(function (currentBtn) {
//         currentBtn.disabled = true;
//     })
// }
//
// function disableOnly() {
//     const submitButtons = document.querySelectorAll('#submit')
//     submitButtons.forEach(function (currentBtn) {
//         // if(currentBtn.disabled=false){
//         currentBtn.disabled = true;
//         // }
//
//     })
// }
//
// if (unhide !== "true") {
//     console.log(unhide)
//     $('#hideNew').hide();
// }
//
// // const editButtons = document.querySelectorAll('#edit')
// // editButtons.forEach(function (currentBtn) {
// //     currentBtn.addEventListener('click', function () {
// //         $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
// //         $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
// //         $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
// //         checkedItem = ''
// //     })
// // })
//
// $("#hide").click(function () {
//     $('#hideNew').toggle();
// });
//
// // document.querySelectorAll('#name').forEach(item => {
// //     item.addEventListener('change', function () {
// //         if ($('#sendButton').attr('disabled') !== undefined) {
// //             $(this).closest('tr').find('input#submit').prop("disabled", false);
// //         } else $(this).closest('tr').find('input#submit').prop("disabled", true);
// //     })
// // })
//
// let allNames = document.querySelectorAll('#name');
// allNames.forEach(item => {
//     item.addEventListener('click', function () {
//         valueChanged(allNames);
//     })
// })
//
// let allDescriptions = document.querySelectorAll('#description');
// allDescriptions.forEach(item => {
//     item.addEventListener('click', function () {
//         valueChanged(allDescriptions);
//     })
// })
//
// function valueChanged(items) {
//     for (const el of items) {
//         el.oldValue = el.value + el.checked;
//     }
//     var setEnabled;
//     (setEnabled = function () {
//         var e = true;
//         for (const el of items) {
//             if (el.oldValue !== (el.value + el.checked) && $('#sendButton').attr('disabled') !== undefined) {
//                 e = false;
//                 checkedItem = el;
//                 break;
//             }
//         }
//         $(checkedItem).closest('tr').find('input#submit').prop("disabled", e)
//     })();
//     document.oninput = setEnabled;
// }
//
// //
// //
// // $(document).ready(function () {
// //     var table = $('#example').DataTable({
// //         dom: 'Bfrtip',
// //         select: true,
// //         buttons: [
// //             {
// //                 text: 'Row selected data',
// //                 action: function (e, dt, node, config) {
// //                     // $(this).closest('tr').find('input#submit').prop("disabled", false);
// //                     // $(this).closest('tr').find('input#name').prop("disabled", (_, val) => !val);
// //                     // $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
// // console.log(dt.row({selected: true}))
// //                     $(dt.row({selected: true})).find('input#name').prop("disabled", (_, val) => !val);
// //
// //                     // $(checkedItem).closest('tr').find('input#submit').prop("disabled", true)
// //                     // checkedItem = ''
// //
// //                     console.log("CLICKA")
// //                     // alert(
// //                     // 'Row data: ' +
// //                     // JSON.stringify(dt.row({selected: true}).data())
// //                     //     );
// //                 },
// //                 enabled: false
// //             },
// //             {
// //                 text: 'Count rows selected',
// //                 action: function (e, dt, node, config) {
// //                     alert('Rows: ' + dt.rows({selected: true}).count());
// //                 },
// //                 enabled: false
// //             }
// //         ]
// //     });
// //
// //     table.on('select deselect', function () {
// //         // $(this).closest('tr').find('input#submit').prop("disabled", false);
// //         // $(this).find('input#name').prop("disabled", (_, val) => !val);
// //         // $(this).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
// //         // console.log("ZAZNACZONE")
// //         var selectedRows = table.rows({selected: true}).count();
// //         // let rows = table.rows({selected: true});
// //         // $(rows).find('input#name').prop("disabled", (_, val) => !val);
// //         // $(rows).closest('tr').find('input#description').prop("disabled", (_, val) => !val);
// //
// //         table.button(0).enable(selectedRows === 1);
// //         table.button(1).enable(selectedRows > 0);
// //     });
// // });