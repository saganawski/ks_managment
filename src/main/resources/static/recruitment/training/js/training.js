$(document).ready(function(){
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });

    function setDataTable(){
        $('#trainer-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
                ajax:{
                    "url": "/trainings",
                    "dataSrc": ""
                },
                columns :[
                    {"data": "id" },
                    {"data" : function(data,type,row,meta){
                        let firstName = data.application.firstName;
                        let lastName = data.application.lastName;
                        let traineeName = firstName + " " + lastName;
                        return traineeName;
                        },
                        "defaultContent": ""},
                    {"data": "application.phoneNumber","defaultContent":""},
                    {"data" : function(data,type,row,meta){
                        if(data.scheduledTime == null){
                            return " ";
                        }
                        return moment(data.scheduledTime).format('YYYY-MM-DD h:mm:ss a');
                        },
                        "defaultContent": ""},
                    {"data" : function(data,type,row,meta){
                            if(data.trainer == null){
                                return " ";
                            }
                            let firstName = data.trainer.firstName;
                            let lastName = data.trainer.lastName;
                            let trainerName = lastName + " ," + firstName;
                            return trainerName;
                        },
                        "defaultContent":""},

                    {"data" : function(data,type,row,meta){
                        if(data.application.office == null){
                            return " ";
                        }
                        return data.application.office.name;
                    },
                    "defaultContent":""},
                    {   "targets": -1,
                        "data": function(data, type,row,meta){
                            return '<a class="btn btn-warning" href="/recruitment/training/training-details.html?trainingId= '+data.id + '">Details</a>'

                        }
                    }
                ],
                dom:"Bfrtip",
                buttons: ['copy','csv','pdf']
        });
    }


});