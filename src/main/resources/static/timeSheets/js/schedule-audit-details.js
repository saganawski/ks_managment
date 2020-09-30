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
        let scheduleAuditId = searchParams.get('scheduleAuditId');
        $.ajax({
            url:"/scheduleAudits/" + scheduleAuditId
        }).then(function(data){
            setDataTable(data);
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
            ]
        });
    }

    $('#auditFormSubmit').on('click', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            let jsonForm = convertFormToJson($("#auditForm").serializeArray());
            let office = JSON.parse(jsonForm.office);
            jsonForm.office = office;
            createScheduleAudit(jsonForm);
        }

    });

    function validationCheck(){
        const form = document.querySelector('#auditForm');
        if(form.checkValidity()  === false){
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
    }

    function createScheduleAudit(scheduleAudit){
        $.ajax({
            type: "POST",
            url: "/scheduleAudits",
            data: JSON.stringify(scheduleAudit),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You created a Schedule audit",
                icon: "success",
                timer: 2000
            }).then(function(){
                $('#auditModal').modal('hide');
                location.reload();
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not update employee schedule! \n" + error.responseJSON.message,
                icon: "error"
            });
        });
    }

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }
})
