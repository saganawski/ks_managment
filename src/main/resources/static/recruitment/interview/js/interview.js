$(document).ready(function(){
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });

    function setDataTable(){
        $('#interview-table').DataTable({
            columnDefs: [
                { visible: false, targets: 2 }
            ],
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
                ajax:{
                    "url": "/interviews",
                    "dataSrc": ""
                },
                columns :[
                    {"data": "id" },
                    {"data" : function(data,type,row,meta){
                        let firstName = data.application.firstName;
                        let lastName = data.application.lastName;
                        let applicantName = firstName + " " + lastName;
                        return applicantName;
                        },
                        "defaultContent": ""},
                    {"data" : "application.email","defaultContent":""},
                    {"data" : "application.phoneNumber","defaultContent":""},
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
                    {"data" : "application.office.name","defaultContent":"" },
                    {   "targets": -1,
                        "data": function(data, type,row,meta){
                            return '<a class="btn btn-warning" href="/recruitment/interview/interview-details.html?interviewId= '+data.id + '">Details</a>'

                        }
                    }
                ],
                dom:"Bfrtip",
                buttons: ['copy','csv','pdf']
        });
    }


});