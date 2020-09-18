$(document).ready(function () {
    var vm = this;

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });

    function setDataTable(){
        $('#project-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            ajax:{
                "url": "/projects",
                "dataSrc": ""
            },
            columns :[
                {"data" : "id",
                    "defaultContent": ""},
                {"data" : "name",
                    "defaultContent":""},
                {   "targets": -1,
                    "data": function(data, type,row,meta){
//                        return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                        return '<a class="btn btn-warning" href="#">Details</a>'
                    }
                }
            ]
        });
    }

});