$(document).ready(function () {
	
    $('body').on('click', '#sidebarCollapse', function () {
    	$('#sidebar').toggleClass('active');
    });

    $('#nav').load("_side-bar-nav.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt =="success"){
             $('#interview-table').DataTable({
                    "initComplete": function(settings, json){
                        $("div").removeClass("spinner-border");
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
    });


});