$(document).ready(function () {
	// TODO: something to load side bar and nav

    $('#employee-table').DataTable({
        "initComplete": function(settings, json){
            $("div").removeClass("spinner-border");
        },
        ajax:{
            "url": "/employees",
            "dataSrc": ""
        },
        columns :[
            {"data" : "id"},
            {"data" : "firstName"},
            {"data" : "lastName"},
            {"data" : "email"},
            {"data" : "position.name",
                "defaultContent":""},
            {"data" : function(data,type,row,meta){
                if(data.offices == undefined || data.offices.length === 0 ){
                    return "";
                }
                let officeName = "";
                for(office of data.offices){
                    if(officeName.length ==0){
                        officeName = office.name;
                    }else{
                        officeName = officeName + ", " + office.name
                    }
                }
                return officeName;
            }, "defaultContent": ""},
            {   "targets": -1,
                "data": function(data, type,row,meta){
                    return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                }
            }
        ],
        dom:"Bfrtip",
        buttons: ['copy','csv','pdf']
    });
});