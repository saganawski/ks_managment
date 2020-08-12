$(document).ready(function () {

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt =="success"){
            $('#load-layout').append(main);
            setInterviewTable();
            setTrainingTable();
        }
    });

    function setInterviewTable(){
        $('#interview-table').DataTable({
            "initComplete": function(settings, json){
                $("#interviewLoad").removeClass("spinner-border");
            },
            ajax:{
                "url": "/interviews/todays",
                "dataSrc": ""
            },
            columns :[
                {"data": "id" },
                {"data" : function(data,type,row,meta){
                    let firstName = data.application.firstName;
                    let lastName = data.application.lastName;
                    let applicantName = lastName + " ," + firstName;
                    return applicantName;
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
                        if(data.interviewers == null){
                            return " ";
                        }
                        let interviewersLastNames = data.interviewers.map(interviewer => interviewer.lastName);
                        return interviewersLastNames;
                    },
                    "defaultContent":""},
                {"data" : function(data, type,row,meta){
                    return moment(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/recruitment/interview/interview-details.html?interviewId= '+data.id + '">Details</a>'

                    }
                }
            ]
        });
    }
    function setTrainingTable(){
        $('#trainer-table').DataTable({
                "initComplete": function(settings, json){
                    $("#trainingLoad").removeClass("spinner-border");
                },
                    ajax:{
                        "url": "/trainings/todays",
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
    }

});