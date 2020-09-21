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
                {"data" : "office.name",
                    "defaultContent":""},
                {"data" : function(data,type,row,meta){
                        if(data.completed == false){
                            return "In Progress";
                        }
                        return "Completed";
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/reports/shift-goal/project-details.html?projectId= '+data.id+ '  ">Details</a>'
                    }
                }
            ]
        });
    }

});