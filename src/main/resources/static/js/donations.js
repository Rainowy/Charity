$('#hideSecond').hide();
$('td').click(function () {
  var checked = $('input', this).is(':checked');
  $('span', this).text(checked ? ' Tak ' : ' Nie ');
});
$(":checkbox").change(function () {
  $(this).closest('td').next('td').find('input').prop("disabled", !this.checked);
  $(this).closest('td').next('td').next('td').find('input').prop("disabled", !this.checked);
});
$("#hide").click(function () {
  $('#hideFirst').toggle();
  $('#hideSecond').toggle();
});
$(document).ready(function() {
  $('#dataTable2').DataTable();
  $('#dataTable').DataTable();
});


