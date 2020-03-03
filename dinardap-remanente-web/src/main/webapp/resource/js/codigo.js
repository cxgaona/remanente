/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function teste() {
    var checked = $(document).find(":checkbox")["0"].checked; ///Find checkbox header and verify if checkbox is checked
    if (checked == true) {
        PF('tramiteTbl').selectAllRows(); // if true, selectAllRows from datatable
    } else {
        PF('tramiteTbl').unselectAllRows(); //
    }
}

