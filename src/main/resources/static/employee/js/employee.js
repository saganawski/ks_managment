$(document).ready(function () {
	// TODO: something to load side bar and nav
//    //TODO: does not handle nulls. maybe do a DTO

    $('#employee-table').DataTable({
        ajax:{
            "url": "/employees",
            "dataSrc": ""
        },
        columns :[
            {"data" : "firstName"},
            {"data" : "lastName"},
            {"data" : "email"},
            {"data" : "position.name",
                "defaultContent":""},
            {   "targets": -1,
                "data": function(data, type,row,meta){
                    return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                }
            }
        ]
    })
});