$(document).ready(function(){
    $('#application-table').DataTable({
            ajax:{
                "url": "/applications",
                "dataSrc": "_embedded.applications"
            },
            columns :[
                {"data" : "firstName"},
                {"data" : "lastName"},
                {"data" : "phoneNumber",
                    "defaultContent":""},
                {"data" : "email",
                    "defaultContent":""},
                {"data" : function(data, type,row,meta){
                    return moment(data.dateReceived).format('YYYY-MM-DD h:mm:ss a');
                    },
                    "defaultContent":""
                },
                {"data" : function(data, type,row,meta){
                    return moment(data.callBackDate).format('YYYY-MM-DD h:mm:ss a');
                    },
                    "defaultContent":""
                },
                {"data" : "applicationSource.source",
                    "defaultContent":""},

                {"data" : "applicationResult.result",
                                    "defaultContent":""},
                {"data" : function(data, type,row,meta){
                    return moment(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a id="details" class="btn btn-warning" href="#">Details</a>'
//                        return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                    }
                }
            ]
    });


//TODO: add delegation
    $('#details').on('click','body', function(event){
    		event.preventDefault();
    		alert("coming soon");
    });
});