$(document).ready(function(){
    $('#trainer-table').DataTable({
            ajax:{
                "url": "/trainings",
                "dataSrc": ""
            },
            columns :[
                {"data": "id" },
                {"data" : function(data,type,row,meta){
                    let firstName = data.application.firstName;
                    let lastName = data.application.lastName;
                    let traineeName = lastName + " ," + firstName;
                    return traineeName;
                    },
                    "defaultContent": ""},
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
                {"data" : function(data, type,row,meta){
                    return moment(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/recruitment/training/training-details.html?trainingId= '+data.id + '">Details</a>'

                    }
                }
            ]
    });


});