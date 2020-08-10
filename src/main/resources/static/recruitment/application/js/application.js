$(document).ready(function(){
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDatatable();
        }
    });

    function setDatatable(){
        $('#application-table').DataTable({
            responsive: true,
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
                    {"data" : "office.name",
                        "defaultContent":""},
                    {"data" : function(data, type,row,meta){
                        return moment.utc(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                        }
                    },
                    {   "targets": -1,
                        "data": function(data, type,row,meta){
                            return '<a class="btn btn-warning" href="/recruitment/application/application-edit.html?applicationId='+ data.id +'">Details</a>'
                        }
                    }
                ],
                dom:"Bfrtip",
                buttons: ['copy','csv','pdf']
        });
    }
});