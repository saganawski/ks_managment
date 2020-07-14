$(document).ready(function(){
    $('#application-table').DataTable({
            "fnInitComplete": function (oSettings, json) {
                $("div").removeClass("spinner-border");
            },
            ajax:{
                "url": "/applications",
                dataSrc: ''
            },
            columns :[
                {"data" : "firstName"},
                {"data" : "lastName"},
                {"data" : "phoneNumber",
                    "defaultContent":""},
                {"data" : "email",
                    "defaultContent":""},
                {"data" : function(data, type,row,meta){
                    if(data.dateReceived != null){
                        return moment.utc(data.dateReceived).format('YYYY-MM-DD');
                    }else{
                        return " ";
                    }
                    },
                    "defaultContent":""
                },
                {"data" : function(data, type,row,meta){
                    if(data.callBackDate != null){
                        return moment.utc(data.callBackDate).format('YYYY-MM-DD');
                    }else{
                        return " ";
                    }
                    },
                    "defaultContent":""
                },
                {"data" : "applicationSource.source",
                    "defaultContent":""},

                {"data" : "applicationResult.result",
                                    "defaultContent":""},
                {"data" : function(data, type,row,meta){
                    return moment.utc(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
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