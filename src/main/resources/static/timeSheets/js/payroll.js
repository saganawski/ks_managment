$(document).ready(function(){
    vm = this;

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });

    getOfficeOptions()
        .then(function(data){
            setOfficeOptions(data);
        })
        .fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Could not get Offices for drop down\n" + err.responseJSON.message,
                icon: "error"
            });
        });

    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    }

    function setOfficeOptions(offices){
        for(office of offices){
            $('#officeSelect').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>");
        }
    }

    function setDataTable(){
        $('#schedule-audit-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            ajax:{
                "url": "/scheduleAudits/payroll",
                "dataSrc": ""
            },
            columns :[
                {"data" : "id"},
                {"data" : function(data,type,row,meta){
                    if(data.office == undefined){
                        return "";
                    }
                    return data.office.name;
                }, "defaultContent": ""},
                {"data" : function(data,type,row,meta){
                        if(data.startDate == null){
                            return "";
                        }
                        return moment(data.startDate).format('YYYY-MM-DD');
                    }
                },
                {"data" : function(data,type,row,meta){
                        if(data.endDate == null){
                            return "";
                        }
                        return data.endDate;
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/timeSheets/payroll-audit-details.html?scheduleAuditId='+ data.id +'">Details</a>'
                    }
                }
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
            url: "/scheduleAudits/payroll",
            data: JSON.stringify(scheduleAudit),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You created a Schedule Payroll",
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
                text: "Could not create payroll report! \n" + error.responseJSON.message,
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
