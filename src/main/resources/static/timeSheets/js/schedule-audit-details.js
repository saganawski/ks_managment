$(document).ready(function(){
    vm = this;

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
        }
    });

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('scheduleAuditId')){
        var scheduleAuditId = searchParams.get('scheduleAuditId');
        $.ajax({
            url:"/scheduleAudits/" + scheduleAuditId
        }).then(function(data){
            setDataTable(data);
            setTitle(data);
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to retrieve schedule audit! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setDataTable(scheduleAudit){
        $('#schedule-audit-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            data: scheduleAudit.employeeSchedules,
            columns :[
                {"data" : "id"},
                {"data" : function(data,type,row,meta){
                        if(data.scheduledTime == null){
                            return "";
                        }
                        return moment(data.scheduledTime).format('YYYY-MM-DD');
                    }
                },
                {"data" : "employeeScheduleStatus.status",
                        "defaultContent": ""},
                {"data" : "employee.firstName",
                    "defaultContent": ""},
                {"data" : "employee.lastName",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.payRate",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.timeIn",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.timeOut",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.lunch",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.mileage",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.totalDayWage",
                    "defaultContent": ""},
                {"data" : "employeeSchedulePayroll.overtime",
                    "defaultContent": ""}
            ],
            dom:"Bfrtip",
            buttons: ['copy','csv','pdf']
        });
    }

    function setTitle(scheduleAudit){
        $('.card-header').append(" - " + scheduleAudit.office.name + "<br> Start Date: " + scheduleAudit.startDate + "<br> End Date: " + scheduleAudit.endDate);
    }

     $('#load-layout').on('click', '#delete-report',function(event){
         event.preventDefault();
         swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this record!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            if(willDelete){
                deleteScheduleAudit(scheduleAuditId);
            }
         });
     });

     function deleteScheduleAudit(scheduleAuditId){
         $.ajax({
             type: "DELETE",
             url: "/scheduleAudits/" + scheduleAuditId
         }).then(function(response){
             swal({
                 title: "Success!",
                 text: "You deleted this audit",
                 icon: "success",
                 timer: 2000
             }).then(function(){
                window.location.href = "/timeSheets/schedule-audit.html";
             });
         }).fail(function(error){
             console.log(error);
             swal({
                 title: "Error!",
                 text: "Could NOT remove training! \n" + error.responseJSON.message,
                 icon: "error"
             });
         });
     }
})
