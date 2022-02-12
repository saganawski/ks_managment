$(document).ready(function () {
    var vm = this;
    vm.offices = [];

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });

    function setDataTable(){
        $('#office-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            ajax: {
                url: "/offices/all",
                dataSrc: ''
            },
            columns : [
                {"data": "name"},
                {"data": "location.address1",
                    "defaultContent":""
                },
                {"data": "completed",
                    "defaultContent": ""
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/office/office-details.html?officeId= '+data.id + '">Details</a>'

                    }
                }
            ]
        });
    }

});